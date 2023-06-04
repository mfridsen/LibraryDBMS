package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 6/2/2023
 * <p>
 * Custom InvalidTypeException class. Used to make Exceptions clearer.
 */
public class InvalidTypeException extends Exception
{
    public InvalidTypeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidTypeException(String message)
    {
        super(message);
    }
}