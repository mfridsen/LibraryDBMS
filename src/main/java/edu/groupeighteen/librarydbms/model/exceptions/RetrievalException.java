package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 6/1/2023
 * <p>
 * Custom RetrievalException class. Used to make Exceptions clearer.
 */
public class RetrievalException extends Exception
{
    public RetrievalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RetrievalException(String message)
    {
        super(message);
    }
}