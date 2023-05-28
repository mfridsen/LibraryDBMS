package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
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
public class Author extends Entity
    //TODO-prio METADATA AND CONSTANTS
{

    private int authorID; //Primary key
    private String authorFirstname; ///Varchar 100 NOT NULL
    private String authorLastName; //Varchar 100
    private String biography; //SQL TEXT

    /**
     * Creation Constructor.
     *
     * @param authorFirstname
     * @param authorLastName
     */
    public Author(String authorFirstname, String authorLastName)
    throws ConstructionException
    {
        super();
        try
        {
            this.authorID = 0;
            setAuthorFirstname(authorFirstname);
            setAuthorLastName(authorLastName);
            this.biography = null;
        }
        catch (InvalidNameException e)
        {
            throw new ConstructionException("Author Creation Construction failed due to " +
                                                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieval Constructor.
     *
     * @param authorID
     * @param authorFirstname
     * @param authorLastName
     * @param biography
     */
    public Author(int authorID, String authorFirstname, String authorLastName, String biography, boolean deleted)
    throws ConstructionException
    {
        super(deleted);
        try
        {
            setAuthorID(authorID);
            setAuthorFirstname(authorFirstname);
            setAuthorLastName(authorLastName);
            setBiography(biography);
        }
        catch (InvalidIDException | InvalidNameException e)
        {
            throw new ConstructionException("Author Retrieval Construction failed due to " +
                                                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Copy Constructor.
     *
     * @param other
     */
    public Author(Author other)
    {
        super(other);
        this.authorID = other.authorID;
        this.authorFirstname = other.authorFirstname;
        this.authorLastName = other.authorLastName;
        this.biography = other.biography;
    }


    public int getAuthorID()
    {
        return authorID;
    }

    public void setAuthorID(int authorID)
    throws InvalidIDException
    {
        if (authorID <= 0)
            throw new InvalidIDException("Author ID must be greater than 0. Received: " + authorID);
        this.authorID = authorID;
    }

    public String getAuthorFirstname()
    {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname)
    throws InvalidNameException
    {
        if (authorFirstname == null || authorFirstname.isEmpty())
            throw new InvalidNameException("Author first name cannot be null or empty.");
        if (authorFirstname.length() > 100)
            throw new InvalidNameException("Author first name must be at most 100 characters. " +
                                                   "Received: " + authorFirstname.length());
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorLastName()
    {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName)
    throws InvalidNameException
    {
        if (authorLastName.length() > 100)
            throw new InvalidNameException("Author last name must be at most 100 characters. " +
                                                   "Received: " + authorLastName.length());
        this.authorLastName = authorLastName;
    }

    public String getBiography()
    {
        return biography;
    }

    public void setBiography(String biography)
    {
        this.biography = biography;
    }
}