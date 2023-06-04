package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.rental
 * @contact matfir-1@student.ltu.se
 * @date 6/4/2023
 * <p>
 * Custom InvalidReceiptException class. Used to make Exceptions clearer.
 */
public class InvalidReceiptException extends Exception
{
    public InvalidReceiptException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidReceiptException(String message)
    {
        super(message);
    }
}