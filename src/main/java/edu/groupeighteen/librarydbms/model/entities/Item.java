package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidRentalException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTitleException;

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
 *      Titles cannot be null or empty. //TODO-future add max length
 *      Allowed rental days must not be less than 0.
 */
public class Item extends Entity {

    //TODO-future add more fields and methods
    //TODO-comment everything
    public static final int ITEM_TITLE_MAX_LENGHT = 255;
    public static final int DEFAULT_ALLOWED_DAYS = 14;

    private int itemID; //Primary key
    private String title;
    private int allowedRentalDays;
    private boolean available;

    /**
     * Creation Constructor. Takes the needed values to construct a new Item as arguments.
     * @param title
     */
    public Item(String title) throws ConstructionException {
        try {
            this.itemID = 0; //Set AFTER initial INSERT by createNewItem
            setTitle(title); //Throws InvalidTitleException
            this.allowedRentalDays = DEFAULT_ALLOWED_DAYS; //TODO-prio for now
            this.available = true;
        } catch (InvalidTitleException e) {
            throw new ConstructionException("Failed to construct Item due to " + e.getClass().getName() + ": " + e.getMessage(), e);
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
            setAvailable(available);
        } catch (InvalidIDException | InvalidTitleException | InvalidRentalException e) {
            throw new ConstructionException("Failed to construct Item due to " + e.getClass().getName() + ": " + e.getMessage(), e);
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
        if (title.length() > ITEM_TITLE_MAX_LENGHT)
            throw new InvalidTitleException("Title cannot be longer than " + ITEM_TITLE_MAX_LENGHT + " characters. Received: " + title);
        this.title = title;
    }

    //TODO-implement properly
    public int getAllowedRentalDays() {
        return allowedRentalDays;
    }

    public void setAllowedRentalDays(int allowedRentalDays) throws InvalidRentalException {
        if (allowedRentalDays < 0)
            throw new InvalidRentalException("Allowed rental days can't be negative. Received: " + allowedRentalDays);
        this.allowedRentalDays = allowedRentalDays;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}