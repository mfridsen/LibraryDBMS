package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class represents an Item in the library.
 *
 * Invariants, enforced by setters:
 *      ItemIDs have to be positive integers.
 *      Titles cannot be null or empty. //TODO-future add min and max length
 */
public class Item {

    //TODO-future add more fields and methods
    //TODO-comment everything

    private int itemID; //Primary key
    private String title;

    public Item(String title) {
        this.itemID = 0;
        setTitle(title);
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        if (itemID <= 0) {
            throw new IllegalArgumentException("ItemID must be greater than zero.");
        }
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }
}