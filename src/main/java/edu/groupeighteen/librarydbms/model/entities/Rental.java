package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.*;

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
 *      Usernames cannot be null or empty.
 *      Titles cannot be null or empty.
 */
public class Rental extends Entity {
    public static final int RENTAL_DUE_DATE_HOURS = 20;
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


    //creation constructor
    public Rental(int userID, int itemID) throws ConstructionException {
        try {
            this.rentalID = 0; //Set AFTER initial INSERT by createNewRental
            setUserID(userID); //Throws InvalidIDException
            setItemID(itemID); //Throws InvalidIDException
            setRentalDate(LocalDateTime.now()); //Throws InvalidDateException
            this.username = null; //Set BEFORE initial INSERT by createNewRental
            this.itemTitle = null; //Set BEFORE initial INSERT by createNewRental
            this.rentalDueDate = null; //Set BEFORE initial INSERT by createNewRental
            this.rentalReturnDate = null; //Should be null since the Rental has just been created
            this.lateFee = 0.0; //Should be 0.0 since the Rental has just been created
            this.deleted = false;
        } catch (InvalidIDException | InvalidDateException e) {
            throw new ConstructionException(e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //retrieval constructor
    public Rental(int rentalID, int userID, int itemID, LocalDateTime rentalDate, String username, String itemTitle,
                  LocalDateTime rentalDueDate, LocalDateTime rentalReturnDate, double lateFee, boolean deleted) throws ConstructionException {
        try {
            setRentalID(rentalID); //Throws InvalidIDException
            setUserID(userID); //Throws InvalidIDException
            setItemID(itemID); //Throws InvalidIDException
            setRentalDate(rentalDate); //Throws InvalidDateException
            setUsername(username); //Throws InvalidUsernameException
            setItemTitle(itemTitle); //Throws InvalidTitleException
            setRentalDueDate(rentalDueDate); //Throws InvalidDateException
            setRentalReturnDate(rentalReturnDate); //Throws InvalidDateException
            setLateFee(lateFee); //Throws InvalidLateFeeException
            this.deleted = deleted;
        } catch (InvalidIDException | InvalidDateException | InvalidUsernameException | InvalidTitleException | InvalidLateFeeException e) {
            throw new ConstructionException(e.getClass().getName() + ": " + e.getMessage(), e);
        }
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
        this.rentalDate = other.rentalDate;  //Assuming LocalDateTime is immutable
        this.username = other.username;
        this.itemTitle = other.itemTitle;
        this.rentalDueDate = other.rentalDueDate;
        this.rentalReturnDate = other.rentalReturnDate;
        this.lateFee = other.lateFee;
        this.deleted = other.deleted;
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
     * @throws InvalidIDException if the rental ID is less than or equal to zero
     */
    public void setRentalID(int rentalID) throws InvalidIDException {
        if (rentalID <= 0) throw new InvalidIDException("RentalID must be greater than zero. Received: " + rentalID);
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
     * @throws InvalidIDException if the user ID is less than or equal to zero
     */
    public void setUserID(int userID) throws InvalidIDException {
        if (userID <= 0) throw new InvalidIDException("UserID must be greater than zero. Received: " + userID);
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
     * @throws InvalidIDException if the item ID is less than or equal to zero
     */
    public void setItemID(int itemID) throws InvalidIDException {
        if (itemID <= 0) throw new InvalidIDException("ItemID must be greater than zero. Received: " + itemID);
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
    public void setRentalDate(LocalDateTime rentalDate) throws InvalidDateException {
        if (rentalDate == null)
            throw new InvalidDateException("RentalDate cannot be null.");
        if (rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new InvalidDateException("RentalDate cannot be in the future. Received: " + rentalDate);
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
    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("Username cannot be null or empty.");
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
    public void setItemTitle(String itemTitle) throws InvalidTitleException {
        if (itemTitle == null || itemTitle.isEmpty())
            throw new InvalidTitleException("Title cannot be null or empty.");
        this.itemTitle = itemTitle;
    }

    /**
     * Returns the rental due date, truncated to days.
     *
     * @return the rental due date
     */
    public LocalDateTime getRentalDueDate() {
        return rentalDueDate;
    }

    /**
     * Sets the rental due date.
     *
     * @param rentalDueDate the rental due date to set
     * @throws IllegalArgumentException if the rental due date is null or is before the current time
     */
    public void setRentalDueDate(LocalDateTime rentalDueDate) throws InvalidDateException {
        if (rentalDueDate == null)
            throw new InvalidDateException("Rental due date cannot be null.");
        if (rentalDueDate.isBefore(LocalDateTime.now()))
            throw new InvalidDateException("Rental due date cannot be in the past. Received: " + rentalDueDate);
        this.rentalDueDate = rentalDueDate.withHour(RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Returns the rental return date.
     *
     * @return the rental return date
     */
    public LocalDateTime getRentalReturnDate() {
        return rentalReturnDate;
    }

    /**
     * Sets the rental return date.
     *
     * @param rentalReturnDate the rental return date to set
     * @throws IllegalArgumentException if the rental return date is not null and is before the rental date
     */
    public void setRentalReturnDate(LocalDateTime rentalReturnDate) throws InvalidDateException {
        if (rentalReturnDate == null)
            throw new InvalidDateException("Rental return date cannot be null.");
        if (rentalReturnDate.isBefore(this.getRentalDate()))
            throw new InvalidDateException("Rental return date cannot be before the rental date. Received: " + rentalReturnDate);
        this.rentalReturnDate = rentalReturnDate.truncatedTo(ChronoUnit.SECONDS);
    }
    /**
     * Returns the late fee.
     *
     * @return the late fee
     */
    public double getLateFee() {
        return lateFee;
    }

    /**
     * Sets the late fee.
     *
     * @param lateFee the late fee to set
     * @throws IllegalArgumentException if the late fee is negative
     */
    public void setLateFee(double lateFee) throws InvalidLateFeeException {
        if (lateFee < 0.0)
            throw new InvalidLateFeeException("Late fee cannot be negative. Received: " + lateFee);
        this.lateFee = lateFee;
    }
}