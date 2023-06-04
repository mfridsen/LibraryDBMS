package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 6/2/2023
 * <p>
 * Custom InvalidInputException class. Used to make Exceptions clearer.
 */
public class InvalidInputException extends Exception
{
    public InvalidInputException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidInputException(String message)
    {
        super(message);
    }
}