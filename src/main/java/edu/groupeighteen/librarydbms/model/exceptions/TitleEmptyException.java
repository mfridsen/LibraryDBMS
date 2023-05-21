package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom TitleEmptyException class. Used to make Exceptions clearer.
 */
public class TitleEmptyException extends Exception {
    public TitleEmptyException(String message) {
        super(message);
    }
}