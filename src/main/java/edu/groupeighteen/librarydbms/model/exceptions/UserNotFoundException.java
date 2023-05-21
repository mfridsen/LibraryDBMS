package edu.groupeighteen.librarydbms.model.exceptions;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * Custom UserNotFoundException class. Used to make Exceptions clearer.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(int userId) {
        super("Failed to find user with ID: " + userId);
    }

    public UserNotFoundException(String username) {
        super("Failed to find user with username: " + username);
    }
}