package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.*;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidBarcodeException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidItemTypeException;
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
            availableTitles.put(title, 0); //keep the item in the map with a count of 0
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
        //Execute the query to retrieve data
        String query = "SELECT title, available, barcode FROM items ORDER BY " + "title ASC";
        try (QueryResult result = DatabaseHandler.executeQuery(query))
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

    /**
     * Creates a new Literature object, validates its parameters, saves it to the database, sets its ItemID,
     * sets its author and classification names, and increments the count of the new title.
     *
     * @param title            The title of the literature.
     * @param type             The type of the item.
     * @param authorID         The ID of the author.
     * @param classificationID The ID of the classification.
     * @param barcode          The barcode of the literature.
     * @param ISBN             The ISBN of the literature.
     * @return The newly created Literature object.
     * @throws InvalidBarcodeException If the provided barcode is already registered.
     * @throws InvalidIDException      If either the provided authorID or classificationID is invalid.
     * @throws EntityNotFoundException If the provided authorID or classificationID does not correspond to an existing author or classification.
     * @throws ConstructionException   If there is an error during the construction of the Literature object.
     */
    public static Literature createNewLiterature(String title, Item.ItemType type, int authorID, int classificationID,
                                                 String barcode, String ISBN)
    throws InvalidBarcodeException, InvalidIDException, EntityNotFoundException, ConstructionException
    {
        //Validate input
        if (barcodeTaken(barcode))
            throw new InvalidBarcodeException("Barcode " + barcode + " is already registered.");
        if (invalidID(authorID))
            throw new InvalidIDException("Invalid authorID: " + authorID);
        if (invalidID(classificationID))
            throw new InvalidIDException("Invalid classificationID: " + classificationID);

        //Retrieve author, Throws EntityNotFoundException
        Author author = getExistingAuthor(authorID);

        //Retrieve classification, Throws EntityNotFoundException
        Classification classification = getExistingClassification(classificationID);

        //Create literature object and set authorName and classificationName by retrieving from their handlers
        Literature newLiterature = new Literature(title, type, authorID, classificationID, barcode, ISBN);

        //Save, retrieve and set itemID,
        int itemID = saveItem(newLiterature);
        newLiterature.setItemID(itemID); //Throws InvalidIDException

        //Save literature
        saveLiterature(newLiterature);

        //Set author and classification names
        newLiterature.setAuthorName(author.getAuthorFirstname() + " " + author.getAuthorLastName());
        newLiterature.setClassificationName(classification.getClassificationName());

        //Increment the count of the new title. Add a new entry if the title does not exist yet.
        incrementBothTitles(title);

        //Increment registered barcode
        incrementRegisteredBarcodes(barcode);

        return newLiterature;
    }

    /**
     * Creates a new Film object, validates its parameters, saves it to the database, sets its ItemID,
     * sets its author and classification names, and increments the count of the new title.
     *
     * @param title            The title of the film.
     * @param authorID         The ID of the author.
     * @param classificationID The ID of the classification.
     * @param barcode          The barcode of the film.
     * @param ageRating        The age rating of the film.
     * @return The newly created Film object.
     * @throws InvalidBarcodeException If the provided barcode is already registered.
     * @throws InvalidIDException      If either the provided authorID or classificationID is invalid.
     * @throws EntityNotFoundException If the provided authorID or classificationID does not correspond to an existing author or classification.
     * @throws ConstructionException   If there is an error during the construction of the Film object.
     */
    public static Film createNewFilm(String title, int authorID, int classificationID,
                                     String barcode, int ageRating)
    throws InvalidBarcodeException, InvalidIDException, EntityNotFoundException, ConstructionException
    {
        //Validate input
        if (barcodeTaken(barcode))
            throw new InvalidBarcodeException("Barcode " + barcode + " is already registered.");
        if (invalidID(authorID))
            throw new InvalidIDException("Invalid authorID: " + authorID);
        if (invalidID(classificationID))
            throw new InvalidIDException("Invalid classificationID: " + classificationID);

        //Retrieve author, Throws EntityNotFoundException
        Author author = getExistingAuthor(authorID);

        //Retrieve classification, Throws EntityNotFoundException
        Classification classification = getExistingClassification(classificationID);

        //Create film object and set authorName and classificationName by retrieving from their handlers
        Film newFilm = new Film(title, authorID, classificationID, barcode, ageRating); //Throws ConstructionException

        //Save, retrieve and set itemID,
        int itemID = saveItem(newFilm);
        newFilm.setItemID(itemID); //Throws InvalidIDException

        //Save the new film to the table
        saveFilm(newFilm);

        //Set author and classification names
        newFilm.setAuthorName(author.getAuthorFirstname() + " " + author.getAuthorLastName());
        newFilm.setClassificationName(classification.getClassificationName());

        //Increment the count of the new title. Add a new entry if the title does not exist yet.
        incrementBothTitles(title);

        //Increment registered barcode
        incrementRegisteredBarcodes(barcode);

        return newFilm;
    }

    /**
     * Saves an Item object to the database and returns the automatically generated item ID.
     * This method saves common attributes of all items in the library to the 'items' table,
     * such as title, item type, barcode, author ID, classification ID, allowed rental days,
     * availability status, and deletion status.
     * <p>
     * If a SQLException occurs during the operation, the method will throw a fatal exception
     * that is handled by the ExceptionHandler.
     * <p>
     * Note: The returned value of 0 is a fallback and should never be reached in normal circumstances
     * as any SQLException will halt the execution of the program.
     *
     * @param item The Item object to be saved.
     * @return The automatically generated ID of the saved item.
     */
    private static int saveItem(Item item)
    {
        try
        {
            //Prepare query
            String query = "INSERT INTO items (title, itemType, barcode, authorID, classificationID, " +
                    "allowedRentalDays, available, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String[] params = {
                    item.getTitle(),
                    item.getType().toString(),
                    item.getBarcode(),
                    String.valueOf(item.getAuthorID()),
                    String.valueOf(item.getClassificationID()),
                    String.valueOf(item.getAllowedRentalDays()),
                    item.isAvailable() ? "1" : "0", //If boolean is true, add the string "1", if false, "0"
                    item.isDeleted() ? "1" : "0" //If boolean is true, add the string "1", if false, "0"
            };

            //Execute query and get the generated itemID
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
        catch (SQLException e) //Fatal
        {
            ExceptionHandler.HandleFatalException("Failed to save Item to database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Won't reach, needed for compilation
        return 0;
    }

    /**
     * Saves a Literature object to the literature table in the database.
     *
     * @param literature The Literature object to be saved.
     */
    private static void saveLiterature(Literature literature)
    {
        //Save to literature table
        String query = "INSERT INTO literature (literatureID, ISBN) VALUES (?, ?)";
        DatabaseHandler.executePreparedQuery(query,
                new String[]{
                        String.valueOf(literature.getItemID()),
                        literature.getISBN()});
    }

    private static void saveFilm(Film film)
    {
        // Check if countryOfProduction or listOfActors are null
        String countryOfProduction = film.getCountryOfProduction() == null ? null : film.getCountryOfProduction();
        String listOfActors = film.getListOfActors() == null ? null : film.getListOfActors();

        //Save to films table
        String query = "INSERT INTO films (filmID, ageRating, countryOfProduction, actors) VALUES (?, ?, ?, ?)";
        DatabaseHandler.executePreparedQuery(query,
                new String[]{
                        String.valueOf(film.getItemID()),
                        String.valueOf(film.getAgeRating()),
                        countryOfProduction,
                        listOfActors});
    }

    /**
     * Retrieves an item by its ID from the database.
     *
     * @param itemID The ID of the item to be retrieved.
     * @return The retrieved Item or null if not found.
     */
    public static Item getItemByID(int itemID)
    throws InvalidIDException
    {
        // Validate the provided ID
        checkValidItemID(itemID);

        // Prepare a SQL query to select an item by itemID
        String query = "SELECT * FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
        {
            ResultSet resultSet = queryResult.getResultSet();

            if (resultSet.next())
            {
                Item.ItemType itemType = Item.ItemType.valueOf(resultSet.getString("itemType"));

                // Based on the itemType, call the appropriate private method to retrieve the item
                return switch (itemType)
                        {
                            case REFERENCE_LITERATURE, MAGAZINE, COURSE_LITERATURE, OTHER_BOOKS -> getLiteratureByID(
                                    itemID);
                            case FILM -> getFilmByID(itemID);
                        };
            }
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Item by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        // If no Item was found, return null
        return null;
    }

    /**
     * Retrieves a Literature object from the database based on the given item ID.
     *
     * @param itemID The ID of the item to retrieve.
     * @return The Literature object retrieved from the database, or null if not found.
     * @throws SQLException          If an SQL exception occurs.
     * @throws ConstructionException If there is an error constructing the Literature object.
     */
    private static Literature getLiteratureByID(int itemID)
    throws SQLException, ConstructionException
    {
        String literatureQuery = "SELECT items.*, literature.ISBN FROM items " +
                "INNER JOIN literature ON items.itemID = literature.literatureID " +
                "WHERE items.itemID = ?";
        String[] literatureParams = {String.valueOf(itemID)};

        try (QueryResult literatureQueryResult = DatabaseHandler.executePreparedQuery(literatureQuery,
                literatureParams))
        {
            ResultSet resultSet = literatureQueryResult.getResultSet();
            if (resultSet.next())
            {
                return new Literature(
                        resultSet.getBoolean("deleted"),
                        itemID,
                        resultSet.getString("title"),
                        Item.ItemType.valueOf(resultSet.getString("itemType")),
                        resultSet.getString("barcode"),
                        resultSet.getInt("authorID"),
                        resultSet.getInt("classificationID"),
                        AuthorHandler.getAuthorByID(resultSet.getInt("authorID")).
                                getAuthorFirstname() + " " + AuthorHandler.getAuthorByID(resultSet.
                                getInt("authorID")).getAuthorLastName(),
                        ClassificationHandler.getClassificationByID(resultSet.
                                getInt("classificationID")).getClassificationName(),
                        resultSet.getInt("allowedRentalDays"),
                        resultSet.getBoolean("available"),
                        resultSet.getString("ISBN")
                );
            }
        }

        // If no Literature was found, return null
        return null;
    }

    /**
     * Retrieves a Film object from the database based on the given item ID.
     *
     * @param itemID The ID of the item to retrieve.
     * @return The Film object retrieved from the database, or null if not found.
     * @throws SQLException          If an SQL exception occurs.
     * @throws ConstructionException If there is an error constructing the Film object.
     */
    private static Film getFilmByID(int itemID)
    throws SQLException, ConstructionException
    {
        String filmQuery = "SELECT items.*, films.ageRating, films.countryOfProduction, films.actors FROM items " +
                "INNER JOIN films ON items.itemID = films.filmID " +
                "WHERE items.itemID = ?";
        String[] filmParams = {String.valueOf(itemID)};

        try (QueryResult filmQueryResult = DatabaseHandler.executePreparedQuery(filmQuery, filmParams))
        {
            ResultSet resultSet = filmQueryResult.getResultSet();

            if (resultSet.next())
            {
                return new Film(
                        resultSet.getBoolean("deleted"),
                        itemID,
                        resultSet.getString("title"),
                        Item.ItemType.valueOf(resultSet.getString("itemType")),
                        resultSet.getString("barcode"),
                        resultSet.getInt("authorID"),
                        resultSet.getInt("classificationID"),
                        AuthorHandler.
                                getAuthorByID(resultSet.getInt("authorID")).getAuthorFirstname() + " " +
                                AuthorHandler.
                                        getAuthorByID(resultSet.getInt("authorID")).getAuthorLastName(),
                        ClassificationHandler.
                                getClassificationByID(resultSet.getInt("classificationID")).
                                getClassificationName(),
                        resultSet.getInt("allowedRentalDays"),
                        resultSet.getBoolean("available"),
                        resultSet.getInt("ageRating"),
                        resultSet.getString("countryOfProduction"),
                        resultSet.getString("actors")
                );
            }
        }

        // If no Film was found, return null
        return null;
    }


    //UPDATE -----------------------------------------------------------------------------------------------------------

    /**
     * Updates an item in the database with the provided item object.
     *
     * @param item The item object containing the updated information.
     * @throws NullEntityException     If the item is null.
     * @throws EntityNotFoundException If the item with the specified ID is not found in the database.
     */
    public static void updateItem(Item item)
    throws NullEntityException, EntityNotFoundException
    {
        try
        {
            // Check for null item.
            checkNullItem(item);

            // Get the old Item instance (which hasn't been updated)
            Item oldItem = getItemByID(item.getItemID());
            if (oldItem == null)
                throw new EntityNotFoundException("Update failed: could not find Item with ID " + item.getItemID());

            // Get the old title
            String oldTitle = oldItem.getTitle();

            // Get the old availability status
            boolean oldAvailability = oldItem.isAvailable();

            // Prepare a SQL command to update an item's title and availability by itemID.
            String sql = "UPDATE items SET title = ?, itemType = ?, barcode = ?, authorID = ?, classificationID = ?, " +
                    "allowedRentalDays = ?, available = ? WHERE " +
                    "itemID = ?";
            String[] params = {
                    item.getTitle(),
                    String.valueOf(item.getType()),
                    item.getBarcode(),
                    String.valueOf(item.getAuthorID()),
                    String.valueOf(item.getClassificationID()),
                    String.valueOf(item.getAllowedRentalDays()),
                    item.isAvailable() ? "1" : "0", // If boolean is true, add the string "1", if false, "0"
                    String.valueOf(item.getItemID())
            };

            // Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);

            // Depending on the itemType, update the appropriate details in either the films or literature table
            if (item instanceof Literature) updateLiterature((Literature) item);
            else if (item instanceof Film) updateFilm((Film) item);

            //Update maps
            updateMaps(item, oldTitle, oldAvailability);
        }
        catch (InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to update Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Updates the literature details in the database based on the provided literature object.
     *
     * @param literature The literature object containing the updated information.
     */
    private static void updateLiterature(Literature literature)
    {
        String updateLiteratureQuery = "UPDATE literature SET ISBN = ? WHERE literatureID = ?";
        String[] literatureParams = {
                literature.getISBN(),
                String.valueOf(literature.getItemID()) // Literature's itemID is same as literatureID
        };
        DatabaseHandler.executePreparedUpdate(updateLiteratureQuery, literatureParams);
    }

    /**
     * Updates the film details in the database based on the provided film object.
     *
     * @param film The film object containing the updated information.
     */
    private static void updateFilm(Film film)
    {
        // Check if countryOfProduction or listOfActors are null
        String countryOfProduction = film.getCountryOfProduction() == null ? null : film.getCountryOfProduction();
        String listOfActors = film.getListOfActors() == null ? null : film.getListOfActors();

        //Update to films table
        String updateFilmQuery = "UPDATE films SET ageRating = ?, countryOfProduction = ?, actors = ? WHERE filmID = ?";
        String[] filmParams = {
                String.valueOf(film.getAgeRating()),
                countryOfProduction,
                listOfActors, // actors is a string
                String.valueOf(film.getItemID()) // Film's itemID is same as filmID
        };
        DatabaseHandler.executePreparedUpdate(updateFilmQuery, filmParams);
    }

    /**
     * Updates the maps based on the changes in the item's title and availability status.
     *
     * @param item            The updated item object.
     * @param oldTitle        The previous title of the item.
     * @param oldAvailability The previous availability status of the item.
     */
    private static void updateMaps(Item item, String oldTitle, boolean oldAvailability)
    {
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

    /**
     * Deletes an item by setting its deleted boolean to true.
     *
     * @param itemToDelete The item object to be deleted.
     */
    public static void deleteItem(Item itemToDelete)
    {
        // Prepare a SQL command to set deleted to true for the specified item.
        String sql = "UPDATE items SET deleted = 1 WHERE itemID = ?";
        String[] params = {String.valueOf(itemToDelete.getItemID())};

        // Execute the update.
        DatabaseHandler.executePreparedUpdate(sql, params);

        // Update the deleted field of the item object
        itemToDelete.setDeleted(true);
    }

    /**
     * Restores a deleted item in the database by setting its deleted boolean to false.
     *
     * @param itemToRecover The item object to be recovered.
     */
    public static void undoDeleteItem(Item itemToRecover)
    {
        // Prepare a SQL command to set deleted to false for the specified item.
        String sql = "UPDATE items SET deleted = 0 WHERE itemID = ?";
        String[] params = {String.valueOf(itemToRecover.getItemID())};

        // Execute the update.
        DatabaseHandler.executePreparedUpdate(sql, params);

        // Update the deleted field of the item object
        itemToRecover.setDeleted(false);
    }

    /**
     * Deletes an item from the database and decrements the count of the item's title.
     *
     * @param item The Item object to be deleted.
     */
    public static void hardDeleteItem(Item item)
    throws NullEntityException, EntityNotFoundException
    {
        try
        {
            // Validate the input, throws NullEntityException
            checkNullItem(item);

            //Retrieve old title, throws EntityNotFoundException and InvalidIDException
            String oldTitle = retrieveOldTitle(item);

            // Delete from child tables (Film or Literature) first
            if (item instanceof Film)
            {
                deleteFilm(item);
            }
            else if (item instanceof Literature)
            {
                deleteLiterature(item);
            }

            // Prepare a SQL command to delete an item by itemID
            String sql = "DELETE FROM items WHERE itemID = ?";
            String[] params = new String[]{String.valueOf(item.getItemID())};

            // Execute the update
            DatabaseHandler.executePreparedUpdate(sql, params);

            // Decrement the count of the old title. Remove the entry if the count reaches 0
            decrementTitle(oldTitle);
        }
        catch (InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to delete Item due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a film from the database based on the provided item.
     *
     * @param item The item object representing the film to be deleted.
     */
    private static void deleteFilm(Item item)
    {
        // Prepare SQL command to delete a film by filmID
        String sql = "DELETE FROM films WHERE filmID = ?";
        String[] params = new String[]{String.valueOf(item.getItemID())};

        // Execute the update
        DatabaseHandler.executePreparedUpdate(sql, params);
    }

    /**
     * Deletes literature from the database based on the provided item.
     *
     * @param item The item object representing the literature to be deleted.
     */
    private static void deleteLiterature(Item item)
    {
        // Prepare SQL command to delete a literature by literatureID
        String sql = "DELETE FROM literature WHERE literatureID = ?";
        String[] params = new String[]{String.valueOf(item.getItemID())};

        // Execute the update
        DatabaseHandler.executePreparedUpdate(sql, params);
    }

    /**
     * Retrieves the old title of an item from the database.
     *
     * @param item The item object representing the item for which the old title is to be retrieved.
     * @return The old title of the item.
     * @throws InvalidIDException      If the item ID is invalid.
     * @throws EntityNotFoundException If the item with the specified ID is not found in the database.
     */
    private static String retrieveOldTitle(Item item)
    throws InvalidIDException, EntityNotFoundException
    {
        // Get the old item
        Item oldItem = getItemByID(item.getItemID());
        // Check if the item exists in the database
        if (oldItem == null)
            throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
        return oldItem.getTitle();
    }

    /**
     * Decrements the counts of stored titles and available titles based on the provided old title.
     *
     * @param oldTitle The old title of the item.
     */
    private static void decrementTitle(String oldTitle)
    {
        if (storedTitles.get(oldTitle) != null)
        {
            decrementStoredTitles(oldTitle);
            decrementAvailableTitles(oldTitle);
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
            //No point getting invalid Items
            checkEmptyTitle(title);

            //Prepare a SQL query to select an item by title
            String query = "SELECT * FROM items WHERE title = ?";
            String[] params = {title};

            //Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();

                //Loop through the ResultSet. For each record, create a new Item object and add it to the list
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
        //== 6 test cases
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
        //== 6 test cases
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
        //== 6 test cases

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
        //== 6 test cases

        return null;
    }

    public static Item getItemsByType(String type)
    { //Not going to be string, but an ENUM instead
        //Invalid enum should not be possible...

        //No Items of such type
        //Single item of type
        //multiple Items of type
        //== 3 test cases

        return null;
    }

    //== 27 test cases


    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    /**
     * Checks if the given ID is invalid.
     *
     * @param ID The ID to be checked.
     * @return True if the ID is invalid, false otherwise.
     */
    private static boolean invalidID(int ID)
    {
        return (ID <= 0);
    }

    /**
     * Checks if a barcode is already registered.
     *
     * @param barcode The barcode to be checked.
     * @return True if the barcode is already registered, false otherwise.
     */
    private static boolean barcodeTaken(String barcode)
    {
        return (registeredBarcodes.contains(barcode));
    }

    /**
     * Retrieves an existing Author object.
     *
     * @param authorID The ID of the author to be retrieved.
     * @return The retrieved Author object.
     * @throws EntityNotFoundException If no author with the given ID is found.
     */
    private static Author getExistingAuthor(int authorID)
    throws EntityNotFoundException
    {
        Author author = AuthorHandler.getAuthorByID(authorID);
        if (author == null)
            throw new EntityNotFoundException("Author with ID " + authorID + "not found.");
        return author;
    }

    /**
     * Retrieves an existing Classification object.
     *
     * @param classificationID The ID of the classification to be retrieved.
     * @return The retrieved Classification object.
     * @throws EntityNotFoundException If no classification with the given ID is found.
     */
    private static Classification getExistingClassification(int classificationID)
    throws EntityNotFoundException
    {
        Classification classification = ClassificationHandler.getClassificationByID(classificationID);
        if (classification == null)
            throw new EntityNotFoundException("Classification with ID " + classificationID + " not found.");
        return classification;
    }


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
}