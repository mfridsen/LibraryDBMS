package edu.groupeighteen.librarydbms.control.exceptions;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/20/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class ExceptionHandler {
    public static void HandleSQLException(SQLException sqle) { {
            // Log the error
            Logger logger = Logger.getLogger("DatabaseErrorLogger");
            logger.log(Level.SEVERE, "Fatal database error occurred", sqle);
            System.out.println("A fatal error occurred. Please check the log file for more details.");

            // Exit the program
            System.exit(1);
        }
    }
}