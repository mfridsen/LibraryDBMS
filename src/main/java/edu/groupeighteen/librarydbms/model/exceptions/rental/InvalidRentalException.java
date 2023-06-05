package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom InvalidRentalException class. Used to make Exceptions clearer.
 */
public class InvalidRentalException extends Exception
{
    public InvalidRentalException(String message)
    {
        super(message);
    }
}