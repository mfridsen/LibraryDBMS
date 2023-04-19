package edu.groupeighteen.librarydbms;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.exceptions.ExceptionHandler;

import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 */
public class LibraryManager {

    public static final String databaseName = "lilla_biblioteket";

    public static void main(String[] args) {
        try {
            DatabaseHandler.setupDatabase();

            //Always close the connection to the database after use
            DatabaseHandler.closeDatabase();
            exit(0);

            //EXCEPTION HANDLING; SQLException
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionHandler.handleException(e);
            //EXCEPTION HANDLING; ClassNotFoundException
        } finally {
            // Always close the connection to the database after use
            DatabaseHandler.closeDatabase();
        }
    }

    /**
     * Exits the program with status.
     */
    public static void exit(int status) {
        if (DatabaseConnection.getConnection() != null) { //Always close the connection to the database after use
            DatabaseConnection.closeConnection();
        }
        System.exit(status);
    }
}