package edu.groupeighteen.librarydbms;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;

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
        //System.out.println(UserHandler.login("uname", "pword"));
        //User newUser = UserHandler.createNewUser("jesper", "pass");
        UserHandler.printUsernames();

        //End program
        exit(0);
    }

    /**
     * Calls the setup methods in all other classes (Handlers) that contain one.
     */
    public static void setup() {
        try {
            DatabaseHandler.setup(false);
            UserHandler.setup(DatabaseHandler.getConnection());
            ItemHandler.setup(DatabaseHandler.getConnection());
            //RentalHandler.setup
            //EveryOtherHandler.setup
        } catch (SQLException | ClassNotFoundException e) { //TODO-exceptions handle
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