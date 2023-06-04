package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 6/1/2023
 * <p>
 * Custom RecoveryException class. Used to make Exceptions clearer.
 */
public class RecoveryException extends Exception
{
    public RecoveryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RecoveryException(String message)
    {
        super(message);
    }
}