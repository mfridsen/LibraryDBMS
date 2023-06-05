package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/22/2023
 * <p>
 * Custom ConstructionException class. Used to make Exceptions clearer.
 */
public class ConstructionException extends Exception
{
    public ConstructionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}