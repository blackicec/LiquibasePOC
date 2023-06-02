package app.migrations.seed;

import app.constants.DatabaseInfo;
import app.constants.DatabaseTable;
import com.github.javafaker.Faker;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class S001_AddInitialSeedData implements CustomTaskChange {
    private String ordersHydrationCount;
    private String value;

    @Override
    public void execute(Database database) throws CustomChangeException {
        final List<String> statuses = List.of("Error", "Problematic", "Success", "Processing", "Pending");
        List<SqlStatement> statements = new ArrayList<>();
        Random rand = new Random();
        Faker faker = new Faker();

        // Note: value only populates if the get method is called
        String registrationCount = this.getOrdersHydrationCount();

        for(int i = 0; i < Integer.parseInt(registrationCount); i++) {
            InsertStatement statement = new InsertStatement(DatabaseInfo.CatalogName, DatabaseInfo.Schema, DatabaseTable.Orders);

            statement.addColumnValue("firstName", faker.name().firstName());
            statement.addColumnValue("lastName", faker.name().lastName());
            statement.addColumnValue("email", faker.internet().emailAddress());
            statement.addColumnValue("status", statuses.get(rand.nextInt(statuses.size())));

            statements.add(statement);
        }

        try {
            database.execute(statements.toArray(new SqlStatement[0]), null);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

    // Custom properties
    public void setOrdersHydrationCount(String ordersHydrationCount) {
        this.ordersHydrationCount = ordersHydrationCount;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOrdersHydrationCount() {
        return this.ordersHydrationCount;
    }

    public String getValue() {
        return this.value;
    }
}

