package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom IllegalCurrentRentalsException class. Used to make Exceptions clearer.
 */
public class IllegalCurrentRentalsException extends Exception {
    public IllegalCurrentRentalsException(String message) {
        super(message);
    }
}