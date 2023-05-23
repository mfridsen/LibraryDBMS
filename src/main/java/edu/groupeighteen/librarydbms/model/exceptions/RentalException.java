package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * Custom RentalException class. Used to make Exceptions clearer.
 */
public class RentalException extends Exception {
    public RentalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalException(String message) {
        super(message);
    }
}