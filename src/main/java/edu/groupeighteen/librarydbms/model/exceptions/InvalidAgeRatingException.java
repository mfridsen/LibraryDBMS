package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/30/2023
 * <p>
 * Custom InvalidAgeRatingException class. Used to make Exceptions clearer.
 */
public class InvalidAgeRatingException extends Exception
{
    public InvalidAgeRatingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidAgeRatingException(String message)
    {
        super(message);
    }
}