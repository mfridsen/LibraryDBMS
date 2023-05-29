package edu.groupeighteen.librarydbms.model.exceptions.item;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.item
 * @contact matfir-1@student.ltu.se
 * @date 5/29/2023
 * <p>
 * Custom InvalidBarcodeException class. Used to make Exceptions clearer.
 */
public class InvalidBarcodeException extends Exception
{
    public InvalidBarcodeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidBarcodeException(String message)
    {
        super(message);
    }
}