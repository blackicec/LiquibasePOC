package controllers;

import com.mysql.cj.MysqlConnection;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;

public class MigrationController {
    public void Test() {
        Database db = new MySQLDatabase();
        //MysqlConnection connection = new
        //db.setConnection();
        DatabaseChangeLog changeLog =  new DatabaseChangeLog();
        ChangeSet changeSet = new ChangeSet(changeLog);

        changeLog.addChangeSet(changeSet);

        //Liquibase liquibase = new Liquibase(changeLog, null, );
    }
}
