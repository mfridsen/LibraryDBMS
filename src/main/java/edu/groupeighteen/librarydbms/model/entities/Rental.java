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

    private int rentalID; //Primary key
    private int userID; //Foreign key referencing User
    private int itemID; //Foreign key referencing Item
    private LocalDateTime rentalDate; //Date of creation
    private String username; //Set upon creation or retrieval
    private String title; //Set upon creation or retrieval

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
        this.title = null;
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
        this.title = other.title;
        this.rentalDate = other.rentalDate;  // Assuming LocalDateTime is immutable
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getRentalID() {
        return rentalID;
    }

    public void setRentalID(int rentalID) {
        if (rentalID <= 0) throw new IllegalArgumentException("RentalID must be greater than zero. Received: " + rentalID);
        this.rentalID = rentalID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        if (userID <= 0) throw new IllegalArgumentException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        if (itemID <= 0) throw new IllegalArgumentException("ItemID must be greater than zero. Received: " + itemID );
        this.itemID = itemID;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("RentalDate cannot be null or in the future. Received: " + rentalDate);
        this.rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS); //TODO-test we added truncation, test it
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty. Received: " + username);
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty. Received: " + title);
        this.title = title;
    }
}