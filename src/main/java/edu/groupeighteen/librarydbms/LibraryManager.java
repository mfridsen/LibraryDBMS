package edu.groupeighteen.librarydbms;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.user.UserHandler;

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

    //TODO clean up exceptions
    public static void main(String[] args) throws SQLException {
        //Perform initialization work
        setup();

        //Do actual stuff
        UserHandler.printUsernames();
        System.out.println(UserHandler.login("uname", "pword"));

        //End program
        exit(0);
    }

    /**
     * Calls the setup methods in all other classes (Handlers) that contain one.
     */
    public static void setup() {
        try {
            DatabaseHandler.setup();
            UserHandler.setup(DatabaseHandler.getConnection());
            //TODO handle exceptions
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the program with status. If the connection to the database is still active, closes it.
     */
    public static void exit(int status) {
        if (DatabaseHandler.getConnection() != null) { //Always close the connection to the database after use
            DatabaseHandler.closeDatabaseConnection();
        }
        System.exit(status);
    }
}