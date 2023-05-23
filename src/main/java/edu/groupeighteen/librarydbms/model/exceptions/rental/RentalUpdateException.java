package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * Custom RentalUpdateException class. Used to make Exceptions clearer.
 */
public class RentalUpdateException extends Exception {
    public RentalUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalUpdateException(String message) {
        super(message);
    }
}