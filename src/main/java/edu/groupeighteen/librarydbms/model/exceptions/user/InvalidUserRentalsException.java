package edu.groupeighteen.librarydbms.model.exceptions.user;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.user
 * @contact matfir-1@student.ltu.se
 * @date 6/2/2023
 * <p>
 * Custom InvalidUserRentalsException class. Used to make Exceptions clearer.
 */
public class InvalidUserRentalsException extends Exception
{
    public InvalidUserRentalsException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidUserRentalsException(String message)
    {
        super(message);
    }
}