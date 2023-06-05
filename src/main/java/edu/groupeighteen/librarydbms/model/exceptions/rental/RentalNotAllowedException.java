package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * Custom RentalNotAllowedException class. Used to make Exceptions clearer.
 * <p>
 * In the context of this library management system, a RentalNotAllowedException could be useful in several scenarios. For instance:
 * <p>
 * If a user attempts to rent an item that's already rented out.
 * If a user attempts to rent more items than allowed by the system.
 * If a user tries to rent an item but they have unpaid late fees or other issues.
 */
public class RentalNotAllowedException extends Exception
{
    public RentalNotAllowedException(String message)
    {
        super(message);
    }
}