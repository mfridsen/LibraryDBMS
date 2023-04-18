package edu.groupeighteen.librarydbms.model.exceptions;

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
 *
 * This class contains 'global' error message Strings, in order to make the application code cleaner,
 * and to adhere to the Don't Repeat Yourself principle.
 */
public class ErrorMessages {
    //Invalid input error messages //TODO fix
    public static final String INVALID_USERNAME = "Invalid username: ";
    public static final String INVALID_PASSWORD = "Invalid password: ";
    public static final String INVALID_EMAIL = "Invalid email: ";


    //Generic Exception error message
    public static final String UNHANDLED_EXCEPTION = "\nERROR: AN UNHANDLED EXCEPTION OCCURRED!";

    //IllegalArgumentException
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "\nERROR: An IllegalArgumentException occurred:";

    public static final String UNHANDLED_ILLEGAL_ARGUMENT_EXCEPTION = "ERROR: AN UNHANDLED ILLEGALARGUMENTEXCEPTION OCCURRED!";

    //SQLException
    public static final String SQL_EXCEPTION = "ERROR: An SQLException occurred:";

    public static final String COM_LINK_ERROR = "An error occurred while connecting to the database: " +
            "Communications link failure. Either the URL was incorrect or there is no server to connect to.\n";

    public static final String ACCESS_DENIED_ERROR = "An error occurred while connecting to the database: " +
            "Access denied for user: either username or password is invalid.\n";

    public static final String UNHANDLED_SQL_EXCEPTION = "ERROR: AN UNHANDLED SQLEXCEPTION OCCURRED!";

    //ClassNotFoundException
    public static final String CLASS_NOT_FOUND_EXCEPTION = "ERROR: A ClassNotFoundException occurred:";

    public static final String JDBC_ERROR = """
                    The JDBC driver could not be found:
                    Could occur if the JDBC driver is not present in the classpath,
                    or if the name of the JDBC driver class specified in the code is incorrect.""";

    public static final String UNHANDLED_CLASS_NOT_FOUND_EXCEPTION = "ERROR: AN UNHANDLED CLASSNOTFOUNDEXCEPTION OCCURRED!";

    //InvalidEmailException
    public static final String INVALID_EMAIL_ERROR = "To set your email, please enter a valid email address in the format 'username@domain.com'.\n " +
            "The email address should contain only letters, numbers, and the following special characters: ! # $ % & ' * + - / = ? ^ _ ` { | } ~.\n " +
            "The domain name should be proceeded by an '@' and have at least one period (e.g. .com, .edu, .gov, .student.ltu.se).";
}