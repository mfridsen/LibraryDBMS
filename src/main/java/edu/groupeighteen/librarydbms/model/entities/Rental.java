package edu.groupeighteen.librarydbms.model.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 4/27/2023
 *
 * This class represents the union of a User an an Item in a Rental.
 *
 * Invariants, enforced by setters:
 *      RentalIDs have to be positive integers.
 *      UserIDs have to be positive integers.
 *      ItemIDs have to be positive integers.
 *      RentalDates cannot be null, and must be equal or less than LocalDateTime.now().
 *      Usernames cannot be null or empty. //TODO-future add min and max length
 *      Titles cannot be null or empty. //TODO-future add min and max length
 */
public class Rental {

    //TODO-future add more fields and methods
    //TODO-comment everything

    /**
     * The rental ID, which serves as the primary key for the rental.
     */
    private int rentalID;

    /**
     * The user ID, which serves as a foreign key referencing the associated user.
     */
    private int userID;

    /**
     * The item ID, which serves as a foreign key referencing the associated item.
     */
    private int itemID;

    /**
     * The date and time of the rental's creation.
     */
    private LocalDateTime rentalDate;

    /**
     * The username associated with the rental, set upon creation or retrieval.
     */
    private String username;

    /**
     * The title of the item associated with the rental, set upon creation or retrieval.
     */
    private String itemTitle;

    /**
     * Main constructor.
     * @param userID
     * @param itemID
     * @param rentalDate
     */
    public Rental(int userID, int itemID, LocalDateTime rentalDate) {
        this.rentalID = 0;
        setUserID(userID);
        setItemID(itemID);
        setRentalDate(rentalDate);
        this.username = null;
        this.itemTitle = null;
    }

    /**
     * Quick constructor, only needs userID and itemID and calls the main constructor with these and
     * LocalDateTime.now() as arguments.
     * @param userID
     * @param itemID
     */
    public Rental(int userID, int itemID) {
        this(userID, itemID, LocalDateTime.now());
    }

    /**
     * Copy constructor.
     * @param other
     */
    public Rental(Rental other) {
        this.rentalID = other.rentalID;
        this.userID = other.userID;
        this.username = other.username;
        this.itemID = other.itemID;
        this.itemTitle = other.itemTitle;
        this.rentalDate = other.rentalDate;  // Assuming LocalDateTime is immutable
    }

    /**
     * Returns the rental ID.
     *
     * @return the rental ID
     */
    public int getRentalID() {
        return rentalID;
    }

    /**
     * Sets the rental ID.
     *
     * @param rentalID the rental ID to set
     * @throws IllegalArgumentException if the rental ID is less than or equal to zero
     */
    public void setRentalID(int rentalID) {
        if (rentalID <= 0) throw new IllegalArgumentException("RentalID must be greater than zero. Received: " + rentalID);
        this.rentalID = rentalID;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID the user ID to set
     * @throws IllegalArgumentException if the user ID is less than or equal to zero
     */
    public void setUserID(int userID) {
        if (userID <= 0) throw new IllegalArgumentException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    /**
     * Returns the item ID.
     *
     * @return the item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Sets the item ID.
     *
     * @param itemID the item ID to set
     * @throws IllegalArgumentException if the item ID is less than or equal to zero
     */
    public void setItemID(int itemID) {
        if (itemID <= 0) throw new IllegalArgumentException("ItemID must be greater than zero. Received: " + itemID);
        this.itemID = itemID;
    }

    /**
     * Returns the rental date.
     *
     * @return the rental date
     */
    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    /**
     * Sets the rental date.
     *
     * @param rentalDate the rental date to set
     * @throws IllegalArgumentException if the rental date is null or in the future
     */
    public void setRentalDate(LocalDateTime rentalDate) {
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("RentalDate cannot be null or in the future. Received: " + rentalDate);
        this.rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty. Received: " + username);
        this.username = username;
    }

    /**
     * Returns the item title.
     *
     * @return the item title
     */
    public String getItemTitle() {
        return itemTitle;
    }

    /**
     * Sets the item title.
     *
     * @param itemTitle the item title to set
     * @throws IllegalArgumentException if the item title is null or empty
     */
    public void setItemTitle(String itemTitle) {
        if (itemTitle == null || itemTitle.isEmpty())
            throw new IllegalArgumentException("Title cannot be null or empty. Received: " + itemTitle);
        this.itemTitle = itemTitle;
    }

}