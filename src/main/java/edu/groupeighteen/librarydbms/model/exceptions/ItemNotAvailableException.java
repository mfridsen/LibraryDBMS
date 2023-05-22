package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/22/2023
 * <p>
 * Custom ItemNotAvailableException class. Used to make Exceptions clearer.
 */
public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}