package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidRentalStatusChangeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 * <p>
 * This class represents a User of the application.
 * <p>
 * Invariants, enforced by setters:
 * <p>
 * UserIDs have to be positive integers.
 * <p>
 * Usernames cannot be null or empty,
 * shorter than {@value MIN_USERNAME_LENGTH} or longer than MAX_USERNAME_LENGTH.
 * <p>
 * Passwords cannot be null or empty,
 * shorter than {@value MIN_PASSWORD_LENGTH} or longer than MAX_PASSWORD_LENGTH.
 * <p>
 * Current rentals cannot be negative or greater than allowedRentals.
 * <p>
 * If the user has less rentals than allowed, they can rent.
 * <p>
 * If the user has as many rentals as allowed, they can't rent until at least one item is returned.
 * <p>
 * If the user has no late fee, they are allowed to rent.
 * <p>
 * If the user has a late fee, they are not allowed to rent.
 * <p>
 * If the user is deleted, they are not allowed to rent.
 */
public class User extends Entity
{
    /**
     * The minimum length for a username. Shorter usernames will be considered invalid.
     */
    public static final int MIN_USERNAME_LENGTH = 3;
    /**
     * The maximum length for a username. Longer usernames will be considered invalid.
     */
    public static final int MAX_USERNAME_LENGTH; //20
    /**
     * The minimum length for a password. Shorter passwords will be considered invalid.
     */
    public static final int MIN_PASSWORD_LENGTH = 8;
    /**
     * The maximum length for a password. Longer passwords will be considered invalid.
     */
    public static final int MAX_PASSWORD_LENGTH; //50
    /**
     * The minimum length for an email address. Shorter email addresses will be considered invalid.
     */
    public static final int MIN_EMAIL_LENGTH = 6;
    /**
     * The maximum length for an email address. Longer email addresses will be considered invalid.
     */
    public static final int MAX_EMAIL_LENGTH; //255

    /*
     * Initializes the maximum length fields for username, password and email.
     * The lengths are fetched from the database.
     */
    static
    {
        int[] metaData = DatabaseHandler.getUserMetaData();
        MAX_USERNAME_LENGTH = metaData[0];
        MAX_PASSWORD_LENGTH = metaData[1];
        MAX_EMAIL_LENGTH = metaData[2];
    }
    //An email address needs to have a minimum of six characters:
    // One character for the user name.
    // The @ symbol.
    // One character for the domain name.
    // The dot symbol (.)
    // Two characters for the top level domain (like .com, .org, .io, .us).

    /**
     * Whether this user is allowed to rent items.
     */
    boolean allowedToRent;
    /**
     * The unique identifier for this user in the database.
     */
    private int userID; //Primary key

    /**
     * The unique username of this user. It must be between MIN_USERNAME_LENGTH and MAX_USERNAME_LENGTH characters long.
     */
    private String username; //UNIQUE

    /**
     * The password for this user. It must be between MIN_PASSWORD_LENGTH and MAX_PASSWORD_LENGTH characters long.
     */
    private String password; //TODO-future hash and salt

    /**
     * The unique email of this user. It must be between MIN_EMAIL_LENGTH and MAX_EMAIL_LENGTH characters long.
     */
    private String email; //UNIQUE //TODO-future REGEX

    /**
     * The type of this user, as defined in the UserType enum.
     */
    private UserType userType;

    /**
     * The maximum number of items this user can rent at once.
     */
    private int allowedRentals;

    /**
     * The current number of items this user has rented.
     */
    private int currentRentals;

    /**
     * The amount of late fee this user owes.
     */
    private double lateFee;

