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

    public static void main(String[] args) {
        try {
            //TODO CONNECTION SHOULD PROBABLY ONLY BE OPENED EITHER AT LOGIN, OR AT SEARCH QUERIES, AND CLOSED
            // AFTER A CERTAIN AMOUNT OF TIME
            DatabaseHandler.setupDatabase();

            // TODO user login handling code
            /*
            User user = UserHandler.login(username, password);
            if (user != null) {
                if (user.isAdmin()) {
                    // Show the admin GUI
                    //TODO CONNECTION SHOULD PROBABLY ONLY BE ESTABLISHED HERE, AND CLOSED WHEN WE LOG OUT
                } else {
                    // Show the user GUI
                    //TODO CONNECTION SHOULD PROBABLY ONLY BE ESTABLISHED HERE, AND CLOSED WHEN WE LOG OUT
                }
            } else {
                // Show an error message
            }
            */
            // TODO end todo

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

        //TODO dump all lines printed by the app into a file, and then compare to an expected file
        /*
        String fileName = "output.txt";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);
            System.out.println("Hello, world!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         */
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