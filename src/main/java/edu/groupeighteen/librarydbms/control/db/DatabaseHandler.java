package edu.groupeighteen.librarydbms.control.db;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class is responsible for handling a database by performing SQL Queries and SQL Commands on the database.
 * To make the code more readable it was decided to let this class retrieve and store a Connection from the
 * DatabaseConnection class.
 */
public class DatabaseHandler {

    private static Connection connection;

    //TODO handle exceptions
    //TODO Remember that when the program is complete, the DB shouldn't be created from scratch on start-up every time
    /**
     * Deletes demo_database and starts over from scratch, initializing all the tables and
     * then filling them with test data.
     */
    public static void setupDatabase() throws SQLException, ClassNotFoundException {
        // Connect to database
        connection = DatabaseConnection.connectToLocalSQLServer();
        //Delete DB if already exists
        executeSingleSQLCommand("drop database if exists " + LibraryManager.databaseName + ";");
        //Create DB
        executeSingleSQLCommand("create database " + LibraryManager.databaseName + ";");
        //Show DBs in server
        executeSingleSQLQuery("show databases;");
        //Use DB
        executeSingleSQLCommand("use " + LibraryManager.databaseName + ";");
        executeSingleSQLQuery("show tables;");
        //Create tables
        executeSQLCommandsFromFile("src/main/resources/sql/create_tables.sql");
        //Show tables in DB
        executeSingleSQLQuery("show tables;");
        //Load test data
        executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        executeSingleSQLQuery("select * from user");
        executeSingleSQLQuery("select * from item;");
    }

    /**
     * Since any other classes should only do Database-related things through this Handler class,
     * we need to add a close method that calls closeConnection in the DatabaseConnection class.
     */
    public static void closeDatabase() {
        DatabaseConnection.closeConnection();
    }

    //TODO handle exceptions
    /**
     * Executes a single SQL command on the connected database. SQL commands can affect rows and therefore
     * are used with executeUpdate.
     *
     * Prints number of rows affected if command was successfully executed.
     * @param command the SQL command to execute
     * @throws SQLException if an error occurs while executing the command
     */
    public static void executeSingleSQLCommand(String command) throws SQLException {
        //Setup
        System.out.println("\nExecuting command:");
        SQLFormatter.printFormattedSQL(command);
        Statement statement = connection.createStatement();
        //Execute
        int rows = statement.executeUpdate(command);
        System.out.println("Command executed; rows affected: " + rows);
        statement.close(); //Always close Statements after we're done with them
    }

    //TODO handle exceptions
    /**
     * Executes a single SQL query on the connected database and prints the results to the console if successful.
     * SQL queries, unlike SQL commands, do not affect rows.
     *
     * @param query the SQL query to execute
     * @throws SQLException if an error occurs while executing the query
     */
    public static void executeSingleSQLQuery(String query) throws SQLException {
        //Execute query, retrieve resultset and extract metadata
        System.out.println("\nExecuting query: " + query);
        Statement statement = connection.createStatement();
        //Execution
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Iterate through the resultset and print the metadata
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
            }
            System.out.println();
        }

        //Close everything
        resultSet.close(); //Always close ResultSets after we're done with them
        statement.close(); //Always close Statements after we're done with them
    }

    //TODO Exception handling, pass on upwards and also add handling of these specific
    // exceptions in the ExceptionHandler
    /**
     * A simple method which reads the contents of a file, and executes any SQL commands found in that file.
     * @param filePath the path of the file
     */
    public static void executeSQLCommandsFromFile(String filePath) {
        if (filePath.length() == 0) {
            System.out.println("ERROR: executeSqlCommandsFromFile: No filepath.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder commandBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // Skip comments
                if (line.startsWith("--")) {
                    continue;
                }
                // Append the line to the command string, adding a space in between
                commandBuilder.append(line).append(" ");

                // Check if the line ends with a semicolon, signifying the end of the command
                if (line.endsWith(";")) {
                    String command = commandBuilder.toString();
                    executeSingleSQLCommand(command);
                    // Reset the command builder for the next command
                    commandBuilder = new StringBuilder();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't find file at path " + filePath);
            LibraryManager.exit(1);
        } catch (IOException e) {
            System.out.println("Couldn't read file at path " + filePath);
            e.printStackTrace();
            LibraryManager.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public static void setConnection(Connection connection) {
        DatabaseHandler.connection = connection;
    }

    public static Connection getConnection() {
        return connection;
    }
}