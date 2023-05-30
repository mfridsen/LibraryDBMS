package edu.groupeighteen.librarydbms.model.exceptions.Author;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-29
 */
public class NullAuthorException extends Exception{
    public NullAuthorException(String message) {
        super(message);
    }

}
