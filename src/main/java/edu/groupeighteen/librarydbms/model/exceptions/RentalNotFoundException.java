package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * Custom RentalNotFoundException class. Used to make Exceptions clearer.
 */
public class RentalNotFoundException extends Exception {
    public RentalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalNotFoundException(String message) {
        super(message);
    }
}