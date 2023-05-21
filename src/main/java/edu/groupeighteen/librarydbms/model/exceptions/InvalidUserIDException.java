package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom InvalidUserIDException class. Used to make Exceptions clearer.
 */
public class InvalidUserIDException extends Exception {
    public InvalidUserIDException(String message) {
        super(message);
    }
}