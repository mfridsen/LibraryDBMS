package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidItemTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 * <p>
 * Represents an Item in a library system. This abstract class forms the basis for different types of library Items.
 * It provides all the common fields and methods needed to handle a library Item, regardless of its specific type
 * (like book or film).
 * An Item has a title, type, barcode, author ID, classification ID, author name, classification name,
 * allowed rental days, and an availability status. These fields can be retrieved and (where applicable) set through
 * the provided getters and setters.
 * <p>
 * The Item class also provides constructors to create a new Item, to create an Item with specific data,
 * and to create a copy of an existing Item.
 * <p>
 * Items are uniquely identified by their barcode within the library system, which is enforced at construction.
 * <p>
 * The class utilizes the ItemType enum for type specification.
 * <p>
 * The maximum length of an Item's title and the length of the Item's barcode are retrieved from the database
 * upon class initialization to keep the constraints consistent across the system.
 * <p>
 * Furthermore, the class provides a static method to get the default allowed rental days based on the ItemType.
 * <p>
 * Exceptions are used to handle errors in construction and setting of fields, ensuring the integrity of the Item data.
 */
public abstract class Item extends Entity
{
    /**
     * Maximum allowed length of an item's title. The value is retrieved from the database metadata to
     * maintain consistency.
     */
    public static final int ITEM_TITLE_MAX_LENGTH;
    /**
     * The exact required length of an item's barcode. The value is retrieved from the database metadata to
     * maintain consistency.
     */
    public static final int ITEM_BARCODE_LENGTH;

    /*
      Static initializer block that retrieves item metadata from the database and sets ITEM_TITLE_MAX_LENGTH
      and ITEM_BARCODE_LENGTH accordingly.
     */
    static
    {
        int[] metaData = DatabaseHandler.getItemMetaData();
        ITEM_TITLE_MAX_LENGTH = metaData[0];
        ITEM_BARCODE_LENGTH = metaData[1];
    }

    /**
     * Primary key of the item in the database.
     */
    protected int itemID;
    /**
     * Title of the item.
     */
    protected String title;
    /**
     * Type of the item, as defined in the ItemType enumeration.
     */
    protected ItemType type;
    /**
     * Unique identifier for the item, represented as a barcode.
     */
    protected String barcode;
    /**
     * Foreign key reference to the ID of the author or director associated with the item.
     */
    protected int authorID;
    /**
     * Foreign key reference to the ID of the item's classification.
     */
    protected int classificationID;
    /**
     * Name of the author or director associated with the item. This field is not persisted in the database.
     */
    protected String authorFirstname;
    /**
     * Last name of the author or director associated with the item. This field is not persisted in the database.
     */
    protected String authorLastname;
    /**
     * Name of the item's classification. This field is not persisted in the database.
     */
    protected String classificationName;
    /**
     * Number of days this item is allowed to be rented.
     */
    protected int allowedRentalDays;
    /**
     * Availability status of the item. True if the item is available for rental, false otherwise.
     */
    protected boolean available; //True by default //TODO-prio double check availability on delete

