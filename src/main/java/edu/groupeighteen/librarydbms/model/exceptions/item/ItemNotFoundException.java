package edu.groupeighteen.librarydbms.model.exceptions.item;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/19/2023
 * Custom ItemNotFoundException class. Used to make Exceptions clearer.
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(int itemId) {
        super("Failed to find item with ID: " + itemId);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}