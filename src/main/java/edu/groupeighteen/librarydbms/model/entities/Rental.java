package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;

import java.sql.SQLException;
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
 *      Usernames cannot be null or empty. //TODO-future add min and max length in User
 *      Titles cannot be null or empty. //TODO-future add min and max length in Title
 */
public class Rental extends Entity {

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
     * The date and time when the rental item is due to be returned.
     */
    private LocalDateTime rentalDueDate;

    /**
     * The date and time when the rental item was actually returned.
     */
    private LocalDateTime rentalReturnDate;

    /**
     * Any late fee that was incurred due to late return of the rental item.
     */
    private double lateFee;

    /**
     * Constructs a new Rental object for creation and insertion into the rentals database table.
     * The constructor sets the userID and itemID based on the provided arguments and assigns the current time
     * to rentalDate.
     * The rentalReturnDate and lateFee are initialized to null and 0.0 respectively, as these values are expected
     * to be null and 0.0 when the Rental has just been created.
     * The username, itemTitle, and rentalDueDate are initialized to null but are expected to be set by the
     * createNewRental method of the RentalHandler before saving the Rental to the database.
     * The rentalID will be set after the rental object is saved in the database.
     *
     * @param userID The ID of the user who is renting the item.
     * @param itemID The ID of the item being rented.
     */
    public Rental(int userID, int itemID) {
        this.rentalID = 0; //Set after initial INSERT
        setUserID(userID);
        setItemID(itemID);
        setRentalDate(LocalDateTime.now());
        this.username = null; //Set by createNewRental
        this.itemTitle = null; //Set by createNewRental
        this.rentalDueDate = null; //Set by createNewRental
        this.rentalReturnDate = null; //Should be null since the Rental has just been created
        this.lateFee = 0.0; //Should be 0.0 since the Rental has just been created
    }

    /**
     * Constructs a Rental object with data retrieved from the rentals database table.
     * This constructor is typically used when loading a Rental from the database.
     * All fields are initialized based on the values provided as arguments.
     *
     * @param rentalID The unique ID of the rental, as stored in the database.
     * @param userID The ID of the user who is renting the item.
     * @param itemID The ID of the item being rented.
     * @param rentalDate The date and time the item was rented.
     * @param username The username of the user who is renting the item.
     * @param itemTitle The title of the item being rented.
     * @param rentalDueDate The due date for the rental.
     * @param rentalReturnDate The date the rental was returned. This is null if the item hasn't been returned yet.
     * @param lateFee The late fee for the rental, if any.
     */
    public Rental(int rentalID, int userID, int itemID, LocalDateTime rentalDate, String username, String itemTitle,
                  LocalDateTime rentalDueDate, LocalDateTime rentalReturnDate, double lateFee) {
        this.rentalID = rentalID;
        this.userID = userID;
        this.itemID = itemID;
        this.rentalDate = rentalDate;
        this.username = username;
        this.itemTitle = itemTitle;
        this.rentalDueDate = rentalDueDate;
        this.rentalReturnDate = rentalReturnDate;
        this.lateFee = lateFee;
    }

    /**
     * Constructs a new Rental object by copying the fields of an existing Rental object.
     * This constructor is typically used when updating a Rental in the database.
     * All fields are copied directly from the provided Rental object.
     *
     * @param other The Rental object to copy from.
     */
    public Rental(Rental other) {
        this.rentalID = other.rentalID;
        this.userID = other.userID;
        this.itemID = other.itemID;
        this.rentalDate = other.rentalDate;  // Assuming LocalDateTime is immutable
        this.username = other.username;
        this.itemTitle = other.itemTitle;
        this.rentalDueDate = other.rentalDueDate;
        this.rentalReturnDate = other.rentalReturnDate;
        this.lateFee = other.lateFee;
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

    //TODO-prio complete
    public LocalDateTime getRentalDueDate() {
        return rentalDueDate;
    }

    public void setRentalDueDate(LocalDateTime rentalDueDate) {
        this.rentalDueDate = rentalDueDate;
    }

    public LocalDateTime getRentalReturnDate() {
        return rentalReturnDate;
    }

    public void setRentalReturnDate(LocalDateTime rentalReturnDate) {
        this.rentalReturnDate = rentalReturnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }
}