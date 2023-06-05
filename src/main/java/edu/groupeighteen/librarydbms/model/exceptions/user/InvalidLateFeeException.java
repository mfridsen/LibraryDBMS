package edu.groupeighteen.librarydbms.model.exceptions.user;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom InvalidLateFeeException class. Used to make Exceptions clearer.
 */
public class InvalidLateFeeException extends Exception
{
    public InvalidLateFeeException(String message)
    {
        super(message);
    }
}