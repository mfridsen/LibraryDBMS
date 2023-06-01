package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.*;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.NullEntityException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 * <p>
 * This class contains database CRUD operation methods as well as other methods related to the Item entity class.
 * It contains a list of all Item titles for quicker validation.
 * <p>
 * Note on Exceptions:
 * <p>
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 * <p>
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in ItemCreateGUI. When I press the "Create Item" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 * <p>
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 * <p>
 * Meaning, createNewItem (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
 */
public class ItemHandler
{

    /**
     * Used to speed up searching. Contains a HashMap with the titles of all Items in the database and how many
     * copies there are of each.
     */
    private static final Map<String, Integer> storedTitles = new HashMap<>();

    /**
     * Used to keep track of how many available copies there are of Items in the database.
     */
    private static final Map<String, Integer> availableTitles = new HashMap<>();

    /**
     * Used to keep track of already existing barcodes to quickly enforce uniqueness. //TODO-PRIO TEST
     */
    private static final ArrayList<String> registeredBarcodes = new ArrayList<>();

    /**
     * Returns the storedTitles map.
     *
     * @return the storedTitles map.
     */
    public static Map<String, Integer> getStoredTitles()
    {
        return storedTitles;
    }

    /**
     * Returns the availableTitles map.
     *
     * @return the availableTitles map.
     */
    public static Map<String, Integer> getAvailableTitles()
    {
        return availableTitles;
    }

    /**
     * Returns the registeredBarcodes list.
     *
     * @return the registeredBarcodes list. //TODO-PRIO TEST
     */
    public static ArrayList<String> getRegisteredBarcodes()
    {
        return registeredBarcodes;
    }

    /**
     * Increments the count of a title in both the stored and available titles maps.
     *
     * @param title The title to be incremented.
     */
    public static void incrementBothTitles(String title)
    {
        incrementStoredTitles(title);
        incrementAvailableTitles(title);
    }

    /**
     * Increments the count of a title in both the stored and available titles maps.
     *
     * @param title The title to be incremented.
     */
    public static void incrementStoredTitles(String title)
    {
        storedTitles.put(title, storedTitles.getOrDefault(title, 0) + 1);
        if (!availableTitles.containsKey(title))
        {
            availableTitles.put(title, 0);
        }
    }

    /**
     * Increments the count of a title in the available titles map. If the title is not present in the map,
     * it is added with a count of 1.
     *
     * @param title The title to be incremented.
     */
    public static void incrementAvailableTitles(String title)
    {
        availableTitles.put(title, availableTitles.getOrDefault(title, 0) + 1);
    }


    /**
     * Decrements the count of a title in the stored titles map. If the count reaches 0, the title is removed from both stored and available titles maps.
     *
     * @param title The title to be decremented.
     */
    public static void decrementStoredTitles(String title)
    {
        int storedCount = storedTitles.get(title) - 1;
        if (storedCount == 0)
        {
            storedTitles.remove(title);
            availableTitles.remove(title);
        }
        else
        {
            storedTitles.put(title, storedCount);
        }
    }

    /**
     * Decrements the count of a title in the available titles map, if it exists. If the count reaches 0 or less, the title is removed from the map.
     *
     * @param title The title to be decremented.
     */
    public static void decrementAvailableTitles(String title)
    {
        Integer count = availableTitles.get(title);
        if (count != null && count > 0)
        {
            availableTitles.put(title, count - 1);
        }
        else
        {
            availableTitles.put(title, 0); // keep the item in the map with a count of 0
        }
    }

    /**
     * Increments the count of registered barcodes by adding the specified barcode.
     *
     * @param barcode The barcode to be added.
     */
    public static void incrementRegisteredBarcodes(String barcode)
    {
        registeredBarcodes.add(barcode);
    }

    /**
     * Decrements the count of registered barcodes by removing the specified barcode.
     *
     * @param barcode The barcode to be removed.
     */
    public static void decrementRegisteredBarcodes(String barcode)
    {
        registeredBarcodes.remove(barcode);
    }

    /**
     * Prepares the handler by syncing titles from the database. Called at the start of the application.
     */
    public static void setup()
    {
        syncTitlesAndBarcodes();
    }

    /**
     * Syncs the handler with the database by clearing existing data and retrieving current titles and barcodes from
     * the database.
     * <p>
     * Can be called if something's gone wrong and data needs to be re-synced during runtime.
     */
    public static void syncTitlesAndBarcodes()
    {
        reset();
        retrieveTitlesAndBarcodesFromTable();
    }

    /**
     * Retrieves titles from the Items table and returns them as a map with title-count pairs.
     */
    private static void retrieveTitlesAndBarcodesFromTable() //TODO-PRIO RE-TEST AGAINST RETRIEVAL FROM TEST_DATA FILE
    {
        try (QueryResult result = DatabaseHandler.executeQuery("SELECT title, available, barcode FROM items " +
                "ORDER BY " + "title ASC"))
        {
            while (result.getResultSet().next())
            {
                //Retrieve data
                String title = result.getResultSet().getString("title");
                boolean available = result.getResultSet().getBoolean("available");
                String barcode = result.getResultSet().getString("barcode");

                //Increment titles
                incrementStoredTitles(title);
                if (available)
                {
                    incrementAvailableTitles(title);
                }

                //Increment barcodes
                incrementRegisteredBarcodes(barcode);
            }
        }
        catch (SQLException e) //This is fatal
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve titles and barcodes from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        finally
        {
            if (storedTitles.isEmpty()) System.err.println("No titles retrieved from table!");
            if (registeredBarcodes.isEmpty()) System.err.println("No barcodes retrieved from table!");
        }
    }

    /**
     * Clears both stored and available titles maps as well as the registered barcodes list.
     */
    public static void reset()
    {
        storedTitles.clear();
        availableTitles.clear();
        registeredBarcodes.clear();
    }

    /**
     * Prints the stored titles and their counts.
     */
    public static void printTitles()
    {
        System.out.println("\nTitles:");
        storedTitles.forEach((title, count) -> System.out.println("Title: " + title + " Copies: " + count));
    }

    /**
     * Prints the list of Items with their IDs and titles.
     *
     * @param itemList the list of Items to print
     */
    public static void printItemList(List<Item> itemList)
    {
        System.out.println("Items:");
        int count = 1;
        for (Item item : itemList)
        {
            System.out.println(count + " itemID: " + item.getItemID() + ", title: " + item.getTitle());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    public static Literature createNewLiterature(String title, Item.ItemType type, int authorID, int classificationID,
                                           String barcode, String ISBN)
    throws InvalidBarcodeException, InvalidIDException, EntityNotFoundException
    {
        //Validate input, throws InvalidBarcodeException
        checkBarcode(barcode);
        Literature newLiterature = null;

        try
        {
            //TODO-PRIO //Set authorName and classificationName by retrieving from their handlers
            //Retrieve author
            Author author = getExistingAuthor(authorID); //Throws EntityNotFoundException

            //Retrieve classification
            Classification classification = getExistingClassification(classificationID);


            //Create literature object and set authorName and classificationName by retrieving from their handlers
            newLiterature = new Literature(title, type, authorID, classificationID, barcode, ISBN);

            //Retrieve and set itemID,
            int itemID = saveItem(newLiterature);
            newLiterature.setItemID(itemID);

            //Save literature
            saveLiterature(newLiterature);

            //Increment the count of the new title. Add a new entry if the title does not exist yet.
            incrementBothTitles(title);
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
        }

        return newLiterature;
    }

    private static Classification getExistingClassification(int classificationID)
    throws EntityNotFoundException
    {
        Classification classification = ClassificationHandler.getClassificationByID(classificationID);
        if (classification == null)
            throw new EntityNotFoundException("Classification with ID " + classificationID + " not found.");
        return classification;
    }


    private static void checkBarcode(String barcode)
    throws InvalidBarcodeException
    {
        if (registeredBarcodes.contains(barcode))
            throw new InvalidBarcodeException("Barcode " + barcode + " is already registered.");
    }

    private static Author getExistingAuthor(int authorID)
    throws EntityNotFoundException
    {
        Author author = AuthorHandler.getAuthorByID(authorID);
        if (author == null)
            throw new EntityNotFoundException("Author with ID " + authorID + "not found.");
        return author;
    }



    public static Film createNewFilm(String title, int authorID, int classificationID,
                                     String barcode, int ageRating)
    throws InvalidBarcodeException, ConstructionException, InvalidIDException
    {
        //Validate input, throws InvalidBarcodeException
        checkBarcode(barcode);

        //TODO-PRIO //Set authorName and classificationName by retrieving from their handlers


        //Create film object and set authorName and classificationName by retrieving from their handlers
        Film newFilm = new Film(title, authorID, classificationID, barcode, ageRating);

        //Retrieve and set itemID,
        int itemID = saveItem(newFilm);
        newFilm.setItemID(itemID);

        //Save film
        saveFilm(newFilm);

        //Increment the count of the new title. Add a new entry if the title does not exist yet.
        incrementBothTitles(title);

        return newFilm;
    }

    /**
     * Saves an item to the database, retrieving and returning the generated item ID.
     *
     * @param item The Item object to be saved.
     * @return The generated ID of the saved item.
     */
    private static int saveItem(Item item)
    {
        try
        {
            // Prepare query
            String query = "INSERT INTO items (title, itemType, barcode, authorID, classificationID, " +
                    "allowedRentalDays, available, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String[] params = {
                    item.getTitle(),
                    item.getType().toString(),
                    item.getBarcode(),
                    String.valueOf(item.getAuthorID()),
                    String.valueOf(item.getClassificationID()),
                    String.valueOf(item.getAllowedRentalDays()),
                    item.isAvailable() ? "1" : "0", // If boolean is true, add the string "1", if false, "0"
                    item.isDeleted() ? "1" : "0"
            };

            // Execute query and get the generated itemID
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params,
                    Statement.RETURN_GENERATED_KEYS))
            {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next())
                {
                    return generatedKeys.getInt(1);
                }
            }
        }
        catch (SQLException e) // Fatal
        {
            ExceptionHandler.HandleFatalException("Failed to save Item to database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Won't reach, needed for compilation
        return 0;
    }

    private static void saveLiterature(Literature literature)
    {
        // Save to literature table
        String query = "INSERT INTO literature (literatureID, ISBN) VALUES (?, ?)";
        DatabaseHandler.executePreparedQuery(query,
                new String[]{
                        String.valueOf(literature.getItemID()),
                        literature.getISBN()});

        // Save to literature_item join table
        String joinQuery = "INSERT INTO literature_item (literatureID, itemID) VALUES (?, ?)";
        DatabaseHandler.executePreparedQuery(joinQuery,
                new String[]{
                        String.valueOf(literature.getItemID()),
                        String.valueOf(literature.getItemID())});
    }

    private static void saveFilm(Film film)
    {
        // Save to films table
        String query = "INSERT INTO films (filmID, ageRating, countryOfProduction, actors) VALUES (?, ?, ?, ?)";
        DatabaseHandler.executePreparedQuery(query,
                new String[]{
                        String.valueOf(film.getItemID()),
                        String.valueOf(film.getAgeRating()),
                        film.getCountryOfProduction(),
                        film.getListOfActors()});

        // Save to film_item join table
        String joinQuery = "INSERT INTO film_item (filmID, itemID) VALUES (?, ?)";
        DatabaseHandler.executePreparedQuery(joinQuery,
                new String[]{String.valueOf(film.getItemID()), String.valueOf(film.getItemID())});
    }


    /**
     * Retrieves an item by its ID from the database.
     *
     * @param itemID The ID of the item to be retrieved.
     * @return The retrieved Item.
     */
    public static Item getItemByID(int itemID)
    throws InvalidIDException
    {

        // No point getting impossible Items
        checkValidItemID(itemID);

        // Prepare a SQL query to select an item by itemID
        String query = "SELECT * FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        // Execute the query and store the result in a ResultSet
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
        {
            ResultSet resultSet = queryResult.getResultSet();

            // If the ResultSet contains data, create a new Item object using the retrieved title
            // and set the item's itemID
            if (resultSet.next())
            {
                String title = resultSet.getString("title");
                int allowedRentalDays = resultSet.getInt("allowedRentalDays");
                boolean available = resultSet.getBoolean("available");
                boolean deleted = resultSet.getBoolean("deleted");
                //TODO-PRIO FIX THIS
                return null;
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Item by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        // If no Item was found, return null
        return null;
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    /**
     * Updates an existing item in the database and adjusts the count of the old and new titles.
     *
     * @param item The Item object containing the updated information.
     */ //TODO-PRIO UPDATE EXCEPTION AND TESTS
    public static void updateItem(Item item)
    throws NullEntityException, EntityNotFoundException
    {
        try
        {
            //TODO-prio update when Item is finished
            //Validate the input, throws NullEntityException
            checkNullItem(item);

            // Get the old Item instance (which hasn't been updated)
            Item oldItem = getItemByID(item.getItemID());
            if (oldItem == null)
                throw new EntityNotFoundException("Updated failed: could not find Item with ID " + item.getItemID());

            // Get the old title
            String oldTitle = oldItem.getTitle();

            // Get the old availability status
            boolean oldAvailability = oldItem.isAvailable();

            // Prepare a SQL command to update an item's title and availability by itemID.
            String sql = "UPDATE items SET title = ?, available = ? WHERE itemID = ?";
            String[] params = {
                    item.getTitle(),
                    item.isAvailable() ? "1" : "0", //If boolean is true, add the string "1", if false, "0"
                    String.valueOf(item.getItemID())
            };

            // Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);

            // If the title has changed, adjust the counts in the maps
            if (!oldTitle.equals(item.getTitle()))
            {
                decrementStoredTitles(oldTitle);
                incrementStoredTitles(item.getTitle());
            }

            // If the availability status has changed, adjust the counts in the availableTitles map
            if (oldAvailability != item.isAvailable())
            {
                if (oldAvailability)
                {
                    decrementAvailableTitles(oldTitle);
                }
                if (item.isAvailable())
                {
                    incrementAvailableTitles(item.getTitle());
                }
            }
        }
        catch (InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to update Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    public static void deleteItem(Item itemToDelete)
    {

    }

    public static void undoDeleteItem(Item itemToRecover)
    {

    }

    /**
     * Deletes an item from the database and decrements the count of the item's title.
     *
     * @param item The Item object to be deleted.
     */ //TODO-PRIO UPDATE EXCEPTION AND TESTS
    public static void hardDeleteItem(Item item)
    throws NullEntityException, EntityNotFoundException
    {
        try
        {
            //TODO-prio UPDATE TO CHANGE DELETED
            //TODO-prio update when Item is finished
            //Validate the input, NullEntityException
            checkNullItem(item);

            // Get the old title, throws EntityNotFoundException
            Item oldItem = getItemByID(item.getItemID());
            if (oldItem == null)
                throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
            String oldTitle = oldItem.getTitle();

            // Check if the item exists in the database
            String sql = "SELECT COUNT(*) FROM items WHERE itemID = ?";
            String[] params = {String.valueOf(item.getItemID())};

            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
            ResultSet resultSet = queryResult.getResultSet();
            resultSet.next(); // Move to the first row


            //Prepare a SQL command to delete an item by itemID.
            sql = "DELETE FROM items WHERE itemID = ?";
            params = new String[]{String.valueOf(item.getItemID())};

            //Execute the update. //TODO-prio handle cascades in rentals
            DatabaseHandler.executePreparedUpdate(sql, params);

            // Decrement the count of the old title. Remove the entry if the count reaches 0.
            if (storedTitles.get(oldTitle) != null)
            {
                decrementStoredTitles(oldTitle);
                decrementAvailableTitles(oldTitle);
            }
        }
        catch (SQLException | InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to delete Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    /**
     * Retrieves all Items with a given title from the database.
     *
     * @param title The title of the Items to be retrieved.
     * @return A list of Item objects with the provided title.
     */
    public static List<Item> getItemsByTitle(String title)
    throws InvalidTitleException
    {
        List<Item> items = new ArrayList<>();
        try
        {
            // No point getting invalid Items
            checkEmptyTitle(title);

            // Prepare a SQL query to select an item by title
            String query = "SELECT * FROM items WHERE title = ?";
            String[] params = {title};

            // Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();

                // Loop through the ResultSet. For each record, create a new Item object and add it to the list
                while (resultSet.next())
                {
                    //TODO-PRIO FIX THIS
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve items by title due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Can be empty
        return items;
    }


    public static List<Item> getItemsByISBN(String ISBN)
    {
        //Empty ISBN
        //Null ISBN
        //Incorrect format ISBN
        //Item does not exist
        //Item does exist
        //Multiple Items exist
        // == 6 test cases
        return null;
    }

    public static List<Item> getItemsByGenre(String genre)
    {
        //Empty genre
        //Null genre
        //genre does not exist
        //Items don't exist in genre
        //Item does exist
        //Multiple Items exist
        // == 6 test cases
        return null;
    }

    public static List<Item> getItemsByAuthor(String authorName)
    {
        //empty authorName
        //null authorName
        //no such author
        //author exists, but no titles for some reason
        //author exists and has title
        //Multiple Items exist for author
        // == 6 test cases

        return null;
    }

    public static Item getItemsByPublisher(String publisherName)
    {
        //empty publisherName
        //null publisherName
        //no such publisher
        //publisher exists, but no titles for some reason
        //publisher exists and has title
        //Multiple Items exist for publisher
        // == 6 test cases

        return null;
    }

    public static Item getItemsByType(String type)
    { //Not going to be string, but an ENUM instead
        //Invalid enum should not be possible...

        //No Items of such type
        //Single item of type
        //multiple Items of type
        // == 3 test cases

        return null;
    }

    // == 27 test cases


    /**
     * Retrieves the allowed rental days for an item by its ID.
     *
     * @param itemID The ID of the item.
     * @return The allowed rental days for the item.
     * @throws EntityNotFoundException If no item with the provided ID exists in the database.
     * @throws InvalidIDException      If the provided ID is invalid (e.g., negative or zero).
     */
    public static int getAllowedRentalDaysByID(int itemID)
    throws EntityNotFoundException, InvalidIDException
    {
        Item item = getItemByID(itemID);
        if (item != null) return item.getAllowedRentalDays();
        else throw new EntityNotFoundException("Item not found. Item ID: " + itemID);
    }

    /**
     * Retrieves the number of available copies for a specific item.
     *
     * @param item The Item object for which the available copies are to be retrieved.
     * @return The number of available copies for the item.
     * @throws EntityNotFoundException If the item does not exist in the database.
     * @throws NullEntityException     If the Item is null.
     */
    public static int getAvailableCopiesForItem(Item item)
    throws EntityNotFoundException, NullEntityException
    {
        checkNullItem(item);
        if (!availableTitles.containsKey(item.getTitle()) && !storedTitles.containsKey(item.getTitle()))
            throw new EntityNotFoundException(item.getTitle() + ": Item not found in stored or available titles.");
        return availableTitles.get(item.getTitle());
    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    /**
     * This method is used to retrieve the title of an item by its ID from the database. It executes a prepared
     * SQL query to fetch the title of the item corresponding to the provided itemID. If the item exists, the title
     * is returned. If the item does not exist, an EntityNotFoundException is thrown. In the case of a SQLException,
     * the exception is handled and the method returns null.
     *
     * @param itemID The ID of the item whose title is to be fetched.
     * @return The title of the item if it exists, null if an SQLException occurs.
     */
    private static String getItemTitleByID(int itemID)
    {
        String sql = "SELECT title FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        try
        {
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
            try (queryResult)
            {
                if (queryResult.getResultSet().next())
                    return queryResult.getResultSet().getString("title");
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to get Item title by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //If no item title was found
        return null;
    }

    /**
     * Checks whether a given title is null or empty. If so, throws an InvalidTitleException,
     * which must be handled.
     *
     * @param title the title to check.
     * @throws InvalidTitleException if title is null or empty.
     */
    private static void checkEmptyTitle(String title)
    throws InvalidTitleException
    {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Empty title.");
    }

    /**
     * Checks whether a given itemID is invalid (<= 0). If so, throws an InvalidIDException,
     * which must be handled.
     *
     * @param itemID the ID to check.
     * @throws InvalidIDException if itemID <= 0.
     */
    private static void checkValidItemID(int itemID)
    throws InvalidIDException
    {
        if (itemID <= 0)
            throw new InvalidIDException("Error retrieving item by itemID: invalid itemID " + itemID);
    }

    /**
     * Checks whether a given Item is null. If so, throws a NullEntityException which must be handled.
     *
     * @param item the item to check.
     * @throws NullEntityException if item is null.
     */
    private static void checkNullItem(Item item)
    throws NullEntityException
    {
        if (item == null)
            throw new NullEntityException("Invalid item: item is null.");
    }
}