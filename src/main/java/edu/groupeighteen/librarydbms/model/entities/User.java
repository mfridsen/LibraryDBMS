package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class represents a User of the application.
 *
 * Invariants, enforced by setters:
 *      UserIDs have to be positive integers.
 *      Usernames cannot be null or empty.
 *      Passwords cannot be null or empty.
 */
public class User extends Entity {

    //TODO-future add more fields and methods
    //TODO-comment everything
    public static final int DEFAULT_ALLOWED_RENTALS = 5;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH;

    static { //Now we don't have to update both create_tables.sql AND this file when we want to change the allowed size of usernames and passwords :)
        int[] metaData = DatabaseHandler.getUserMetaData();
        MAX_USERNAME_LENGTH = metaData[0];
        MAX_PASSWORD_LENGTH = metaData[1];
    }

    private int userID; //Primary key
    private String username;
    private String password; //TODO-future hash and salt
    private final int allowedRentals; //TODO-test
    private int currentRentals; //TODO-test
    private double lateFee; //TODO-test

    /**
     * Creation Constructor.
     * @param username
     * @param password
     */
    public User(String username, String password) throws ConstructionException {
        try {
            this.userID = 0;
            setUsername(username); //InvalidUsernameException
            setPassword(password); //InvalidPasswordException
            this.allowedRentals = DEFAULT_ALLOWED_RENTALS;
            this.currentRentals = 0;
            this.lateFee = 0.0;
            this.deleted = false;
        } catch (InvalidUsernameException | InvalidPasswordException e) {
            throw new ConstructionException("Failed to construct User due to " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieval Constructor.
     */
    public User(int userID, String username, String password, int allowedRentals, int currentRentals,
                double lateFee, boolean deleted) throws ConstructionException {
        try {
            setUserID(userID); //Throws InvalidIDException
            setUsername(username); //Throws InvalidUsernameException
            setPassword(password); //Throws InvalidPasswordException
            this.allowedRentals = allowedRentals;
            setCurrentRentals(currentRentals); //Throws InvalidRentalException
            setLateFee(lateFee); //Throws InvalidLateFeeException
            this.deleted = deleted;
        } catch (InvalidIDException | InvalidUsernameException | InvalidPasswordException | InvalidRentalException | InvalidLateFeeException e) {
            throw new ConstructionException("Failed to construct User due to " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public User(User other) {
        this.userID = other.userID;
        this.username = other.username;
        this.password = other.password;
        this.allowedRentals = other.allowedRentals;
        this.currentRentals = other.currentRentals;
        this.lateFee = other.lateFee;
        this.deleted = other.deleted;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) throws InvalidIDException {
        if (userID <= 0)
            throw new InvalidIDException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("Username cannot be null or empty.");
        if (username.length() < MIN_USERNAME_LENGTH)
            throw new InvalidUsernameException("Username too short, must be at least " + MIN_USERNAME_LENGTH + " characters. Received: " + username);
        if (username.length() > MAX_USERNAME_LENGTH)
            throw new InvalidUsernameException("Username too long, must be at most " + MAX_USERNAME_LENGTH + " characters. Received: " + username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidPasswordException {
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password cannot be null or empty.");
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too short, must be at least " + MIN_PASSWORD_LENGTH + " characters. Received: " + password.length());
        if (password.length() > 50)
            throw new InvalidPasswordException("Password too long, must be at most " + MAX_PASSWORD_LENGTH + " characters. Received: " + password.length());
        this.password = password;
    }

    public int getAllowedRentals() {
        return allowedRentals;
    }

    public int getCurrentRentals() {
        return currentRentals;
    }

    public void setCurrentRentals(int currentRentals) throws InvalidRentalException {
        if (currentRentals > allowedRentals)
            throw new InvalidRentalException("Current rentals can't be greater than allowed rentals. Received: " + currentRentals + ", allowed: " + allowedRentals);
        this.currentRentals = currentRentals;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) throws InvalidLateFeeException {
        if (lateFee < 0)
            throw new InvalidLateFeeException("Late fee cannot be less than zero. Received: " + lateFee);
        this.lateFee = lateFee;
    }
}