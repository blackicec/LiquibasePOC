package app.migrations.custom;

import app.constants.DatabaseInfo;
import app.constants.DatabaseTable;
import app.data.models.enums.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.*;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;
import liquibase.statement.core.UpdateStatement;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class M001_FixOrderStatus implements CustomTaskChange, CustomTaskRollback {

    private final HashMap<String, String> oldToNewMap = new HashMap<>() {
        {
            put(OrderStatus.Error.toString(), OrderStatus.Warning.toString());
            put(OrderStatus.Problematic.toString(), OrderStatus.Hopeless.toString());
        }
    };
    private int rowsEffected;

    public void execute(Database database) {
        //statement.addColumn("id", new IntType());

        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<SqlStatement> statements = new ArrayList<>();
        List<String> oldStatuses = oldToNewMap.keySet().stream().toList();
        String historyQuery = String.format("SELECT id, status FROM %s WHERE status IN (%s)", DatabaseTable.Orders,
                String.join(", ", oldStatuses.stream().map(s -> String.format("'%s'", s)).toList()));
        Connection connection = ((JdbcConnection) (database.getConnection())).getWrappedConnection();
        try {
            ResultSet result = connection.createStatement().executeQuery(historyQuery);

            // Store the historical values in the history table
            while(result.next()) {
                int orderId = result.getInt("id");
                String oldOrderStatus = result.getNString("status");

                InsertStatement insertStatement = new InsertStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, DatabaseTable.Historical);
                HistoricOrder historicOrder = new HistoricOrder(orderId, oldOrderStatus);

                insertStatement.addColumnValue("tableName", DatabaseTable.Orders);
                insertStatement.addColumnValue("migrationClass", M001_FixOrderStatus.class.getName());

                try {
                    insertStatement.addColumnValue("data", writer.writeValueAsString(historicOrder));
                } catch (JsonProcessingException e) {
                    // Just move on...
                }

                statements.add(insertStatement);
            }

            rowsEffected = statements.size();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        oldToNewMap.keySet().forEach(key -> {
            UpdateStatement updateStatement = new UpdateStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, DatabaseTable.Orders);
            updateStatement.addNewColumnValue("status", oldToNewMap.get(key));
            updateStatement.setWhereClause(String.format("status = '%s'", key));

            statements.add(updateStatement);
        });

        try {
            database.execute(statements.toArray(new SqlStatement[0]), null);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getConfirmationMessage()
    {
        return String.format("%s - Updated %d records", M001_FixOrderStatus.class.getName(), rowsEffected);
    }

    public void setUp() throws SetupException {

    }

    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    public ValidationErrors validate(Database database) {
        return null;
    }

    public void rollback(Database database) throws CustomChangeException, RollbackImpossibleException {
        Map<String, String> newToOldMap = new HashMap<>();
        oldToNewMap.forEach((key, val) -> newToOldMap.put(val, key));

        // TODO: Grab data from historical table and reset orders data by id
    }

    @AllArgsConstructor
    private class HistoricOrder {
        public int id;
        public String status;
    }
}
