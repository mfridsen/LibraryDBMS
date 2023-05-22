package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/22/2023
 * <p>
 * Custom NullRentalException class. Used to make Exceptions clearer.
 */
public class NullRentalException extends Exception {
    public NullRentalException(String message) {
        super(message);
    }
}