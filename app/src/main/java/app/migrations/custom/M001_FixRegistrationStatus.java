package app.migrations.custom;

import app.constants.DatabaseInfo;
import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.datatype.core.IntType;
import liquibase.exception.*;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.CreateTableStatement;

import java.util.HashMap;
import java.util.Map;

public class M001_FixRegistrationStatus implements CustomTaskChange, CustomTaskRollback {
    private final HashMap<String, String> oldToNewMap = new HashMap<>() {
        {
            put("Error", "Warning");
            put("Problematic", "Hopeless");
        }
    };

    public void execute(Database database) throws CustomChangeException {
        //SqlStatement saveHistoricalData = new CopyRowsStatement()
        CreateTableStatement statement = new CreateTableStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, "new_table");
        statement.addColumn("id", new IntType());

        try {
            database.execute(new SqlStatement[] {statement}, null);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getConfirmationMessage() {
        return "Congrats, you did a thing...";
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
}