    /**
     * Constructs a new User with the specified username and password. This is
     * generally used when creating a new User, as it assigns a default number of
     * allowed rentals and no late fee. An exception is thrown if the username or
     * password do not meet the specified criteria.
     *
     * @param username the username for the new User
     * @param password the password for the new User
     * @param email    The users email address
     * @param userType The type of the user.
     * @throws ConstructionException if the username or password is invalid
     */
    public User(String username, String password, String email, UserType userType)
    throws ConstructionException
    {
        super();
        try
        {
            this.userID = 0;
            setUsername(username); //Throws InvalidNameException
            setPassword(password); //Throws InvalidPasswordException
            setEmail(email); //Throws InvalidEmailException
            setUserType(userType); //Throws InvalidTypeException
            setAllowedRentals(getDefaultAllowedRentals(userType)); //Throws InvalidUserRentalsException
            setCurrentRentals(0); //Throws InvalidUserRentalsException
            setLateFee(0); //Throws InvalidLateFeeException
            switch (userType) //Throws InvalidRentalStatusChangeException
            {
                case ADMIN, STAFF -> setAllowedToRent(false);
                default -> setAllowedToRent(true);
            }
        }
        catch (InvalidNameException | InvalidPasswordException | InvalidTypeException | InvalidEmailException
               | InvalidUserRentalsException | InvalidLateFeeException | InvalidRentalStatusChangeException e)
        {
            throw new ConstructionException("Failed to construct User due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //TODO-future private boolean activeRentals;

    /**
     * Constructs a User object based on data retrieved from the database.
     * This constructor is primarily used when a user is fetched from the database.
     * It validates the input data and sets all the fields of the User object accordingly.
     *
     * @param userID         Unique identifier for the user.
     * @param username       The username of the user.
     * @param password       The password of the user.
     * @param email          The users email address
     * @param userType       The type of the user.
     * @param allowedRentals The maximum number of rentals allowed for the user.
     * @param currentRentals The number of rentals the user currently has.
     * @param lateFee        Any late fee applicable to the user.
     * @param deleted        Boolean indicating whether the user is marked as deleted.
     * @throws ConstructionException If there are issues validating the provided data.
     */
    public User(int userID, String username, String password, String email, UserType userType, int allowedRentals,
                int currentRentals, double lateFee, boolean allowedToRent, boolean deleted)
    throws ConstructionException
    {
        super(deleted);

        try
        {
            setUserID(userID);                  //Throws InvalidIDException
            setUsername(username);              //Throws InvalidNameException
            setPassword(password);              //Throws InvalidPasswordException
            setUserType(userType);              //Throws InvalidTypeException
            setEmail(email);                    //Throws InvalidEmailException
            setAllowedRentals(allowedRentals);  //Throws InvalidUserRentalsException
            setCurrentRentals(currentRentals);  //Throws InvalidUserRentalsException
            setLateFee(lateFee);                //Throws InvalidLateFeeException
            setAllowedToRent(allowedToRent);    //Throws InvalidRentalStatusChangeException
        }
        catch (InvalidIDException | InvalidNameException | InvalidPasswordException | InvalidLateFeeException
               | InvalidRentalStatusChangeException | InvalidUserRentalsException | InvalidTypeException
               | InvalidEmailException e)
        {
            throw new ConstructionException("Failed to construct User due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a new User as a copy of the specified User. This constructor
     * creates a deep copy, where the new User has the same attributes as the
     * original, but changes to the new User will not affect the original.
     *
     * @param other the User to be copied
     */
    public User(User other)
    {
        super(other);
        this.userID = other.userID;
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.userType = other.userType;
        this.allowedRentals = other.allowedRentals;
        this.currentRentals = other.currentRentals;
        this.lateFee = other.lateFee;
        this.allowedToRent = other.allowedToRent;
    }

    /**
     * Retrieves the default number of rentals for a given UserType.
     *
     * @param type The UserType for which to get the default rentals.
     * @return An integer representing the default number of rentals.
     */
    public static int getDefaultAllowedRentals(UserType type)
    {
        return switch (type)
                {
                    case ADMIN, STAFF -> 0;
                    case PATRON -> 3;
                    case STUDENT -> 5;
                    case TEACHER -> 10;
                    case RESEARCHER -> 20;
                };
    }

    /**
     * Returns the unique identifier for this User.
     *
     * @return The unique identifier for the user.
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * Sets the unique identifier for this User.
     *
     * @param userID The unique identifier to set.
     * @throws InvalidIDException If the provided ID is not valid (less than or equal to 0).
     */
    public void setUserID(int userID)
    throws InvalidIDException
    {
        if (userID <= 0)
            throw new InvalidIDException("UserID must be greater than zero. Received: " + userID);
        this.userID = userID;
    }

    /**
     * Returns the username of this User.
     *
     * @return The username of the user.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username for this User.
     *
     * @param username The username to set.
     * @throws InvalidNameException If the provided username is not valid (null, empty, too short, or too long).
     */
    public void setUsername(String username)
    throws InvalidNameException
    {
        if (username == null || username.isEmpty())
            throw new InvalidNameException("Username cannot be null or empty.");
        if (username.length() < MIN_USERNAME_LENGTH)
            throw new InvalidNameException("Username too short, must be at least " + MIN_USERNAME_LENGTH +
                    " characters. Received: " + username);
        if (username.length() > MAX_USERNAME_LENGTH)
            throw new InvalidNameException("Username too long, must be at most " + MAX_USERNAME_LENGTH +
                    " characters. Received: " + username);
        this.username = username;
    }

    /**
     * Returns the password of this User.
     *
     * @return The password of the user.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password for this User.
     *
     * @param password The password to set.
     * @throws InvalidPasswordException If the provided password is not valid (null, empty, too short, or too long).
     */
    public void setPassword(String password)
    throws InvalidPasswordException
    {
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password cannot be null or empty.");
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too short, must be at least " + MIN_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
        if (password.length() > MAX_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too long, must be at most " + MAX_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
        this.password = password;
    }

    /**
     * Returns the user type of this user.
     *
     * @return The user type of this user.
     */
    public UserType getUserType()
    {
        return userType;
    }

    /**
     * Sets the user type of this user and updates the user's allowed rentals accordingly.
     *
     * @param userType The user type to set for this user.
     * @throws InvalidTypeException        if the given userType is null.
     * @throws InvalidUserRentalsException if updating allowed rentals fails for some reason.
     */
    public void setUserType(UserType userType)
    throws InvalidTypeException, InvalidUserRentalsException
    {
        if (userType == null)
            throw new InvalidTypeException("User Type cannot be null.");

        //Update allowedRentals and allowedToRent
        setAllowedRentals(getDefaultAllowedRentals(userType));

        this.userType = userType;
    }

    /**
     * Returns the email address of this user.
     *
     * @return The email address of this user.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the email address of this user.
     *
     * @param email The email address to set for this user.
     * @throws InvalidEmailException if the given email is null, empty, shorter than MIN_EMAIL_LENGTH or longer than MAX_EMAIL_LENGTH.
     */
    public void setEmail(String email)
    throws InvalidEmailException
    {
        if (email == null || email.isEmpty())
            throw new InvalidEmailException("Email cannot be null or empty.");
        if (email.length() < MIN_EMAIL_LENGTH)
            throw new InvalidEmailException("Email cannot be shorter than " + MIN_EMAIL_LENGTH + " characters. " +
                    "Received: " + email.length());
        if (email.length() > MAX_EMAIL_LENGTH)
            throw new InvalidEmailException("Email cannot be longer than " + MAX_EMAIL_LENGTH + " characters. " +
                    "Received: " + email.length());
        this.email = email;
    }

    /**
     * Returns the maximum number of rentals allowed for this User.
     *
     * @return The maximum number of rentals allowed for the user.
     */
    public int getAllowedRentals()
    {
        return allowedRentals;
    }

    /**
     * Sets the number of rentals allowed for this user and updates the allowedToRent attribute accordingly.
     *
     * @param allowedRentals The number of rentals to be set for this user.
     * @throws InvalidUserRentalsException if the given allowedRentals is less than zero.
     */
    public void setAllowedRentals(int allowedRentals)
    throws InvalidUserRentalsException
    {
        if (allowedRentals < 0)
            throw new InvalidUserRentalsException("Allowed Rentals can't be less than 0. Received: " + allowedRentals);

        //Updates allowedToRent
        setCurrentRentals(currentRentals);

        this.allowedRentals = allowedRentals;
    }

    /**
     * Returns the number of current rentals of this User.
     *
     * @return The number of current rentals of the user.
     */
    public int getCurrentRentals()
    {
        return currentRentals;
    }

    /**
     * Sets the number of current rentals for this User.
     * <p>
     * If the user has less rentals than allowed, they can rent again.
     * If the user has as many rentals as allowed, they can't rent until at least one item is returned.
     *
     * @param currentRentals The number of current rentals to set.
     * @throws InvalidUserRentalsException If the provided number of current rentals is lower than 0 or
     *                                     greater than the allowed number of rentals.
     */
    public void setCurrentRentals(int currentRentals)
    throws InvalidUserRentalsException
    {
        //Can't be less than zero
        if (currentRentals < 0)
            throw new InvalidUserRentalsException("Current rentals can't be lower than 0. Received: " + currentRentals);
        //Current rentals can't be greater than allowed
        if (currentRentals > allowedRentals)
            throw new InvalidUserRentalsException("Current rentals can't be greater than allowed rentals. Received: " +
                    currentRentals + ", allowed: " + allowedRentals);
        //User is still allowed to rent
        if (currentRentals < allowedRentals)
            allowedToRent = true;
        //User is no longer allowed to rent until at least one item is returned
        if (currentRentals == allowedRentals)
            allowedToRent = false;
        this.currentRentals = currentRentals;
    }

    /**
     * Returns the late fee of this User.
     *
     * @return The late fee of the user.
     */
    public double getLateFee()
    {
        return lateFee;
    }

    /**
     * Sets the late fee for this User.
     * <p>
     * If the user has no late fee, they are allowed to rent again.
     * If the user has a late fee, they are not allowed to rent.
     *
     * @param lateFee The late fee to set.
     * @throws InvalidLateFeeException If the provided late fee is negative.
     */
    public void setLateFee(double lateFee)
    throws InvalidLateFeeException
    {
        // Late fee can't be less than zero
        if (lateFee < 0)
            throw new InvalidLateFeeException("Late fee cannot be less than zero. Received: " + lateFee);
        // If the late fee is paid off, the user is allowed to rent again
        if (lateFee == 0)
            allowedToRent = true;
        // If there is a late fee, the user is not allowed to rent
        if (lateFee > 0)
            allowedToRent = false;
        this.lateFee = lateFee;
    }

    /**
     * Retrieves the rental status of the user.
     *
     * @return allowedToRent a boolean value that indicates whether the user is currently allowed to rent items or not.
     */
    public boolean isAllowedToRent()
    {
        return allowedToRent;
    }

    /**
     * Sets the rental status for the user.
     *
     * @param allowedToRent the new rental status to be set. If true, the user is allowed to rent items; if false,
     *                      the user is not.
     * @throws InvalidRentalStatusChangeException if the attempt to change the rental status contradicts the
     *                                            library's rules. The rules being, a user with no late fee and with fewer current rentals
     *                                            than permitted should be allowed to rent.
     */
    public void setAllowedToRent(boolean allowedToRent)
    throws InvalidRentalStatusChangeException
    {
        // If the user is deleted, they can't rent.
        if (deleted)
        {
            if (allowedToRent)
            {
                throw new InvalidRentalStatusChangeException("Attempt to change rental status failed. " +
                        "A deleted user cannot be allowed to rent.");
            }
        }
        // If the user has no late fee and fewer rentals than allowed, they can rent.
        else if (lateFee == 0.0 && currentRentals < allowedRentals)
        {
            if (!allowedToRent)
            {
                throw new InvalidRentalStatusChangeException("Attempt to change rental status failed. " +
                        "A user with no late fee and fewer rentals than permitted should be allowed to rent. " +
                        "Current late fee: " + lateFee + ", Current rentals: " + currentRentals +
                        ", Allowed rentals: " + allowedRentals);
            }
        }
        // If the user has a late fee or has reached the maximum rentals, they can't rent.
        else if (lateFee > 0.0 || currentRentals >= allowedRentals)
        {
            if (allowedToRent)
            {
                throw new InvalidRentalStatusChangeException("Attempt to change rental status failed. " +
                        "User has late fee: " + lateFee + " or has rented to capacity.");
            }
        }

        this.allowedToRent = allowedToRent;
    }

    /**
     * Represents the different types of users in the system.
     * <p>
     * ADMIN: Has full control over the system and can perform any operation.
     * STAFF: Can perform most operations, but may be restricted in some areas.
     * PATRON: A regular user of the system, with the least privileges.
     * STUDENT: Represents a student in an educational setting. May have extra privileges related to educational
     * materials.
     * TEACHER: Represents a teacher in an educational setting. May have extra privileges related to educational
     * materials.
     * RESEARCHER: Represents a researcher in an educational or corporate setting. May have extra privileges related
     * to research materials.
     */
    public enum UserType
    {
        ADMIN,
        STAFF,
        PATRON,
        STUDENT,
        TEACHER,
        RESEARCHER
    }
}