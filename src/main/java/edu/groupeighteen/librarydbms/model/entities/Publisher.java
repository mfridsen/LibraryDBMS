package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidEmailException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * The Publisher class represents a publisher in the library store system.
 * <p>
 * A publisher is characterized by a unique ID, a publisher name, and an email address.
 * The length of the publisher's name and email address are defined as static fields and are loaded
 * from the metadata stored in the database.
 * <p>
 * The Publisher class extends the Entity class, inheriting fields and methods related to soft deletion status.
 */
public class Publisher extends Entity
{
    /**
     * The maximum length of a publisher's name.
     */
    public static final int PUBLISHER_NAME_LENGTH;
    /**
     * The maximum length of a publisher's email.
     */
    public static final int PUBLISHER_EMAIL_LENGTH;

    /*
     * Helps with adherence to DRY, now if we want to change the rules we only need to do so in one place.
     */
    static
    {
        int[] metaData = DatabaseHandler.getPublisherMetaData();
        PUBLISHER_NAME_LENGTH = metaData[0];
        PUBLISHER_EMAIL_LENGTH = metaData[1];
    }

    /**
     * The unique ID of the publisher.
     */
    private int publisherID;
    /**
     * The name of the publisher.
     */
    private String publisherName; //Unique
    /**
     * The email address of the publisher.
     */
    private String email;

    /**
     * Creation constructor for the Publisher class.
     * <p>
     * This constructor is intended to be used when creating a new publisher that does not yet exist in the database.
     * The publisher's ID is automatically set to 0 and the email is set to null.
     *
     * @param publisherName The name of the publisher.
     * @throws ConstructionException If the provided name is invalid.
     */
    public Publisher(String publisherName)
    throws ConstructionException
    {
        super();
        try
        {
            setPublisherName(publisherName);
            this.email = null;
        }
        catch (InvalidNameException e)
        {
            throw new ConstructionException("Publisher Creation Construction failed due to " +
                                                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieval constructor for the Publisher class.
     * <p>
     * This constructor is intended to be used when retrieving a publisher from the database.
     * All fields are filled with values provided in the parameters.
     *
     * @param publisherID   The unique ID of the publisher.
     * @param publisherName The name of the publisher.
     * @param email         The email address of the publisher.
     * @param deleted       The soft deletion status of the publisher.
     * @throws ConstructionException If the provided name is invalid or the provided ID is invalid.
     */
    public Publisher(int publisherID, String publisherName, String email, boolean deleted)
    throws ConstructionException
    {
        super(deleted);
        try
        {
            setPublisherID(publisherID);
            setPublisherName(publisherName);
            setEmail(email);
        }
        catch (InvalidIDException | InvalidNameException | InvalidEmailException e)
        {
            throw new ConstructionException("Publisher Retrieval Construction failed due to " +
                                                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Copy constructor for the Publisher class.
     * <p>
     * Creates a new publisher object that is a copy of the provided publisher.
     *
     * @param other The publisher to copy.
     */
    public Publisher(Publisher other)
    {
        super(other);
        this.publisherID = other.publisherID;
        this.publisherName = other.publisherName;
        this.email = other.email;
    }

    /**
     * Returns the unique ID of the publisher.
     *
     * @return the unique ID of the publisher.
     */
    public int getPublisherID()
    {
        return publisherID;
    }

    /**
     * Sets the unique ID of the publisher.
     * The publisher ID must be greater than 0, otherwise an InvalidIDException is thrown.
     *
     * @param publisherID The unique ID of the publisher.
     * @throws InvalidIDException if the publisherID is less than or equal to 0.
     */
    public void setPublisherID(int publisherID)
    throws InvalidIDException
    {
        if (publisherID <= 0)
            throw new InvalidIDException("Publisher ID must be greater than 0. Received: " + publisherID);
        this.publisherID = publisherID;
    }

    /**
     * Returns the name of the publisher.
     *
     * @return the name of the publisher.
     */
    public String getPublisherName()
    {
        return publisherName;
    }

    /**
     * Sets the name of the publisher.
     * The name must not be null or empty, and must not exceed the length limit defined by PUBLISHER_NAME_LENGTH,
     * otherwise an InvalidNameException is thrown.
     *
     * @param publisherName The name of the publisher.
     * @throws InvalidNameException if the publisherName is null, empty or too long.
     */
    public void setPublisherName(String publisherName)
    throws InvalidNameException
    {
        if (publisherName == null || publisherName.isEmpty())
            throw new InvalidNameException("Publisher name cannot be null or empty.");
        if (publisherName.length() > PUBLISHER_NAME_LENGTH)
            throw new InvalidNameException("Publisher name must be at most " + PUBLISHER_NAME_LENGTH +
                                                   " characters. Received: " + publisherName.length());
        this.publisherName = publisherName;
    }

    /**
     * Returns the email address of the publisher.
     *
     * @return the email address of the publisher.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the email address of the publisher.
     * The email must not exceed the length limit defined by PUBLISHER_EMAIL_LENGTH,
     * otherwise an InvalidEmailException is thrown.
     *
     * @param email The email address of the publisher.
     * @throws InvalidEmailException if the email is too long.
     */
    public void setEmail(String email)
    throws InvalidEmailException
    {
        if (email.length() > PUBLISHER_EMAIL_LENGTH)
            throw new InvalidEmailException("Publisher email must be at most " + PUBLISHER_EMAIL_LENGTH +
                                                    " characters. Received: " + email.length());
        this.email = email;
    }
}