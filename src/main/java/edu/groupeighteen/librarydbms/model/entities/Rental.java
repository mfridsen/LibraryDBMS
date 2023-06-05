package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.InvalidReceiptException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Mattias Fridsén, Johan Lund, Jesper Truedsson
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 4/27/2023
 * <p>
 * This class represents the union of a User an an Item in a Rental.
 * <p>
 * Invariants, enforced by setters:
 * RentalIDs have to be positive integers.
 * UserIDs have to be positive integers.
 * ItemIDs have to be positive integers.
 * RentalDates cannot be null, and must be equal or less than LocalDateTime.now().
 * Usernames cannot be null or empty.
 * Titles cannot be null or empty.
 * RentalDueDates cannot be null or before rentalDate. TODO-Move up in hierarchy
 * RentalReturnDates cannot be before rentalDate.
 * LateFees cannot be negative.
 */
public class Rental extends Entity
{
    //TODO-FUTURE implement actual receipt printing via own printer

    /**
     *
     */
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
     * The date and time when the rental item is due to be returned.
     */
    private LocalDateTime rentalDueDate;

    /**
     * The username associated with the rental, set upon creation or retrieval.
     */
    private String username;

    /**
     * The title of the item associated with the rental, set upon creation or retrieval.
     */
    private String itemTitle;

    private String itemType;

    /**
     * The date and time when the rental item was actually returned. Null by default.
     */
    private LocalDateTime rentalReturnDate;

    /**
     * Any late fee that was incurred due to late return of the rental item. 0.0 by default.
     */
    private double lateFee;

    /**
     * A receipt for the rental, containing relevant information.
     */
    private String receipt;

    //TODO-future implement
    private boolean active;
    private boolean overdue;

