package edu.groupeighteen.librarydbms.model.exceptions.item;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.item
 * @contact matfir-1@student.ltu.se
 * @date 5/30/2023
 * <p>
 * Custom InvalidISBNException class. Used to make Exceptions clearer.
 */
public class InvalidISBNException extends Exception
{
    public InvalidISBNException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidISBNException(String message)
    {
        super(message);
    }
}