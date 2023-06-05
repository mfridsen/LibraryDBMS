package edu.groupeighteen.librarydbms.model.exceptions.user;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.user
 * @contact matfir-1@student.ltu.se
 * @date 5/24/2023
 * <p>
 * Custom InvalidRentalStatusChangeException class. Used to make Exceptions clearer.
 */
public class InvalidRentalStatusChangeException extends Exception
{
    public InvalidRentalStatusChangeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidRentalStatusChangeException(String message)
    {
        super(message);
    }
}