    /**
     * Constructs a new Rental object which represents a rental transaction between a user and an item.
     * This constructor is used when creating a new rental, therefore some attributes are automatically set:
     * the rental date is set to the current date and time, the return date is null as the rental has just been created,
     * the late fee is set to 0.0, and the deleted status is set to false.
     *
     * @param userID The ID of the user who is renting the item.
     * @param itemID The ID of the item being rented.
     * @throws ConstructionException If an error occurs while setting the values of the rental attributes. The cause of the exception (InvalidIDException or InvalidDateException) is included in the thrown exception.
     */
    public Rental(int userID, int itemID)
    throws ConstructionException
    {
        super();
        try
        {
            this.rentalID = 0; //Set AFTER initial INSERT by createNewRental
            setUserID(userID); //Throws InvalidIDException
            setItemID(itemID); //Throws InvalidIDException
            setRentalDate(LocalDateTime.now()); //Throws InvalidDateException
            this.rentalDueDate = null; //Set BEFORE initial INSERT by createNewRental
            this.username = null; //Set BEFORE initial INSERT by createNewRental
            this.itemTitle = null; //Set BEFORE initial INSERT by createNewRental
            this.itemType = null; //Set BEFORE initial INSERT by createNewRental
            this.rentalReturnDate = null; //Should be null since the Rental has just been created
            this.lateFee = 0.0; //Should be 0.0 since the Rental has just been created
            this.receipt = null; //Set BEFORE initial INSERT by createNewRental
        }
        catch (InvalidIDException | InvalidDateException e)
        {
            throw new ConstructionException(
                    "Failed to construct Rental due to " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a Rental object which represents a rental transaction between a user and an item.
     * This constructor is used when retrieving an existing rental from the database.
     *
     * @param rentalID         The ID of the rental.
     * @param userID           The ID of the user who is renting the item.
     * @param itemID           The ID of the item being rented.
     * @param rentalDate       The date and time when the item was rented.
     * @param rentalDueDate    The due date of the rental.
     * @param username         The username of the user who is renting the item.
     * @param itemTitle        The title of the item being rented.
     * @param itemType         The type of item being rented.
     * @param rentalReturnDate The return date of the rental. This is null if the rental is still ongoing.
     * @param lateFee          The late fee of the rental, if any. This is 0.0 if the rental is returned on time
     *                         or is still ongoing.
     * @param receipt          The receipt of the rental.
     * @param deleted          A boolean representing whether the rental has been (soft) deleted.
     * @throws ConstructionException If an error occurs while setting the values of the rental attributes.
     *                               The cause of the exception (InvalidIDException, InvalidDateException,
     *                               InvalidNameException, InvalidTitleException, or InvalidLateFeeException)
     *                               is included in the thrown exception.
     */
    public Rental(int rentalID, int userID, int itemID, LocalDateTime rentalDate, LocalDateTime rentalDueDate,
                  String username, String itemTitle, String itemType, LocalDateTime rentalReturnDate, double lateFee,
                  String receipt, boolean deleted)
    throws ConstructionException
    {
        super(deleted);
        try
        {
            setRentalID(rentalID); //Throws InvalidIDException
            setUserID(userID); //Throws InvalidIDException
            setItemID(itemID); //Throws InvalidIDException
            setRentalDate(rentalDate); //Throws InvalidDateException
            setRentalDueDate(rentalDueDate); //Throws InvalidDateException
            setUsername(username); //Throws InvalidNameException
            setItemTitle(itemTitle); //Throws InvalidTitleException
            setItemType(itemType); //Throws InvalidTypeException
            setRentalReturnDate(rentalReturnDate); //Throws InvalidDateException
            setLateFee(lateFee); //Throws InvalidLateFeeException
            setReceipt(receipt); //Throws InvalidReceiptException
        }
        catch (InvalidIDException | InvalidDateException | InvalidNameException | InvalidTitleException |
               InvalidLateFeeException | InvalidTypeException | InvalidReceiptException e)
        {
            throw new ConstructionException("Failed to construct Rental due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a new Rental object by copying the fields of an existing Rental object.
     * This constructor is typically used when updating a Rental in the database.
     * All fields are copied directly from the provided Rental object.
     *
     * @param other The Rental object to copy from.
     */
    public Rental(Rental other)
    {
        super(other);
        this.rentalID = other.rentalID;
        this.userID = other.userID;
        this.itemID = other.itemID;
        this.rentalDate = other.rentalDate;
        this.rentalDueDate = other.rentalDueDate;
        this.username = other.username;
        this.itemTitle = other.itemTitle;
        this.itemType = other.itemType;
        this.rentalReturnDate = other.rentalReturnDate;
        this.lateFee = other.lateFee;
        this.receipt = other.receipt;
        this.active = other.active;
        this.overdue = other.overdue;
    }

    /**
     * Returns the rental ID.
     *
     * @return the rental ID
     */
    public int getRentalID()
    {
        return rentalID;
    }

    /**
     * Sets the rental ID.
     *
     * @param rentalID the rental ID to set
     * @throws InvalidIDException if the rental ID is less than or equal to zero
     */
    public void setRentalID(int rentalID)
    throws InvalidIDException
    {
        if (rentalID <= 0) throw new InvalidIDException("RentalID must be greater than zero. Received: " + rentalID);
        this.rentalID = rentalID;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID the user ID to set
     * @throws InvalidIDException if the user ID is less than or equal to zero
     */
    public void setUserID(int userID)
    throws InvalidIDException
    {
        if (userID <= 0) throw new InvalidIDException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    /**
     * Returns the item ID.
     *
     * @return the item ID
     */
    public int getItemID()
    {
        return itemID;
    }

    /**
     * Sets the item ID.
     *
     * @param itemID the item ID to set
     * @throws InvalidIDException if the item ID is less than or equal to zero
     */
    public void setItemID(int itemID)
    throws InvalidIDException
    {
        if (itemID <= 0) throw new InvalidIDException("ItemID must be greater than zero. Received: " + itemID);
        this.itemID = itemID;
    }

    /**
     * Returns the rental date.
     *
     * @return the rental date
     */
    public LocalDateTime getRentalDate()
    {
        return rentalDate;
    }

    /**
     * Sets the rental date.
     *
     * @param rentalDate the rental date to set
     * @throws IllegalArgumentException if the rental date is null or in the future
     */
    public void setRentalDate(LocalDateTime rentalDate)
    throws InvalidDateException
    {
        if (rentalDate == null)
            throw new InvalidDateException("RentalDate cannot be null.");
        if (rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new InvalidDateException("RentalDate cannot be in the future. Received: " + rentalDate);
        this.rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Returns the rental due date, truncated to days.
     *
     * @return the rental due date
     */
    public LocalDateTime getRentalDueDate()
    {
        return rentalDueDate;
    }

    /**
     * Sets the rental due date.
     *
     * @param rentalDueDate the rental due date to set
     * @throws IllegalArgumentException if the rental due date is null or is before the current time
     */
    public void setRentalDueDate(LocalDateTime rentalDueDate)
    throws InvalidDateException
    {
        if (rentalDueDate == null)
            throw new InvalidDateException("Rental due date cannot be null.");
        if (rentalDueDate.isBefore(rentalDate))
            throw new InvalidDateException("Rental due date cannot be before rental date. Received: " + rentalDueDate);
        this.rentalDueDate = rentalDueDate.withHour(RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0)
                .truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username)
    throws InvalidNameException
    {
        if (username == null || username.isEmpty())
            throw new InvalidNameException("Username cannot be null or empty.");
        this.username = username;
    }

    /**
     * Returns the item title.
     *
     * @return the item title
     */
    public String getItemTitle()
    {
        return itemTitle;
    }

    /**
     * Sets the item title.
     *
     * @param itemTitle the item title to set
     * @throws IllegalArgumentException if the item title is null or empty
     */
    public void setItemTitle(String itemTitle)
    throws InvalidTitleException
    {
        if (itemTitle == null || itemTitle.isEmpty())
            throw new InvalidTitleException("Title cannot be null or empty.");
        this.itemTitle = itemTitle;
    }

    /**
     * Returns the item type.
     *
     * @return the item type
     */
    public String getItemType()
    {
        return itemType;
    }

    /**
     * Sets the item type.
     *
     * @param itemType the item type to be set
     * @throws InvalidTypeException if the item type is null or empty
     */
    public void setItemType(String itemType)
    throws InvalidTypeException
    {
        if (itemType == null || itemType.isEmpty())
            throw new InvalidTypeException("ItemType cannot be null or empty.");
        this.itemType = itemType;
    }

    /**
     * Returns the rental return date.
     *
     * @return the rental return date
     */
    public LocalDateTime getRentalReturnDate()
    {
        return rentalReturnDate;
    }

    /**
     * Sets the rental return date.
     *
     * @param rentalReturnDate the rental return date to set
     * @throws IllegalArgumentException if the rental return date is not null and is before the rental date
     */
    public void setRentalReturnDate(LocalDateTime rentalReturnDate)
    throws InvalidDateException
    {
        if (rentalReturnDate != null && rentalReturnDate.isBefore(this.getRentalDate()))
        {
            throw new InvalidDateException(
                    "Rental return date cannot be before the rental date. Received: " + rentalReturnDate);
        }
        this.rentalReturnDate = rentalReturnDate != null ? rentalReturnDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    /**
     * Returns the late fee.
     *
     * @return the late fee
     */
    public double getLateFee()
    {
        return lateFee;
    }

    /**
     * Sets the late fee.
     *
     * @param lateFee the late fee to set
     * @throws IllegalArgumentException if the late fee is negative
     */
    public void setLateFee(double lateFee)
    throws InvalidLateFeeException
    {
        if (lateFee < 0.0)
            throw new InvalidLateFeeException("Late fee cannot be negative. Received: " + lateFee);
        this.lateFee = lateFee;
    }

    /**
     * Returns the receipt.
     *
     * @return the receipt
     */
    public String getReceipt()
    {
        return receipt;
    }

    public void setReceipt(String receipt)
    throws InvalidReceiptException
    {
        if (receipt == null || receipt.isEmpty())
            throw new InvalidReceiptException("Receipt cannot be null or empty");
        this.receipt = receipt;
    }

    /**
     * Prints the receipt.
     * TODO-future: Implement printing to printers
     */
    public void printReceipt()
    {
        System.out.println(receipt);
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isOverdue()
    {
        return overdue;
    }

    public void setOverdue(boolean overdue)
    {
        this.overdue = overdue;
    }
}