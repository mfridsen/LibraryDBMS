package edu.groupeighteen.librarydbms.model.exceptions.user;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.user
 * @contact matfir-1@student.ltu.se
 * @date 6/2/2023
 * <p>
 * Custom UserValidationException class. Used to make Exceptions clearer.
 */
public class UserValidationException extends Exception
{
    public UserValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UserValidationException(String message)
    {
        super(message);
    }
}