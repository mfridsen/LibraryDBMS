package edu.groupeighteen.librarydbms.model.exceptions.rental;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * Custom RentalDeleteException class. Used to make Exceptions clearer.
 */
public class RentalDeleteException extends Exception {
    public RentalDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalDeleteException(String message) {
        super(message);
    }
}