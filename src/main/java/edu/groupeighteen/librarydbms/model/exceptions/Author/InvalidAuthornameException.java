package edu.groupeighteen.librarydbms.model.exceptions.Author;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-29
 */
public class InvalidAuthornameException extends Throwable {
    public InvalidAuthornameException(String message) {
        super(message);
    }

}
