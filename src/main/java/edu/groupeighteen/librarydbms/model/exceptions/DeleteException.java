package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 6/1/2023
 * <p>
 * Custom DeleteException class. Used to make Exceptions clearer.
 */
public class DeleteException extends Exception
{
    public DeleteException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DeleteException(String message)
    {
        super(message);
    }
}