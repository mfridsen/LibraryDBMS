package edu.groupeighteen.librarydbms.control.db;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.db.SQLFormatter;

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
 *
 * This class is only responsible for general operations. Specific operations are delegated to specific Handler classes,
 * such as {@link UserHandler}.   //TODO-comment finish
 */
public class DatabaseHandler {
    //The DatabaseHandler needs a connection to perform commands and queries.
    private static Connection connection;
    //Print commands being run, default = not
    private static boolean verbose = false;

    /**
     * Sets up the DatabaseConnection, then checks if the database exists. If not, calls createDatabase to
     * create it.
     */
    public static void setup(boolean verbose) {
        try {
            //Set verbosity
            DatabaseHandler.verbose = verbose;

            //Connect to database
            connection = DatabaseConnection.setup();

            executeCommand("drop database if exists " + LibraryManager.databaseName);
            createDatabase(LibraryManager.databaseName);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionHandler.HandleFatalException("Failed to setup databse due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        /*
        if (!databaseExists(LibraryManager.databaseName)) {
            createDatabase(LibraryManager.databaseName);
        } else executeCommand("use " + LibraryManager.databaseName);
        */

    }

    /**
     * Checks whether a given database already exists on the server.
     * @param databaseName the name of the database in question.
     * @return true if the database exists, otherwise false.
     */
    public static boolean databaseExists(String databaseName) {
        String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        String[] params = {databaseName.toLowerCase()};

        try {
            QueryResult queryResult = executePreparedQuery(query, params);
            ResultSet resultSet = queryResult.getResultSet();
            boolean exists = resultSet.next();
            queryResult.close();
            return exists;
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to verify that database exists due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Creates a new database with a given name and fills it with tables and data.
     * @param databaseName the name of the database.
     */
    public static void createDatabase(String databaseName) {
        //Create DB
        executeCommand("create database " + databaseName);
        //Use DB
        executeCommand("use " + LibraryManager.databaseName);
        //Fill DB with tables and data
        executeSQLCommandsFromFile("src/main/resources/sql/create_tables.sql");
        executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
    }

    /**
     * Executes a single SQL command on the connected database. SQL commands can affect rows and therefore
     * are used with executeUpdate. Prints number of rows affected if command was successfully executed.
     *
     * @param command the SQL command to execute.
     */
    public static void executeCommand(String command) {
        if (verbose) {
            System.out.println("\nExecuting command:");
            SQLFormatter.printFormattedSQL(command);
        }

        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(command);
            System.out.println("Command executed; rows affected: " + rows);
            statement.close(); //Always close Statements after we're done with them
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to execute command due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //TODO-TEST
    /**
     * Execute an SQL update statement using a prepared statement.
     *
     * @param command The SQL statement to execute.
     * @param parameters An array of values to be bound to the SQL statement.
     * @return The number of rows affected by the update.
     */
    public static int executePreparedUpdate(String command, String[] parameters) {
        if (verbose) {
            System.out.println("\nExecuting command:");
            SQLFormatter.printFormattedSQL(command);
        }

        try (PreparedStatement stmt = connection.prepareStatement(command)) {

            //Bind the provided parameters to the SQL statement
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    stmt.setString(i + 1, parameters[i]);
                }
            }

            //Execute the update and return the number of affected rows
            return stmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to execute prepared update due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Won't reach, but needed to compile
        return -1;
    }

    /**
     * Executes a single SQL query on the connected database. SQL queries, unlike SQL commands, do not affect rows,
     * but do instead produce ResultSets which are sent up the call stack in a QueryResult.
     *
     * @param query the SQL query to execute.
     * @return a QueryResult.
     */
    public static QueryResult executeQuery(String query) {
        if (verbose) {
            System.out.println("\nExecuting query:");
            SQLFormatter.printFormattedSQL(query);
        }

        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to execute query due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        return new QueryResult(resultSet, statement);
    }

    //TODO-comment
    /**
     *
     * @param query
     * @param params
     * @return
     */
    public static QueryResult executePreparedQuery(String query, String[] params, int... settings) {
        if (verbose) {
            System.out.println("\nExecuting prepared query:");
            SQLFormatter.printFormattedSQL(query);
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //Prepare the statement with the given settings
            preparedStatement = connection.prepareStatement(query, settings);
            //Set the parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }
            //Execute the query
            preparedStatement.execute();
            //Get the result set, if available
            resultSet = preparedStatement.getResultSet();
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to execute prepared query due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        return new QueryResult(resultSet, preparedStatement);
    }

    /**
     * Executes a SQL update operation (such as UPDATE, INSERT, or DELETE) on the database, using a prepared statement
     * with the given SQL command and parameters.
     *
     * @param sql The SQL command to be executed. This command should be a SQL UPDATE, INSERT, or DELETE command,
     *            and can include placeholders (?) for parameters.
     * @param params The parameters to be used in the SQL command. The parameters will be inserted into the command
     *               in the order they appear in the array.
     * @return The number of rows affected by the update.
     */
    public static int executeUpdate(String sql, String[] params) {
        if (verbose) System.out.println("\nExecuting update: " + sql);

        int rowsAffected = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }

            //The method executeUpdate() returns the number of affected rows.
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to execute update due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return rowsAffected;
    }

    /**
     * A simple method which reads the contents of a file, and executes any SQL commands found in that file.
     * @param filePath the path of the file
     */
    public static void executeSQLCommandsFromFile(String filePath) {
        //No point attempting to execute from an empty file path
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("ERROR: executeSqlCommandsFromFile: No filepath.");
            return;
        }

        if (verbose) System.out.println("\nExecuting commands from file: " + filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder commandBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                //Skip comments
                if (line.startsWith("--")) {
                    continue;
                }

                //Append the line to the command string, adding a space in between
                commandBuilder.append(line).append(" ");

                //Check if the line ends with a semicolon, signifying the end of the command
                if (line.endsWith(";")) {
                    String command = commandBuilder.toString();
                    executeCommand(command);
                    //Reset the command builder for the next command
                    commandBuilder = new StringBuilder();
                }
            }
        } catch (FileNotFoundException e) {
            ExceptionHandler.HandleFatalException("Couldn't find file at path " + filePath, e);
        } catch (IOException e) {
            ExceptionHandler.HandleFatalException("Couldn't read file at path " + filePath, e);
        }
    }

    //TODO-test
    //TODO-comment
    public static int[] getUserMetaData() {
        if (connection == null) {
            setup(false);
        }

        try {
            DatabaseMetaData metaData = connection.getMetaData();

            // Get metadata for username column
            ResultSet resultSet = metaData.getColumns(null, null, "Users", "username");
            int usernameColumnSize = 0;
            if (resultSet.next()) {
                usernameColumnSize = resultSet.getInt("COLUMN_SIZE");
            }

            // Get metadata for password column
            resultSet = metaData.getColumns(null, null, "Users", "password");
            int passwordColumnSize = 0;
            if (resultSet.next()) {
                passwordColumnSize = resultSet.getInt("COLUMN_SIZE");
            }

            return new int[]{usernameColumnSize, passwordColumnSize};
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Couldn't retrieve User Meta data due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Won't reach, but needed to compile
        return new int[0];
    }

    /**
     * Since any other classes should only do Database-related things through this Handler class,
     * we need to add a close method that calls closeConnection in the DatabaseConnection class.
     */
    public static void closeDatabaseConnection() {
        DatabaseConnection.closeConnection();
    }

    public static void setConnection(Connection connection) {
        DatabaseHandler.connection = connection;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean verbose) {
        DatabaseHandler.verbose = verbose;
    }
}