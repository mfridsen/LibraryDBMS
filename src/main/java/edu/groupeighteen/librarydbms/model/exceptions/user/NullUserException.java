package edu.groupeighteen.librarydbms.model.exceptions.user;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom NullUserException class. Used to make Exceptions clearer.
 */
public class NullUserException extends Exception {
    public NullUserException(String message) {
        super(message);
    }
}