package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
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
 * Represents an Author in a library system.
 * The Author class extends Entity and contains information about the author, including
 * first name, last name, and biography.
 * It provides methods to set and get these values, enforcing various constraints.
 */
public class Author extends Entity
{
    /**
     * Constant defining the maximum length of the author's first name.
     */
    public static final int AUTHOR_FIRST_NAME_LENGTH;

    /**
     * Constant defining the maximum length of the author's last name.
     */
    public static final int AUTHOR_LAST_NAME_LENGTH;

    /*
     * Helps with adherence to DRY, now if we want to change the rules we only need to do so in one place.
     */
    static
    {
        int[] metaData = DatabaseHandler.getAuthorMetaData();
        AUTHOR_FIRST_NAME_LENGTH = metaData[0]; //100
        AUTHOR_LAST_NAME_LENGTH = metaData[1];  //100
    }

    /**
     * Unique identifier of the author. Must be greater than 0.
     */
    private int authorID;

    /**
     * Author's first name. Must not be null or empty.
     */
    private String authorFirstname;

    /**
     * Author's last name.
     */
    private String authorLastName;

    /**
     * Author's biography.
     */
    private String biography;

    /**
     * Constructor for creating a new Author.
     *
     * @param authorFirstname The first name of the author.
     * @param authorLastName  The last name of the author.
     * @throws ConstructionException if the author's first name is null, empty, or too long.
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
     * Constructor for retrieving an existing Author.
     *
     * @param authorID        The unique identifier of the author.
     * @param authorFirstname The first name of the author.
     * @param authorLastName  The last name of the author.
     * @param biography       The biography of the author.
     * @param deleted         Indicates whether the author is deleted.
     * @throws ConstructionException if the authorID is invalid or if the author's names are null, empty, or too long.
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
     * Copy constructor for Author.
     *
     * @param other The Author object to be copied.
     */
    public Author(Author other)
    {
        super(other);
        this.authorID = other.authorID;
        this.authorFirstname = other.authorFirstname;
        this.authorLastName = other.authorLastName;
        this.biography = other.biography;
    }

    /**
     * Returns the unique identifier of the author.
     *
     * @return The author's ID.
     */
    public int getAuthorID()
    {
        return authorID;
    }

    /**
     * Sets the unique identifier of the author.
     *
     * @param authorID The author's ID.
     * @throws InvalidIDException if the authorID is not greater than 0.
     */
    public void setAuthorID(int authorID)
    throws InvalidIDException
    {
        if (authorID <= 0)
            throw new InvalidIDException("Author ID must be greater than 0. Received: " + authorID);
        this.authorID = authorID;
    }

    /**
     * Returns the first name of the author.
     *
     * @return The author's first name.
     */
    public String getAuthorFirstname()
    {
        return authorFirstname;
    }

    /**
     * Sets the first name of the author.
     *
     * @param authorFirstname The author's first name.
     * @throws InvalidNameException if the first name is null, empty, or too long.
     */
    public void setAuthorFirstname(String authorFirstname)
    throws InvalidNameException
    {
        if (authorFirstname == null || authorFirstname.isEmpty())
            throw new InvalidNameException("Author first name cannot be null or empty.");
        if (authorFirstname.length() > AUTHOR_FIRST_NAME_LENGTH)
            throw new InvalidNameException("Author first name must be at most " + AUTHOR_FIRST_NAME_LENGTH +
                    " characters. Received: " + authorFirstname.length());
        this.authorFirstname = authorFirstname;
    }

    /**
     * Returns the last name of the author.
     *
     * @return The author's last name.
     */
    public String getAuthorLastName()
    {
        return authorLastName;
    }

    /**
     * Sets the last name of the author.
     *
     * @param authorLastName The author's last name.
     * @throws InvalidNameException if the last name is too long.
     */
    public void setAuthorLastName(String authorLastName)
    throws InvalidNameException
    {
        if (authorLastName.length() > AUTHOR_LAST_NAME_LENGTH)
            throw new InvalidNameException("Author last name must be at most " + AUTHOR_LAST_NAME_LENGTH +
                    " characters. Received: " + authorLastName.length());
        this.authorLastName = authorLastName;
    }

    /**
     * Returns the biography of the author.
     *
     * @return The author's biography.
     */
    public String getBiography()
    {
        return biography;
    }

    /**
     * Sets the biography of the author.
     *
     * @param biography The author's biography.
     */
    public void setBiography(String biography)
    {
        this.biography = biography;
    }
}