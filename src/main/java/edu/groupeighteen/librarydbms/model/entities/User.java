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
    private final int allowedRentals;
    private int currentRentals;
    private double lateFee;

    /**
     * Constructs a new User with the specified username and password. This is
     * generally used when creating a new User, as it assigns a default number of
     * allowed rentals and no late fee. An exception is thrown if the username or
     * password do not meet the specified criteria.
     *
     * @param username the username for the new User
     * @param password the password for the new User
     * @throws ConstructionException if the username or password is invalid
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
     * Constructs a User object based on data retrieved from the database.
     * This constructor is primarily used when a user is fetched from the database.
     * It validates the input data and sets all the fields of the User object accordingly.
     *
     * @param userID Unique identifier for the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param allowedRentals The maximum number of rentals allowed for the user.
     * @param currentRentals The number of rentals the user currently has.
     * @param lateFee Any late fee applicable to the user.
     * @param deleted Boolean indicating whether the user is marked as deleted.
     *
     * @throws ConstructionException If there are issues validating the provided data.
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
     * Constructs a new User as a copy of the specified User. This constructor
     * creates a deep copy, where the new User has the same attributes as the
     * original, but changes to the new User will not affect the original.
     *
     * @param other the User to be copied
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

    /**
     * Returns the unique identifier for this User.
     *
     * @return The unique identifier for the user.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the unique identifier for this User.
     *
     * @param userID The unique identifier to set.
     * @throws InvalidIDException If the provided ID is not valid (less than or equal to 0).
     */
    public void setUserID(int userID) throws InvalidIDException {
        if (userID <= 0)
            throw new InvalidIDException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    /**
     * Returns the username of this User.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this User.
     *
     * @param username The username to set.
     * @throws InvalidUsernameException If the provided username is not valid (null, empty, too short, or too long).
     */
    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("Username cannot be null or empty.");
        if (username.length() < MIN_USERNAME_LENGTH)
            throw new InvalidUsernameException("Username too short, must be at least " + MIN_USERNAME_LENGTH +
                    " characters. Received: " + username);
        if (username.length() > MAX_USERNAME_LENGTH)
            throw new InvalidUsernameException("Username too long, must be at most " + MAX_USERNAME_LENGTH +
                    " characters. Received: " + username);
        this.username = username;
    }

    /**
     * Returns the password of this User.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for this User.
     *
     * @param password The password to set.
     * @throws InvalidPasswordException If the provided password is not valid (null, empty, too short, or too long).
     */
    public void setPassword(String password) throws InvalidPasswordException {
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password cannot be null or empty.");
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too short, must be at least " + MIN_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
        if (password.length() > 50)
            throw new InvalidPasswordException("Password too long, must be at most " + MAX_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
        this.password = password;
    }

    /**
     * Returns the maximum number of rentals allowed for this User.
     *
     * @return The maximum number of rentals allowed for the user.
     */
    public int getAllowedRentals() {
        return allowedRentals;
    }

    /**
     * Returns the number of current rentals of this User.
     *
     * @return The number of current rentals of the user.
     */
    public int getCurrentRentals() {
        return currentRentals;
    }

    /**
     * Sets the number of current rentals for this User.
     *
     * @param currentRentals The number of current rentals to set.
     * @throws InvalidRentalException If the provided number of current rentals is greater than the allowed number of rentals.
     */
    public void setCurrentRentals(int currentRentals) throws InvalidRentalException {
        if (currentRentals > allowedRentals)
            throw new InvalidRentalException("Current rentals can't be greater than allowed rentals. Received: " +
                    currentRentals + ", allowed: " + allowedRentals);
        this.currentRentals = currentRentals;
    }

    /**
     * Returns the late fee of this User.
     *
     * @return The late fee of the user.
     */
    public double getLateFee() {
        return lateFee;
    }

    /**
     * Sets the late fee for this User.
     *
     * @param lateFee The late fee to set.
     * @throws InvalidLateFeeException If the provided late fee is negative.
     */
    public void setLateFee(double lateFee) throws InvalidLateFeeException {
        if (lateFee < 0)
            throw new InvalidLateFeeException("Late fee cannot be less than zero. Received: " + lateFee);
        this.lateFee = lateFee;
    }
}