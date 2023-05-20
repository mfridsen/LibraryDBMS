package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;

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
 */
public class ItemHandler {

    //TODO-exceptions redirect SQL exceptions and revise all tests

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

    public static void incrementBothTitles(String title) {
        incrementStoredTitles(title);
        incrementAvailableTitles(title);
    }

    public static void incrementStoredTitles(String title) {
        storedTitles.put(title, storedTitles.getOrDefault(title, 0) + 1);
        if (!availableTitles.containsKey(title)) {
            availableTitles.put(title, 0);
        }
    }


    public static void incrementAvailableTitles(String title) {
        availableTitles.put(title, availableTitles.getOrDefault(title, 0) + 1);
    }


    public static void decrementStoredTitles(String title) {
        int storedCount = storedTitles.get(title) - 1;
        if (storedCount == 0) {
            storedTitles.remove(title);
            availableTitles.remove(title);
        } else {
            storedTitles.put(title, storedCount);
        }
    }

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


    public static void setup()  {
        syncTitles();
    }

    public static void reset() {
        storedTitles.clear();
        availableTitles.clear();
    }


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


    //TODO-prio update method and test when Item is finished
    public static Item createNewItem(String title)  {
        //Validate input
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Empty title.");

        //Create item and retrieve and set itemID
        Item newItem = new Item(title);
        newItem.setItemID(saveItem(newItem));

        //Increment the count of the new title. Add a new entry if the title does not exist yet.
        incrementBothTitles(title);

        return newItem;
    }


    /**
     * Note that the -1 return statement is technically unreachable because System.exit(1) is called if any
     * SQLException occurs. However, the Java compiler doesn't understand that System.exit() terminates the program,
     * so a return statement is required to avoid a compile error.
     * @param item
     * @return
     */
    //TODO-prio update method and test when Item is finished
    private static int saveItem(Item item) {
        //Validate input
        if (item == null)
            throw new IllegalArgumentException("Invalid item: item is null.");

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

    //TODO-prio update method and test when Item is finished
    public static Item getItemByID(int itemID) throws ItemNotFoundException {
        //No point getting impossible items
        if (itemID <= 0)
            throw new IllegalArgumentException("Error retrieving item by itemID: invalid itemID " + itemID);

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
                    Item item = new Item(title);
                    item.setItemID(itemID);
                    item.setAllowedRentalDays(allowedRentalDays);
                    item.setAvailable(available);
                    return item;
                } else {
                    throw new ItemNotFoundException("Item not found. Item ID: " + itemID);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // The method must return an Item. If an SQLException occurs, the method will exit before reaching this point.
        // However, the Java compiler doesn't understand that System.exit() terminates the program, so this return statement is necessary to avoid a compile error.
        // Here, we return null as a dummy value.
        return null;
    }


    //TODO-test ASAP
    //TODO-prio update method and test when Item is finished
    public static List<Item> getItemsByTitle(String title) throws ItemNotFoundException {
        //No point getting invalid items
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Error retrieving item by title: empty title.");

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
                    item.setItemID(resultSet.getInt("itemID"));
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
        } catch (SQLException e) {
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

    //TODO-prio update when Item is finished
    public static boolean updateItem(Item item) throws ItemNotFoundException {
        // Validate the input
        if (item == null)
            throw new IllegalArgumentException("Invalid item: item is null.");

        // Get the old title
        String oldTitle = getItemTitleByID(item.getItemID());

        // Prepare a SQL command to update an item's title by itemID.
        String sql = "UPDATE items SET title = ? WHERE itemID = ?";
        String[] params = {item.getTitle(), String.valueOf(item.getItemID())};

        // Execute the update.
        int rowsAffected = DatabaseHandler.executePreparedUpdate(sql, params);

        // If the update was successful, update the storedTitles and availableTitles
        if (rowsAffected == 1 && !item.getTitle().equals(oldTitle)) {
            // Decrement the count of the old title
            decrementStoredTitles(oldTitle);
            decrementAvailableTitles(oldTitle);

            // Increment the count of the new title
            incrementStoredTitles(item.getTitle());
            incrementAvailableTitles(item.getTitle());
        }
        return rowsAffected == 1;
    }

    private static String getItemTitleByID(int itemId) throws ItemNotFoundException {
        String sql = "SELECT title FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemId)};

        try {
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);

            try (queryResult) {
                if(queryResult.getResultSet().next()){
                    return queryResult.getResultSet().getString("title");
                }else{
                    throw new ItemNotFoundException("Item with ID " + itemId + " does not exist.");
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
            return null;
        }
    }



    //TODO-prio update when Item is finished
    public static boolean deleteItem(Item item) throws ItemNotFoundException {
        //Validate the input
        if (item == null)
            throw new IllegalArgumentException("Invalid item: item is null.");

        // Get the old title
        String oldTitle = getItemTitleByID(item.getItemID());

        // Check if the item exists in the database
        String sql = "SELECT COUNT(*) FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(item.getItemID())};

        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            resultSet.next(); // Move to the first row

            // If the item does not exist, throw an exception
            if (resultSet.getInt(1) == 0) {
                throw new ItemNotFoundException(item.getItemID());
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        //Prepare a SQL command to delete an item by itemID.
        sql = "DELETE FROM items WHERE itemID = ?";
        params = new String[]{String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = 0;
        rowsAffected = DatabaseHandler.executePreparedUpdate(sql, params);

        //Check if the delete was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            // Decrement the count of the old title. Remove the entry if the count reaches 0.
            if(storedTitles.get(oldTitle) != null) {
                storedTitles.put(oldTitle, storedTitles.get(oldTitle) - 1);
                if (storedTitles.get(oldTitle) == 0) {
                    storedTitles.remove(oldTitle);
                }
                availableTitles.put(oldTitle, availableTitles.get(oldTitle) - 1);
                if (availableTitles.get(oldTitle) == 0) {
                    availableTitles.remove(oldTitle);
                }
            }
            return true;
        }
        return false;
    }




    //TODO-test
    //TODO-comment
    //TODO-implement
    //TODO-exception
    public static int getAllowedRentalDaysByID(int itemID) throws ItemNotFoundException {
        Item item = getItemByID(itemID);
        if (item != null)
            return item.getAllowedRentalDays();
        else
            throw new ItemNotFoundException("Item not found. Item ID: " + itemID);
    }

    public static int getAvailableCopiesForItem(Item item) throws ItemNotFoundException {
        if (!availableTitles.containsKey(item.getTitle())) {
            throw new ItemNotFoundException(item.getTitle() + ": Item not found.");
        }
        return availableTitles.get(item.getTitle());
    }
}