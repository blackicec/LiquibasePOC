package app.migrations.custom;

import app.constants.DatabaseInfo;
import app.constants.DatabaseTable;
import app.data.models.OrdersDao;
import app.data.models.enums.OrderStatus;
import app.data.repositories.OrdersRepository;
import app.services.BeanGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.datatype.core.IntType;
import liquibase.exception.*;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;
import liquibase.statement.core.UpdateStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class M001_FixOrderStatus implements CustomTaskChange, CustomTaskRollback {
    private final OrdersRepository ordersRepository;

    public M001_FixOrderStatus() {
        this.ordersRepository = BeanGenerator.getBean(OrdersRepository.class);
    }

    private final HashMap<String, String> oldToNewMap = new HashMap<>() {
        {
            put(OrderStatus.Error.toString(), OrderStatus.Warning.toString());
            put(OrderStatus.Problematic.toString(), OrderStatus.Hopeless.toString());
        }
    };
    private int rowsEffected;

    public void execute(Database database) throws CustomChangeException {
        //statement.addColumn("id", new IntType());
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<SqlStatement> statements = new ArrayList<>();
        List<String> oldStatuses = oldToNewMap.keySet().stream().toList();
        List<OrdersDao> orders = ordersRepository.findAllWhereStatusIn(oldStatuses);
        rowsEffected = orders.size();

        oldToNewMap.keySet().forEach(key -> {
            UpdateStatement updateStatement = new UpdateStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, DatabaseTable.Orders);
            updateStatement.addNewColumnValue("status", oldToNewMap.get(key));
            updateStatement.setWhereClause(String.format("status = %s", key));

            statements.add(updateStatement);
        });

        // Store the historical values in the history table
        for (OrdersDao order : orders) {
            InsertStatement insertStatement = new InsertStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, DatabaseTable.Historicals);
            HistoricOrder historicOrder = new HistoricOrder(order.getId(), order.getStatus());

            insertStatement.addColumnValue("tableName", DatabaseTable.Orders);
            insertStatement.addColumnValue("migrationClass", M001_FixOrderStatus.class.getName());

            try {
                insertStatement.addColumnValue("data", writer.writeValueAsString(historicOrder));
            } catch (JsonProcessingException e) {
                // Just move on...
            }

            statements.add(insertStatement);
        }

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
    }

    @AllArgsConstructor
    private class HistoricOrder {
        public int Id;
        public OrderStatus status;
    }
}
