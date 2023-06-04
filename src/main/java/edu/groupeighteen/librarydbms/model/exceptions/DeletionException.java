package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 6/1/2023
 * <p>
 * Custom DeletionException class. Used to make Exceptions clearer.
 */
public class DeletionException extends Exception
{
    public DeletionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DeletionException(String message)
    {
        super(message);
    }
}