package pms.database;

import java.sql.Connection;
import java.sql.SQLException;
import main.DatabaseConfig;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public void connectToDatabase() throws SQLException {

        String server = DatabaseConfig.addressFromFile;
        String userName = DatabaseConfig.usernameFromFile;
        String password = DatabaseConfig.passwordFromFile;
        String port = DatabaseConfig.portFromFile;
        String database = "pms";

        connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, userName, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
