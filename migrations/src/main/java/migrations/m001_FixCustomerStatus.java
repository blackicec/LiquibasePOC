package migrations;

import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.exception.*;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.visitor.AppendSqlVisitor;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.CopyRowsStatement;
import liquibase.statement.core.CreateTableStatement;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class m001_FixCustomerStatus implements CustomTaskChange, CustomTaskRollback {
    public void execute(Database database) throws CustomChangeException {
        //SqlStatement savehistoricalData = new CopyRowsStatement()
        SqlStatement statement = new CreateTableStatement("historical_statuses", "schema", "new_table");
        //SqlVisitor visitor = new AppendSqlVisitor();
        try {
            database.execute(new SqlStatement[] {statement}, null);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getConfirmationMessage() {
        return null;
    }

    public void setUp() throws SetupException {

    }

    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    public ValidationErrors validate(Database database) {
        return null;
    }

    public void rollback(Database database) throws CustomChangeException, RollbackImpossibleException {

    }
}
