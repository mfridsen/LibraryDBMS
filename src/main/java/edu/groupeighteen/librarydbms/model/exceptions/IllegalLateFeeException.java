package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom IllegalLateFeeException class. Used to make Exceptions clearer.
 */
public class IllegalLateFeeException extends Exception {
    public IllegalLateFeeException(String message) {
        super(message);
    }
}