package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;

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
 *      ItemIDs have to be > 0.
 *      Titles cannot be null, empty or longer than ITEM_TITLE_MAX_LENGTH.
 *      Allowed rental days must not be less than 0.
 */
public class Item extends Entity {

    //TODO-future add more fields and methods
    //TODO-comment everything

    public static final int ITEM_TITLE_MAX_LENGTH; //TODO-PRIO RETRIEVE METADATA
    public static final int DEFAULT_ALLOWED_DAYS = 14;

    /*
      So we don't have to update both create_tables.sql AND this file when we want to change rules.
     */
    static
    {
        int[] metaData = DatabaseHandler.getItemMetaData();
        ITEM_TITLE_MAX_LENGTH = metaData[0];
    }

    private int itemID; //Primary key
    //ENUM TYPE
    private String title;
    //Barcode
    //ISBN
    //Genre/Classification
    //Author ID
    //Author name
    //Publisher ID
    //Publisher name
    private int allowedRentalDays;
    private boolean available; //True by default //TODO-prio double check availability on delete

    /**
     * Creation Constructor. Takes the needed values to construct a new Item as arguments.
     * @param title
     */
    public Item(String title) throws ConstructionException
    {
        try
        {
            this.itemID = 0; //Set AFTER initial INSERT by createNewItem
            setTitle(title); //Throws InvalidTitleException
            this.allowedRentalDays = DEFAULT_ALLOWED_DAYS; //TODO-prio for now
            this.available = true;
        }
        catch (InvalidTitleException e)
        {
            throw new ConstructionException("Failed to construct Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieval Constructor.
     * @param itemID
     * @param title
     * @param allowedRentalDays
     */
    public Item(int itemID, String title, int allowedRentalDays, boolean available) throws ConstructionException {
        try {
            setItemID(itemID); //Throws InvalidIDException
            setTitle(title); //Throws InvalidTitleException
            setAllowedRentalDays(allowedRentalDays);
            this.available = available;
        } catch (InvalidIDException | InvalidTitleException | InvalidDateException e) {
            throw new ConstructionException("Failed to construct Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
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

    public void setItemID(int itemID) throws InvalidIDException {
        if (itemID <= 0)
            throw new InvalidIDException("ItemID must not be less than 0. Received: " + itemID);
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws InvalidTitleException {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Title cannot be null or empty.");
        if (title.length() > ITEM_TITLE_MAX_LENGTH)
            throw new InvalidTitleException("Title cannot be longer than " +
                    ITEM_TITLE_MAX_LENGTH + " characters. Received: " + title);
        this.title = title;
    }

    //TODO-implement properly
    public int getAllowedRentalDays() {
        return allowedRentalDays;
    }

    public void setAllowedRentalDays(int allowedRentalDays) throws InvalidDateException {
        if (allowedRentalDays < 0)
            throw new InvalidDateException("Allowed rental days can't be negative. Received: " + allowedRentalDays);
        this.allowedRentalDays = allowedRentalDays;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}