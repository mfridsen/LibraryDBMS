package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;

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
    public User(String username, String password) {
        this.userID = 0;
        setUsername(username);
        setPassword(password);
        this.allowedRentals = DEFAULT_ALLOWED_RENTALS;
        this.currentRentals = 0;
        this.lateFee = 0.0;
    }

    /**
     * Retrieval Constructor.
     */
    public User(int userID, String username, String password, int allowedRentals, int currentRentals, double lateFee) {
        setUserID(userID);
        setUsername(username);
        setPassword(password);
        this.allowedRentals = allowedRentals;
        setCurrentRentals(currentRentals);
        setLateFee(lateFee);
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
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        if (userID <= 0)
            throw new IllegalArgumentException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty. Received: " + username);
        if (username.length() < MIN_USERNAME_LENGTH)
            throw new IllegalArgumentException("Username too short, must be at least " + MIN_USERNAME_LENGTH + " characters. Received: " + username);
        if (username.length() > MAX_USERNAME_LENGTH)
            throw new IllegalArgumentException("Username too long, must be at most " + MAX_USERNAME_LENGTH + " characters. Received: " + username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty.");
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new IllegalArgumentException("Password too short, must be at least " + MIN_PASSWORD_LENGTH + " characters. Received: " + password.length());
        if (password.length() > 50)
            throw new IllegalArgumentException("Password too long, must be at most " + MAX_PASSWORD_LENGTH + " characters. Received: " + password.length());
        this.password = password;
    }

    public int getAllowedRentals() {
        return allowedRentals;
    }

    public int getCurrentRentals() {
        return currentRentals;
    }

    public void setCurrentRentals(int currentRentals) {
        if (currentRentals > allowedRentals)
            throw new IllegalArgumentException("Current rentals can't be greater than allowed rentals. Received: " + currentRentals + ", allowed: " + allowedRentals);
        this.currentRentals = currentRentals;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        if (lateFee < 0)
            throw new IllegalArgumentException("Late fee cannot be less than zero. Received: " + lateFee);
        this.lateFee = lateFee;
    }
}