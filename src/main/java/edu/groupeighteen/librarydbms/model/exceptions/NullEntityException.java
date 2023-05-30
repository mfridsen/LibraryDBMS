package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/29/2023
 * <p>
 * Custom NullEntityException class. Used to make Exceptions clearer.
 */
public class NullEntityException extends Exception
{
    public NullEntityException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NullEntityException(String message)
    {
        super(message);
    }
}