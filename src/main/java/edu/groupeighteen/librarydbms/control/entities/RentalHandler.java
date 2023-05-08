package edu.groupeighteen.librarydbms.control.entities;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 */
public class RentalHandler {
    //The code is cleaner if every Handler class stores a reference to the Connection
    private static Connection connection;

    //TODO-exception handle
    /**
     * To ensure that things are done in the correct order, only DatabaseHandler will retrieve its connection
     * on its own. The rest of the Handlers need to be assigned the connection, by calling their setup methods
     * with the connection as argument after the DatabaseHandlers setup method has been called.
     * @param con the Connection to the database.
     * @throws SQLException
     */
    public static void setup(Connection con) throws SQLException {
        connection = con;
    }

}