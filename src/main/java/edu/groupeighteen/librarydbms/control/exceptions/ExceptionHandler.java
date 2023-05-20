package edu.groupeighteen.librarydbms.control.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static void HandleFatalException(Exception e) { {
            // Log the error
            Logger logger = Logger.getLogger("DatabaseErrorLogger");
            if (e instanceof SQLException) {
                logger.log(Level.SEVERE, "Fatal database error occurred", e);
                System.err.println(e.getMessage()); //TODO-future remove later
                e.printStackTrace();
            }
            if (e instanceof FileNotFoundException) {
                logger.log(Level.SEVERE, "Fata file not found error occurred", e);
                System.err.println(e.getMessage()); //TODO-future remove later
                e.printStackTrace();
            }
            if (e instanceof IOException) {
                logger.log(Level.SEVERE, "Fatal IOException occurred", e);
                System.err.println(e.getMessage()); //TODO-future remove later
                e.printStackTrace();
            }

            // Exit the program
            System.err.println("A fatal error occurred. Please check the log file for more details.");
            System.exit(1);
        }
    }
}

//TODO-future  consider expanding the log messages to include more context-specific information. For instance,
// for SQLExceptions, you could include details about the database operation that failed.
// For FileNotFoundException, the file path would be useful. Including as much contextual information as possible
// in your log messages can help speed up the debugging process.

//TODO-future In the future, you might also want to consider adding more types of exceptions to the list, or even
// have a general catch-all for exceptions that you did not explicitly account for. This way, you ensure that any
// unanticipated exception is also logged and causes the application to fail fast, which is the behavior you want.