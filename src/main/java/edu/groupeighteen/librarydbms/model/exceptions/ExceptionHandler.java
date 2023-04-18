package edu.groupeighteen.librarydbms.model.exceptions;

import edu.groupeighteen.librarydbms.LibraryManager;

import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 4/18/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 */
public class ExceptionHandler {
    /**
     * Calls the other Exception handling methods depending on what type of Exception e is.
     * If e is an Exception that doesn't have a corresponding handle method, this method calls
     * handleUndefinedException which will print the error message and stack trace and exit the program,
     * since an Exception with unspecified handling method is dangerous for the program.
     * @param e the Exception.
     */
    public static void handleException(Exception e) {
        //Standard Exceptions
        if (e instanceof IllegalArgumentException) {
            handleIllegalArgumentException((IllegalArgumentException) e);
        }

        else if (e instanceof SQLException) {
            handleSQLException((SQLException) e);
        }

        else if (e instanceof ClassNotFoundException) {
            handleClassNotFoundException((ClassNotFoundException) e);
        }

        //Created Exceptions


        //Unspecified Exception
        else {
            handleUndefinedException(ErrorMessages.UNHANDLED_EXCEPTION, e);
        }
    }

    // --------------- STANDARD EXCEPTIONS ---------------

    /**
     * Handles IllegalArgumentExceptions.
     * @param e the IllegalArgumentException.
     */
    static void handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println(ErrorMessages.ILLEGAL_ARGUMENT_EXCEPTION);

        if (false) {
            //TODO handle other IllegalArgumentExceptions here
        }

        //Unspecified IllegalArgumentException
        else {
            handleUndefinedException(ErrorMessages.UNHANDLED_ILLEGAL_ARGUMENT_EXCEPTION, e);
        }
    }

    /**
     * Handles SQLException thrown by DatabaseConnection when trying to connect to database.
     * @param e the SQLException.
     */
    static void handleSQLException(SQLException e) {
        System.err.println(ErrorMessages.SQL_EXCEPTION);

        //Com link fail = probably wrong URL
        if (e.getMessage().contains("Communications link failure")) {
            System.err.println(ErrorMessages.COM_LINK_ERROR);

            //Access denied = invalid username, password or both
        } else if (e.getMessage().contains("Access denied for user")) {
            System.err.println(ErrorMessages.ACCESS_DENIED_ERROR);

            //Unspecified SQLException
        } else {
            handleUndefinedException(ErrorMessages.UNHANDLED_SQL_EXCEPTION, e);
        }
    }

    /**
     * Handles ClassNotFoundException thrown by DatabaseConnection when trying to connect to database.
     * @param e the ClassNotFoundException.
     */
    static void handleClassNotFoundException(ClassNotFoundException e) {
        System.err.println(ErrorMessages.CLASS_NOT_FOUND_EXCEPTION);

        //JBDC Driver not found
        if (e.getMessage().contains("jbdc")) {
            handleFatalException(ErrorMessages.JDBC_ERROR, e);
        }

        //Unspecified ClassNotFoundException
        else {
            handleUndefinedException(ErrorMessages.UNHANDLED_CLASS_NOT_FOUND_EXCEPTION, e);
        }
    }

    // --------------- CREATED EXCEPTIONS ---------------



    // --------------- UNDEFINED/FATAL EXCEPTIONS ---------------

    /**
     * Handles any Exception that hasn't been given its own handle method yet.
     * @param e the Exception.
     */
    static void handleUndefinedException(String message, Exception e) {
        handleFatalException(message, e);
    }

    /**
     * Prints the error message and stack trace of a caught Exception. Should only be called if an un-handled,
     * or handled but fatal, exception has been thrown.
     * If this is called, program needs to shut down.
     * @param e the Exception.
     */
    static void handleFatalException(String message, Exception e) {
        System.err.println(message);
        System.err.println("\nERROR MESSAGE: ");
        System.err.println(e.getMessage());
        System.err.println("\nSTACK TRACE: ");
        e.printStackTrace();
        LibraryManager.exit(1);
    }
}