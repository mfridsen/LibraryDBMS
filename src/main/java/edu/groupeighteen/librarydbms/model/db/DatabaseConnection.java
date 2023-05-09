package edu.groupeighteen.librarydbms.model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class handles setting up, maintaining and closing of a JDBC Connection.
 */
public class DatabaseConnection {
    private static Connection connection = null;

    /**
     * Standard connection method for a default-configured local MySQL Server. Connects to the server
     * using the connectToDatabaseServer method. Connects with the following parameters:
     * user: root
     * password: password
     * url: jdbc:mysql://localhost:3306
     * server: localhost
     * port: 3306
     */
    public static Connection connectToLocalSQLServer() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306";
        String user = "root";
        String password = "password";
        return connectToDatabaseServer(url, user, password);
    }

    /**
     * This method connects the Java application to a specific database. It loads the JDBC driver
     * and then establishes a connection to the database using the provided url, user, and password parameters.
     * If there are any errors connecting to the database, the method will throw a ClassNotFoundException
     * or SQLException.
     * @param url the URL of the database to connect to
     * @param user the username to use when connecting to the database
     * @param password the password to use when connecting to the database
     * @return a Connection if successful
     */
    public static Connection connectToDatabaseServer(String url, String user, String password)
            throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        System.out.println("\nLoading JDBC driver...");
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Loaded JDBC driver.");

        // Establish a connection
        System.out.println("Connecting to: " + user + "@" + url);
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to: " + user + "@" + url);
        return connection;
    }

    /**
     * Closes the connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getters and setters are self-explanatory.
     */
    public static Connection getConnection() {
        return connection;
    }
}