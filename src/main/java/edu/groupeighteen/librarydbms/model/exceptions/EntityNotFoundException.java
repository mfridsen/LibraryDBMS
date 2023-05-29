package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/29/2023
 * <p>
 * Custom EntityNotFoundException class. Used to make Exceptions clearer.
 */
public class EntityNotFoundException extends Exception
{
    public EntityNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EntityNotFoundException(String message)
    {
        super(message);
    }
}