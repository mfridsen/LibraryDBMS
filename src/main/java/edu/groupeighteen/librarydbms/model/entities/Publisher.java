package edu.groupeighteen.librarydbms.model.entities;

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
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class Publisher extends Entity
        //TODO-prio METADATA AND CONSTANTS
        /*
         * Helps with adherence to DRY, now if we want to change the rules we only need to do so in one place.
         */
{

    private int publisherID; //Primary key
    private String publisherName; //Varchar 255 UNIQUE NOT NULL
    private String email; //Varchar 255 UNIQUE NOT NULL

    /**
     * Creation Constructor.
     *
     * @param publisherName
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
     * Retrieval Constructor.
     *
     * @param publisherID
     * @param publisherName
     * @param email
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
        catch (InvalidIDException | InvalidNameException e)
        {
            throw new ConstructionException("Publisher Retrieval Construction failed due to " +
                                                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        catch (InvalidEmailException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy Constructor.
     *
     * @param other
     */
    public Publisher(Publisher other)
    {
        super(other);
        this.publisherID = other.publisherID;
        this.publisherName = other.publisherName;
        this.email = other.email;
    }

    public int getPublisherID()
    {
        return publisherID;
    }

    public void setPublisherID(int publisherID)
    throws InvalidIDException
    {
        if (publisherID <= 0)
            throw new InvalidIDException("Publisher ID must be greater than 0. Received: " + publisherID);
        this.publisherID = publisherID;
    }

    public String getPublisherName()
    {
        return publisherName;
    }

    public void setPublisherName(String publisherName)
    throws InvalidNameException
    {
        if (publisherName == null || publisherName.isEmpty())
            throw new InvalidNameException("Publisher name cannot be null or empty.");
        if (publisherName.length() > 255)
            throw new InvalidNameException("Publisher name must be at most 255 characters. " +
                                                   "Received: " + publisherName.length());
        this.publisherName = publisherName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    throws InvalidEmailException
    {
        if (email == null || email.isEmpty())
            throw new InvalidEmailException("Publisher email cannot be null or empty.");
        if (email.length() > 255)
            throw new InvalidEmailException("Publisher email must be at most 255 characters. " +
                                                   "Received: " + email.length());
        this.email = email;
    }
}