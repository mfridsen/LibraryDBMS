package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/29/2023
 * <p>
 * Represents a literature item in the library.
 * This class extends from the Item class and adds specific property for literature items: ISBN.
 */
public class Literature extends Item
{
    /**
     * The maximum length of an ISBN for a Literature item. It is retrieved from the database metadata.
     */
    public static final int LITERATURE_ISBN_LENGTH;

    /*
     * This block initializes the LITERATURE_ISBN_LENGTH from the database metadata.
     * The first value of the retrieved metadata array is assumed to be the ISBN length.
     */
    static
    {
        int[] metaData = DatabaseHandler.getLiteratureMetaData();
        LITERATURE_ISBN_LENGTH = metaData[0];
    }

    /**
     * The International Standard Book Number (ISBN) for this Literature item.
     * It is a unique numeric commercial book identifier.
     */
    protected String ISBN;

    /**
     * Constructs a new Literature object with the specified title, item type, author ID, classification ID,
     * barcode, and ISBN.
     *
     * @param title            the title of the literature
     * @param type             the item type
     * @param authorID         the author's ID
     * @param classificationID the classification ID
     * @param barcode          the barcode
     * @param ISBN             the ISBN of the literature
     * @throws ConstructionException if any of the parameters do not meet the requirements
     */
    public Literature(String title, ItemType type, int authorID, int classificationID, String barcode, String ISBN)
    throws ConstructionException
    {
        super(title, type, barcode, authorID, classificationID);
        try
        {
            setISBN(ISBN);
        }
        catch (InvalidISBNException e)
        {
            throw new ConstructionException("Literature Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a new Literature object with the given parameters. This constructor is typically used when
     * retrieving data from the database, where all properties are known.
     *
     * @param deleted            Indicates whether the item is marked as deleted.
     * @param itemID             The unique ID for this item.
     * @param title              The title of this Literature item.
     * @param type               The type of this item.
     * @param barcode            The unique barcode for this item.
     * @param authorID           The ID of the author of this Literature item.
     * @param classificationID   The ID of the classification of this Literature item.
     * @param authorFirstname    Name of the author or director of the item.
     * @param authorLastname     Last name of the author or director of the item.
     * @param classificationName The name of the classification of this Literature item.
     * @param allowedRentalDays  The number of days this item is allowed to be rented.
     * @param available          Indicates whether this item is currently available for rent.
     * @param ISBN               The International Standard Book Number (ISBN) of this Literature item.
     * @throws ConstructionException If any of the parameters do not meet the requirements.
     */
    public Literature(boolean deleted, int itemID, String title,
                      ItemType type, String barcode, int authorID, int classificationID, String authorFirstname,
                      String authorLastname, String classificationName, int allowedRentalDays, boolean available,
                      String ISBN)
    throws ConstructionException
    {
        super(deleted, itemID, title, type, barcode, authorID, classificationID, authorFirstname, authorLastname,
                classificationName, allowedRentalDays, available);
        try
        {
            setISBN(ISBN);
        }
        catch (InvalidISBNException e)
        {
            throw new ConstructionException("Literature Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a new Literature object based on the given Literature object.
     *
     * @param other the Literature object to copy
     */
    public Literature(Literature other)
    {
        super(other);
        this.ISBN = other.ISBN;
    }

    /**
     * Returns the ISBN of this Literature.
     *
     * @return the ISBN of this Literature
     */
    public String getISBN()
    {
        return ISBN;
    }

    /**
     * Sets the ISBN of this Literature.
     *
     * @param ISBN the new ISBN
     * @throws InvalidISBNException if the ISBN is null, empty or exceeds the maximum length
     */
    public void setISBN(String ISBN)
    throws InvalidISBNException
    {
        if (ISBN == null || ISBN.isEmpty())
            throw new InvalidISBNException("ISBN must not be null or empty.");
        if (ISBN.length() > LITERATURE_ISBN_LENGTH)
            throw new InvalidISBNException("ISBN cannot be longer than " + LITERATURE_ISBN_LENGTH + " characters. " +
                    "Received: " + ISBN.length());
        this.ISBN = ISBN;
    }
}