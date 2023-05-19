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
    public static final int ITEM_DEFAULT_ALLOWED_DAYS = 14;

    private int itemID; //Primary key
    private String title;
    private int allowedRentalDays;

    /**
     * Creation Constructor. Takes the needed values to construct a new Item as arguments.
     * @param title
     */
    public Item(String title) {
        this.itemID = 0;
        this.allowedRentalDays = ITEM_DEFAULT_ALLOWED_DAYS; //TODO-prio for now
        setTitle(title);
    }

    /**
     * Retrieval Constructor.
     * @param itemID
     * @param title
     * @param allowedRentalDays
     */
    public Item(int itemID, String title, int allowedRentalDays) {
        this.itemID = itemID;
        this.title = title;
        this.allowedRentalDays = allowedRentalDays;
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public Item(Item other) {
        this.itemID = other.itemID;
        this.title = other.title;
        this.allowedRentalDays = other.allowedRentalDays;
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
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
}