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
public class Item extends Entity {

    //TODO-future add more fields and methods
    //TODO-comment everything
    public static final int DEFAULT_ALLOWED_DAYS = 14;

    private int itemID; //Primary key
    private String title;
    private int allowedRentalDays;
    private boolean available;

    /**
     * Creation Constructor. Takes the needed values to construct a new Item as arguments.
     * @param title
     */
    public Item(String title) {
        this.itemID = 0; //Set AFTER initial INSERT by createNewItem
        this.allowedRentalDays = DEFAULT_ALLOWED_DAYS; //TODO-prio for now
        setTitle(title);
        this.available = true;
    }

    /**
     * Retrieval Constructor.
     * @param itemID
     * @param title
     * @param allowedRentalDays
     */
    public Item(int itemID, String title, int allowedRentalDays, boolean availabe) {
        setItemID(itemID);
        setTitle(title);
        setAllowedRentalDays(allowedRentalDays);
        setAvailable(availabe);
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public Item(Item other) {
        this.itemID = other.itemID;
        this.title = other.title;
        this.allowedRentalDays = other.allowedRentalDays;
        this.available = other.available;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        if (itemID <= 0) {
            throw new IllegalArgumentException("ItemID must be greater than zero. Received: " + itemID);
        }
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty. Received: " + title);
        }
        this.title = title;
    }

    //TODO-implement properly
    public int getAllowedRentalDays() {
        return allowedRentalDays;
    }

    public void setAllowedRentalDays(int allowedRentalDays) {
        this.allowedRentalDays = allowedRentalDays;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}