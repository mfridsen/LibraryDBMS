package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidItemIDException;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNullException;

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
 *
 * This class contains database CRUD operation methods as well as other methods related to the Item entity class.
 * It contains a list of all Item titles for quicker validation.
 *
 * Note on Exceptions:
 *
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 *
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in ItemCreateGUI. When I press the "Create Item" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 *
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 *
 * Meaning, createNewItem (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
 */
public class ItemHandler {

    /**
     * Used to speed up searching. Contains a HashMap with the titles of all items in the database and how many
     * copies there are of each.
     */
    private static final Map<String, Integer> storedTitles = new HashMap<>();

    /**
     * Used to keep track of how many available copies there are of items in the database.
     */
    private static final Map<String, Integer> availableTitles = new HashMap<>();

    /**
     * Returns the storedTitles map.
     *
     * @return the storedTitles map
     */
    public static Map<String, Integer> getStoredTitles() {
        return storedTitles;
    }

    /**
     * Returns the availableTitles map.
     *
     * @return the availableTitles map
     */
    public static Map<String, Integer> getAvailableTitles() {
        return availableTitles;
    }

    /**
     * Increments the count of a title in both the stored and available titles maps.
     * @param title The title to be incremented.
     */
    public static void incrementBothTitles(String title) {
        incrementStoredTitles(title);
        incrementAvailableTitles(title);
    }

    /**
     * Increments the count of a title in both the stored and available titles maps.
     * @param title The title to be incremented.
     */
    public static void incrementStoredTitles(String title) {
        storedTitles.put(title, storedTitles.getOrDefault(title, 0) + 1);
        if (!availableTitles.containsKey(title)) {
            availableTitles.put(title, 0);
        }
    }

    /**
     * Increments the count of a title in the available titles map. If the title is not present in the map,
     * it is added with a count of 1.
     * @param title The title to be incremented.
     */
    public static void incrementAvailableTitles(String title) {
        availableTitles.put(title, availableTitles.getOrDefault(title, 0) + 1);
    }


    /**
     * Decrements the count of a title in the stored titles map. If the count reaches 0, the title is removed from both stored and available titles maps.
     * @param title The title to be decremented.
     */
    public static void decrementStoredTitles(String title) {
        int storedCount = storedTitles.get(title) - 1;
        if (storedCount == 0) {
            storedTitles.remove(title);
            availableTitles.remove(title);
        } else {
            storedTitles.put(title, storedCount);
        }
    }

    /**
     * Decrements the count of a title in the available titles map, if it exists. If the count reaches 0 or less, the title is removed from the map.
     * @param title The title to be decremented.
     */
    public static void decrementAvailableTitles(String title) {
        // Only decrement if title exists in the map
        if (availableTitles.containsKey(title)) {
            int availableCount = availableTitles.get(title) - 1;
            if (availableCount <= 0) {
                availableTitles.remove(title);
            } else {
                availableTitles.put(title, availableCount);
            }
        }
    }

    /**
     * Prepares the handler by syncing titles from the database.
     */
    public static void setup()  {
        syncTitles();
    }

    /**
     * Clears both stored and available titles maps.
     */
    public static void reset() {
        storedTitles.clear();
        availableTitles.clear();
    }

    /**
     * Syncs the handler with the database by clearing existing data and retrieving current titles from the database.
     */
    public static void syncTitles()  {
        if (!storedTitles.isEmpty()) {
            storedTitles.clear();
        }
        if (!availableTitles.isEmpty()) {
            availableTitles.clear();
        }
        retrieveTitlesFromTable();
    }

    /**
     * Retrieves titles from the items table and returns them as a map with title-count pairs.
     */
    private static void retrieveTitlesFromTable() {
        // Execute the query to retrieve all items
        QueryResult result = DatabaseHandler.executeQuery("SELECT title, available FROM items ORDER BY title ASC");

        try {
            // Add the retrieved items to the maps
            while (result.getResultSet().next()) {
                String title = result.getResultSet().getString("title");
                boolean available = result.getResultSet().getBoolean("available");

                incrementStoredTitles(title);
                if (available) {
                    incrementAvailableTitles(title);
                }
            }
        } catch (SQLException sqle) {
            ExceptionHandler.HandleFatalException(sqle);
        }

        // Close the resources
        result.close();

        if (storedTitles.isEmpty()) System.err.println("No titles retrieved from table!");
    }

    /**
     * Prints the stored titles and their counts.
     */
    public static void printTitles() {
        System.out.println("\nTitles:");
        storedTitles.forEach((title, count) -> System.out.println("Title: " + title + " Copies: " + count));
    }

    /**
     * Prints the list of items with their IDs and titles.
     *
     * @param itemList the list of items to print
     */
    public static void printItemList(List<Item> itemList) {
        System.out.println("Items:");
        int count = 1;
        for (Item item : itemList) {
            System.out.println(count + " itemID: " + item.getItemID() + ", title: " + item.getTitle());
        }
    }

    /**
     * Creates a new item with a given title, stores it in the database, and increments the title count.
     * @param title The title of the item to be created.
     * @return The created Item.
     * @throws InvalidTitleException If the title is null or an empty string.
     */
    public static Item createNewItem(String title) throws InvalidTitleException {
        //TODO-prio update method and test when Item is finished
        //Validate input, throws InvalidTitleException
        checkEmptyTitle(title);

        //Create item and retrieve and set itemID
        Item newItem = new Item(title);
        try {
            newItem.setItemID(saveItem(newItem)); //If this throws an exception, something is seriously wrong
        } catch (InvalidItemIDException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        //Increment the count of the new title. Add a new entry if the title does not exist yet.
        incrementBothTitles(title);

        return newItem;
    }

    /**
     * Saves an item to the database, retrieving and returning the generated item ID.
     * @param item The Item object to be saved.
     * @return The generated ID of the saved item.
     */
    private static int saveItem(Item item) {
        //TODO-prio update method and test when Item is finished
        //Prepare query
        String query = "INSERT INTO items (title, allowedRentalDays, available) VALUES (?, ?, ?)";
        String[] params = {
                item.getTitle(),
                String.valueOf(item.getAllowedRentalDays()),
                item.isAvailable() ? "1" : "0" //If boolean is true, add the string "1", if false, "0"
        };

        //Execute query and get the generated itemID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                ExceptionHandler.HandleFatalException(new SQLException("Failed to insert the item, no ID obtained."));
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // The method must return an integer. If an SQLException occurs, the method will exit before reaching this point.
        // However, the Java compiler doesn't understand that System.exit() terminates the program, so this return statement is necessary to avoid a compile error.
        return -1;
    }

    /**
     * Retrieves an item by its ID from the database.
     * @param itemID The ID of the item to be retrieved.
     * @return The retrieved Item.
     * @throws ItemNotFoundException If no item with the provided ID exists in the database.
     * @throws InvalidItemIDException If the provided ID is invalid (e.g., negative or zero).
     */
    public static Item getItemByID(int itemID) throws ItemNotFoundException, InvalidItemIDException {
        //TODO-prio update method and test when Item is finished
        //No point getting impossible items, throws InvalidItemIDException
        checkValidItemID(itemID);

        //Prepare a SQL query to select an item by itemID.
        String query = "SELECT * FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        //Execute the query and store the result in a ResultSet.
        try {
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

            try (queryResult) {
                ResultSet resultSet = queryResult.getResultSet();
                //If the ResultSet contains data, create a new Item object using the retrieved title
                //and set the item's itemID.
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    int allowedRentalDays = resultSet.getInt("allowedRentalDays");
                    boolean available = resultSet.getBoolean("available");
                    Item item = new Item(title); //If this throws an exception, something is seriously wrong
                    item.setItemID(itemID);
                    item.setAllowedRentalDays(allowedRentalDays);
                    item.setAvailable(available);
                    return item;
                } else {
                    throw new ItemNotFoundException("Item not found. Item ID: " + itemID);
                }
            }
        } catch (SQLException | InvalidTitleException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // The method must return an Item. If an SQLException occurs, the method will exit before reaching this point.
        // However, the Java compiler doesn't understand that System.exit() terminates the program, so this return statement is necessary to avoid a compile error.
        // Here, we return null as a dummy value.
        return null;
    }

    /**
     * Retrieves all items with a given title from the database.
     * @param title The title of the items to be retrieved.
     * @return A list of Item objects with the provided title.
     * @throws ItemNotFoundException If no items with the provided title exist in the database.
     * @throws InvalidTitleException If the provided title is null or an empty string.
     */
    public static List<Item> getItemsByTitle(String title) throws ItemNotFoundException, InvalidTitleException {
        //TODO-prio update method and test when Item is finished
        //No point getting invalid items, throws InvalidTitleException
        checkEmptyTitle(title);

        //Prepare a SQL query to select an item by title.
        String query = "SELECT * FROM items WHERE title = ?";
        String[] params = {title};

        //Create a list to store the Rental objects.
        List<Item> items = new ArrayList<>();

        //Execute the query and store the result in a ResultSet.
        try {
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

            try (queryResult) {
                ResultSet resultSet = queryResult.getResultSet();

                //Loop through the ResultSet. For each record, create a new Item object and add it to the list.
                while (resultSet.next()) {
                    Item item = new Item(title);
                    item.setItemID(resultSet.getInt("itemID")); //If this throws an exception, something is seriously wrong
                    int allowedRentalDays = resultSet.getInt("allowedRentalDays");
                    boolean available = resultSet.getBoolean("available");
                    item.setAllowedRentalDays(allowedRentalDays);
                    item.setAvailable(available);
                    items.add(item);
                }
                if (items.isEmpty()) {
                    throw new ItemNotFoundException(title);
                }
            }
        } catch (SQLException | InvalidItemIDException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // The method must return a List<Item>. If an SQLException occurs, the method will exit before reaching this point.
        // However, the Java compiler doesn't understand that System.exit() terminates the program, so this return statement is necessary to avoid a compile error.
        // Here, we return a dummy value.
        return items.isEmpty() ? null : items;
    }


    public static List<Item> getItemsByISBN(String ISBN) {
        //Empty ISBN
        //Null ISBN
        //Incorrect format ISBN
        //Item does not exist
        //Item does exist
        //Multiple items exist
        // == 6 test cases
        return null;
    }

    public static List<Item> getItemsByGenre(String genre) {
        //Empty genre
        //Null genre
        //genre does not exist
        //items don't exist in genre
        //Item does exist
        //Multiple items exist
        // == 6 test cases
        return null;
    }

    public static List<Item> getItemsByAuthor(String authorName) {
        //empty authorName
        //null authorName
        //no such author
        //author exists, but no titles for some reason
        //author exists and has title
        //Multiple items exist for author
        // == 6 test cases

        return null;
    }

    public static Item getItemsByPublisher(String publisherName) {
        //empty publisherName
        //null publisherName
        //no such publisher
        //publisher exists, but no titles for some reason
        //publisher exists and has title
        //Multiple items exist for publisher
        // == 6 test cases

        return null;
    }

    public static Item getItemsByType(String type) { //Not going to be string, but an ENUM instead
        //Invalid enum should not be possible...

        //No items of such type
        //Single item of type
        //multiple items of type
        // == 3 test cases

        return null;
    }

    // == 27 test cases


    /**
     * Updates an existing item in the database and adjusts the count of the old and new titles.
     * @param item The Item object containing the updated information.
     * @throws ItemNotFoundException If the item does not exist in the database.
     * @throws ItemNullException If the provided Item object is null.
     */
    public static void updateItem(Item item) throws ItemNotFoundException, ItemNullException, InvalidItemIDException {
        //TODO-prio update when Item is finished
        //Validate the input, throws ItemNullException
        checkNullItem(item);

        // Get the old Item instance, throws ItemNotFoundException
        Item oldItem = getItemByID(item.getItemID());
        if (oldItem == null) {
            throw new ItemNotFoundException("Item with id: " + item.getItemID() + " not found.");
        }

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
        if (!oldTitle.equals(item.getTitle())) {
            decrementStoredTitles(oldTitle);
            incrementStoredTitles(item.getTitle());
        }

        // If the availability status has changed, adjust the counts in the availableTitles map
        if (oldAvailability != item.isAvailable()) {
            if (oldAvailability) {
                decrementAvailableTitles(oldTitle);
            }
            if (item.isAvailable()) {
                incrementAvailableTitles(item.getTitle());
            }
        }
    }

    /**
     * Deletes an item from the database and decrements the count of the item's title.
     * @param item The Item object to be deleted.
     * @throws ItemNotFoundException If the item does not exist in the database.
     * @throws ItemNullException If the provided Item object is null.
     */
    public static void deleteItem(Item item) throws ItemNotFoundException, ItemNullException {
        //TODO-prio update when Item is finished
        //Validate the input, ItemNullException
        checkNullItem(item);

        // Get the old title, throws ItemNotFoundException
        String oldTitle = getItemTitleByID(item.getItemID());

        // Check if the item exists in the database
        String sql = "SELECT COUNT(*) FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(item.getItemID())};

        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            resultSet.next(); // Move to the first row

            // If the item does not exist, throw an ItemNotFoundException
            if (resultSet.getInt(1) == 0) {
                throw new ItemNotFoundException(item.getItemID());
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        //Prepare a SQL command to delete an item by itemID.
        sql = "DELETE FROM items WHERE itemID = ?";
        params = new String[]{String.valueOf(item.getItemID())};

        //Execute the update. //TODO-prio handle cascades in rentals
        DatabaseHandler.executePreparedUpdate(sql, params);

        // Decrement the count of the old title. Remove the entry if the count reaches 0.
        if(storedTitles.get(oldTitle) != null) {
            decrementStoredTitles(oldTitle);
            decrementAvailableTitles(oldTitle);
        }
    }

    /**
     * Retrieves the allowed rental days for an item by its ID.
     * @param itemID The ID of the item.
     * @return The allowed rental days for the item.
     * @throws ItemNotFoundException If no item with the provided ID exists in the database.
     * @throws InvalidItemIDException If the provided ID is invalid (e.g., negative or zero).
     */
    public static int getAllowedRentalDaysByID(int itemID) throws ItemNotFoundException, InvalidItemIDException {
        Item item = getItemByID(itemID);
        if (item != null) return item.getAllowedRentalDays();
        else throw new ItemNotFoundException("Item not found. Item ID: " + itemID);
    }

    /**
     * Retrieves the number of available copies for a specific item.
     * @param item The Item object for which the available copies are to be retrieved.
     * @return The number of available copies for the item.
     * @throws ItemNotFoundException If the item does not exist in the database.
     * @throws ItemNullException If the Item is null.
     */
    public static int getAvailableCopiesForItem(Item item) throws ItemNotFoundException, ItemNullException {
        checkNullItem(item);
        if (!availableTitles.containsKey(item.getTitle()) && !storedTitles.containsKey(item.getTitle()))
            throw new ItemNotFoundException(item.getTitle() + ": Item not found.");
        return availableTitles.get(item.getTitle());
    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    /**
     * This method is used to retrieve the title of an item by its ID from the database. It executes a prepared
     * SQL query to fetch the title of the item corresponding to the provided itemID. If the item exists, the title
     * is returned. If the item does not exist, an ItemNotFoundException is thrown. In the case of a SQLException,
     * the exception is handled and the method returns null.
     *
     * @param itemID The ID of the item whose title is to be fetched.
     * @return The title of the item if it exists, null if an SQLException occurs.
     * @throws ItemNotFoundException if no item with the provided ID exists in the database.
     */
    private static String getItemTitleByID(int itemID) throws ItemNotFoundException {
        String sql = "SELECT title FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        try {
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);

            try (queryResult) {
                if(queryResult.getResultSet().next()){
                    return queryResult.getResultSet().getString("title");
                }else{
                    throw new ItemNotFoundException("Item with ID " + itemID + " does not exist.");
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
            return null;
        }
    }

    /**
     * Checks whether a given title is null or empty. If so, throws an InvalidTitleException,
     * which must be handled.
     * @param title the title to check.
     * @throws InvalidTitleException if title is null or empty.
     */
    private static void checkEmptyTitle(String title) throws InvalidTitleException {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Empty title.");
    }

    /**
     * Checks whether a given itemID is invalid (<= 0). If so, throws an InvalidItemIDException,
     * which must be handled.
     * @param itemID the ID to check.
     * @throws InvalidItemIDException if itemID <= 0.
     */
    private static void checkValidItemID(int itemID) throws InvalidItemIDException {
        if (itemID <= 0)
            throw new InvalidItemIDException("Error retrieving item by itemID: invalid itemID " + itemID);
    }

    /**
     * Checks whether a given Item is null. If so, throws a ItemNullException which must be handled.
     * @param item the item to check.
     * @throws ItemNullException if item is null.
     */
    private static void checkNullItem(Item item) throws ItemNullException {
        if (item == null)
            throw new ItemNullException("Invalid item: item is null.");
    }
}