    /**
     * Constructs a new Item object using provided data. The ItemID is not set during construction and should be
     * set separately after insertion into the database. This constructor is used when creating a new item for
     * the database.
     *
     * @param title            Title of the item.
     * @param type             Type of the item.
     * @param barcode          Unique barcode identifier of the item.
     * @param authorID         Identifier for the author or director of the item.
     * @param classificationID Identifier for the classification of the item.
     * @throws ConstructionException If any of the validation checks for the input data fail.
     */
    public Item(String title, ItemType type, String barcode, int authorID, int classificationID)
    throws ConstructionException
    {
        super(); //deleted
        try
        {
            this.itemID = 0; //Set after initial INSERT by createNewItem
            setTitle(title); //Throws InvalidTitleException
            setType(type);
            setBarcode(barcode); //Throws InvalidBarcodeException
            setAuthorID(authorID); //Throws InvalidIDException
            setClassificationID(classificationID); //Throws InvalidIDException
            this.authorFirstname = null; //Retrieved after creation
            this.authorLastname = null; //Retrieved after creation
            this.classificationName = null; //Retrieved after creation
            setAllowedRentalDays(getDefaultAllowedRentalDays(type)); //Throws InvalidDateException
            this.available = true;
        }
        catch (InvalidTitleException | InvalidIDException | InvalidDateException | InvalidBarcodeException
               | InvalidItemTypeException e)
        {
            throw new ConstructionException("Failed to construct Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //TODO remember the protected boolean deleted inherited from Entity

    /**
     * Constructs an Item object using the provided data. This constructor is typically used when retrieving an item
     * from the database, as it includes all fields that are stored in the database, including the item's database ID.
     *
     * @param deleted            Boolean indicating if the item has been deleted.
     * @param itemID             Identifier for the item.
     * @param title              Title of the item.
     * @param type               Type of the item.
     * @param barcode            Unique barcode identifier of the item.
     * @param authorID           Identifier for the author or director of the item.
     * @param classificationID   Identifier for the classification of the item.
     * @param authorFirstname    Name of the author or director of the item.
     * @param authorLastname     Last name of the author or director of the item.
     * @param classificationName Name of the classification of the item.
     * @param allowedRentalDays  Number of days the item is allowed to be rented for.
     * @param available          Boolean indicating the availability of the item.
     * @throws ConstructionException If any of the validation checks for the input data fail.
     */
    public Item(boolean deleted, int itemID, String title, ItemType type, String barcode, int authorID,
                int classificationID, String authorFirstname, String authorLastname, String classificationName,
                int allowedRentalDays,
                boolean available)
    throws ConstructionException
    {
        super(deleted);
        try
        {
            setItemID(itemID); //Throws InvalidIDException
            setTitle(title); //Throws InvalidTitleException
            setType(type);
            setBarcode(barcode); //Throws InvalidBarcodeException
            setAuthorID(authorID); //Throws InvalidIDException
            setClassificationID(classificationID); //Throws InvalidIDException
            setAuthorFirstname(authorFirstname);
            setAuthorLastname(authorLastname);
            setClassificationName(classificationName);
            setAllowedRentalDays(allowedRentalDays); //Throws InvalidDateException
            this.available = available;
        }
        catch (InvalidIDException | InvalidTitleException | InvalidDateException
               | InvalidBarcodeException | InvalidItemTypeException e)
        {
            throw new ConstructionException("Failed to construct Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Copy constructor for the Item class. Creates a new Item object that is an exact copy of the provided Item.
     *
     * @param other The Item object to be copied.
     */
    public Item(Item other)
    {
        super(other); //deleted
        this.itemID = other.itemID;
        this.title = other.title;
        this.type = other.type;
        this.barcode = other.barcode;
        this.authorID = other.authorID;
        this.classificationID = other.classificationID;
        this.authorFirstname = other.authorFirstname;
        this.authorLastname = other.authorLastname;
        this.classificationName = other.classificationName;
        this.allowedRentalDays = other.allowedRentalDays;
        this.available = other.available;
    }

    /**
     * Retrieves the default number of rental days for a given ItemType.
     *
     * @param type The ItemType for which to get the default rental days.
     * @return An int representing the default number of rental days.
     */
    public static int getDefaultAllowedRentalDays(ItemType type)
    {
        return switch (type)
                {
                    case REFERENCE_LITERATURE, MAGAZINE -> 0;
                    case FILM -> 7;
                    case COURSE_LITERATURE -> 14;
                    case OTHER_BOOKS -> 28;
                };
    }

    /**
     * Gets the ID of this Item.
     *
     * @return The ID of this Item.
     */
    public int getItemID()
    {
        return itemID;
    }

    /**
     * Sets the ID of this Item.
     *
     * @param itemID The ID to set for this Item.
     * @throws InvalidIDException If the provided ID is not greater than 0.
     */
    public void setItemID(int itemID)
    throws InvalidIDException
    {
        if (itemID <= 0)
            throw new InvalidIDException("Item ID must be greater than 0. Received: " + itemID);
        this.itemID = itemID;
    }

    /**
     * Gets the title of this Item.
     *
     * @return The title of this Item.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the title of this Item.
     *
     * @param title The title to set for this Item.
     * @throws InvalidTitleException If the provided title is null, empty, or longer than the maximum allowed length.
     */
    public void setTitle(String title)
    throws InvalidTitleException
    {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Title cannot be null or empty.");
        if (title.length() > ITEM_TITLE_MAX_LENGTH)
            throw new InvalidTitleException("Title cannot be longer than " +
                    ITEM_TITLE_MAX_LENGTH + " characters. Received: " + title.length());
        this.title = title;
    }

    /**
     * Gets the ItemType of this Item.
     *
     * @return The ItemType of this Item.
     */
    public ItemType getType()
    {
        return type;
    }

    /**
     * Sets the ItemType of this Item.
     *
     * @param type The ItemType to set for this Item.
     */
    public void setType(ItemType type)
    throws InvalidItemTypeException
    {
        if (type == null)
            throw new InvalidItemTypeException("Item type cannot be null.");
        this.type = type;
    }

    /**
     * Gets the barcode of this Item.
     *
     * @return The barcode of this Item.
     */
    public String getBarcode()
    {
        return barcode;
    }

    /**
     * Sets the barcode of this Item.
     *
     * @param barcode The barcode to set for this Item.
     * @throws InvalidBarcodeException If the provided barcode is null, empty, or longer than the
     *                                 maximum allowed length.
     */
    public void setBarcode(String barcode)
    throws InvalidBarcodeException
    {
        if (barcode == null || barcode.isEmpty())
            throw new InvalidBarcodeException("Item barcode cannot be null or empty.");
        if (barcode.length() > ITEM_BARCODE_LENGTH)
            throw new InvalidBarcodeException("Item barcode length cannot be greater than " +
                    ITEM_BARCODE_LENGTH + " characters. Received: " + barcode.length());
        this.barcode = barcode;
    }

    /**
     * Gets the author ID of this Item.
     *
     * @return The author ID of this Item.
     */
    public int getAuthorID()
    {
        return authorID;
    }

    /**
     * Sets the author ID of this Item.
     *
     * @param authorID The author ID to set for this Item.
     * @throws InvalidIDException If the provided author ID is not greater than 0.
     */
    public void setAuthorID(int authorID)
    throws InvalidIDException
    {
        if (authorID <= 0)
            throw new InvalidIDException("Author ID must be greater than 0. Received: " + authorID);
        this.authorID = authorID;
    }

    /**
     * Gets the classification ID of this Item.
     *
     * @return The classification ID of this Item.
     */
    public int getClassificationID()
    {
        return classificationID;
    }

    /**
     * Sets the classification ID of this Item.
     *
     * @param classificationID The classification ID to set for this Item.
     * @throws InvalidIDException If the provided classification ID is not greater than 0.
     */
    public void setClassificationID(int classificationID)
    throws InvalidIDException
    {
        if (classificationID <= 0)
            throw new InvalidIDException("Classification ID must be greater than 0. Received: " + classificationID);
        this.classificationID = classificationID;
    }

    /**
     * Gets the author first name of this Item.
     *
     * @return The author name of this Item.
     */
    public String getAuthorFirstname()
    {
        return authorFirstname;
    }

    /**
     * Sets the author first name of this Item.
     *
     * @param authorFirstname The author name to set for this Item.
     */
    public void setAuthorFirstname(String authorFirstname)
    {
        this.authorFirstname = authorFirstname; //Validation is the responsibility of the Author class
    }

    /**
     * Gets the author last name of this Item.
     *
     * @return The author last name of this Item.
     */
    public String getAuthorLastname()
    {
        return authorLastname;
    }

    /**
     * Sets the author last name of this Item.
     *
     * @param authorLastname The author name to set for this Item.
     */
    public void setAuthorLastname(String authorLastname)
    {
        this.authorLastname = authorLastname; //Validation is the responsibility of the Author class
    }

    /**
     * Gets the classification name of this Item.
     *
     * @return The classification name of this Item.
     */
    public String getClassificationName()
    {
        return classificationName;
    }

    /**
     * Sets the classification name of this Item.
     *
     * @param classificationName The classification name to set for this Item.
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName; //Validation is the responsibility of the Classification class
    }

    /**
     * Gets the number of days this Item is allowed to be rented.
     *
     * @return The number of days this Item is allowed to be rented.
     */
    public int getAllowedRentalDays()
    {
        return allowedRentalDays;
    }

    /**
     * Sets the number of days this Item is allowed to be rented.
     *
     * @param allowedRentalDays The number of days to set for this Item.
     * @throws InvalidDateException If the provided number of days is less than 0.
     */
    public void setAllowedRentalDays(int allowedRentalDays)
    throws InvalidDateException
    {
        if (allowedRentalDays < 0)
            throw new InvalidDateException("Allowed rental days can't be negative. Received: " + allowedRentalDays);
        this.allowedRentalDays = allowedRentalDays;
    }

    /**
     * Checks if this Item is available.
     *
     * @return True if this Item is available, false otherwise.
     */
    public boolean isAvailable()
    {
        return available;
    }

    /**
     * Sets the availability of this Item.
     *
     * @param available The availability to set for this Item.
     */
    public void setAvailable(boolean available)
    {
        this.available = available;
    }

    /**
     * An enumeration of different types of items available in the library.
     * Each item type can be one of the following: REFERENCE_LITERATURE, MAGAZINE, FILM, COURSE_LITERATURE, OTHER_BOOKS
     */
    public enum ItemType
    {
        REFERENCE_LITERATURE,
        MAGAZINE,
        FILM,
        COURSE_LITERATURE,
        OTHER_BOOKS
    }
}