package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-28
 */
public class InvalidEmailException extends Exception
{
    public InvalidEmailException(String message)
    {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
