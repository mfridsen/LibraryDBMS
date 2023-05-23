package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * Custom RentalRecoveryException class. Used to make Exceptions clearer.
 */
public class RentalRecoveryException extends Exception {
    public RentalRecoveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalRecoveryException(String message) {
        super(message);
    }
}