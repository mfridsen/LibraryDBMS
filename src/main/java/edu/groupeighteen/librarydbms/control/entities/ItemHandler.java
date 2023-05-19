package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
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
    /**
     * Used to speed up searching
     */
    private static Map<String, Integer> storedTitles = new HashMap<>();

    /**
     * Syncs the storedTitles against the items table.
     *
     * @throws SQLException if there's an error executing the SQL query
     */
    public static void setup() throws SQLException {
        syncTitles();
    }

    /**
     * Syncs the storedTitles by retrieving titles from the items table.
     *
     * @throws SQLException if there's an error executing the SQL query
     */
    public static void syncTitles() throws SQLException {
        if (!storedTitles.isEmpty()) {
            storedTitles.clear();
        }
        storedTitles = retrieveTitlesFromTable();
    }

    /**
     * Retrieves titles from the items table and returns them as a map with title-count pairs.
     *
     * @return a map of titles and their counts
     * @throws SQLException if there's an error executing the SQL query
     */
    private static Map<String, Integer> retrieveTitlesFromTable() throws SQLException {
        // Execute the query to retrieve all titles
        QueryResult result = DatabaseHandler.executeQuery("SELECT title, count(*) as count FROM items GROUP BY title ORDER BY title ASC");
        Map<String, Integer> titles = new HashMap<>();

        // Add the retrieved titles to the map
        while (result.getResultSet().next()) {
            titles.put(result.getResultSet().getString("title"), result.getResultSet().getInt("count"));
        }

        // Close the resources
        result.close();
        return titles;
    }

    /**
     * Prints the stored titles and their counts.
     */
    public static void printTitles() {
        System.out.println("\nTitles:");
        storedTitles.forEach((title, count) -> System.out.println("Title: " + title + " Available: " + count));
    }

    /**
     * Returns the storedTitles map.
     *
     * @return the storedTitles map
     */
    public static Map<String, Integer> getStoredTitles() {
        return storedTitles;
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
    public static Item createNewItem(String title) throws SQLException {
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Empty title.");

        Item newItem = new Item(title);
        newItem.setItemID(saveItem(newItem));

        storedTitles.put(newItem.getTitle(), storedTitles.getOrDefault(newItem.getTitle(), 0) + 1);
        return newItem;
    }


    //TODO-prio update method and test when Item is finished
    private static int saveItem(Item item) throws SQLException {
        //Validate input
        if (item == null)
            throw new IllegalArgumentException("Invalid item: item is null.");

        //Prepare query
        String query = "INSERT INTO items (title, allowedRentalDays) VALUES (?, ?)"; //Update these two when more fields are added, as well as javadoc
        String[] params = {item.getTitle(), String.valueOf(item.getAllowedRentalDays())}; //Update these two when more fields are added, as well as javadoc

        //Execute query and get the generated itemID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
            else throw new SQLException("Failed to insert the item, no ID obtained.");
        }
    }

    //TODO-prio update method and test when Item is finished
    public static Item getItemByID(int itemID) throws SQLException, ItemNotFoundException {
        //No point getting impossible items
        if (itemID <= 0)
            throw new IllegalArgumentException("Error retrieving item by itemID: invalid itemID " + itemID);

        //Prepare a SQL query to select an item by itemID.
        String query = "SELECT * FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

        try (queryResult) {
            ResultSet resultSet = queryResult.getResultSet();
            //If the ResultSet contains data, create a new Item object using the retrieved title
            //and set the item's itemID.
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int allowedRentalDays = resultSet.getInt("allowedRentalDays");
                Item item = new Item(title);
                item.setItemID(itemID);
                item.setAllowedRentalDays(allowedRentalDays);
                return item;
            } else
                throw new ItemNotFoundException(itemID);
        }
    }


    //TODO-test ASAP
    //TODO-prio update method and test when Item is finished
    public static List<Item> getItemsByTitle(String title) throws SQLException, ItemNotFoundException {
        //No point getting invalid items
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Error retrieving item by title: empty title.");

        //Prepare a SQL query to select an item by title.
        String query = "SELECT * FROM items WHERE title = ?";
        String[] params = {title};

        //Create a list to store the Rental objects.
        List<Item> items = new ArrayList<>();

        //Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet. For each record, create a new Item object and add it to the list.
            while (resultSet.next()) {
                Item item = new Item(title);
                item.setItemID(resultSet.getInt("itemID"));
                int allowedRentalDays = resultSet.getInt("allowedRentalDays");
                item.setAllowedRentalDays(allowedRentalDays);
                items.add(item);
            }
            if (items.isEmpty()) {
                throw new ItemNotFoundException(title);
            }
        }
        return items;
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
    public static boolean updateItem(Item item) throws SQLException, ItemNotFoundException {
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

        // If the update was successful, update the storedTitles
        if (rowsAffected == 1) {
            // Decrement the count of the old title. Remove the entry if the count reaches 0.
            storedTitles.put(oldTitle, storedTitles.get(oldTitle) - 1);
            if (storedTitles.get(oldTitle) == 0) {
                storedTitles.remove(oldTitle);
            }

            // Increment the count of the new title. Add a new entry if the title does not exist yet.
            storedTitles.put(item.getTitle(), storedTitles.getOrDefault(item.getTitle(), 0) + 1);
        }
        return rowsAffected == 1;
    }

    private static String getItemTitleByID(int itemId) throws SQLException, ItemNotFoundException {
        String sql = "SELECT title FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemId)};
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);

        if(queryResult.getResultSet().next()){
            return queryResult.getResultSet().getString("title");
        }else{
            throw new ItemNotFoundException("Item with ID " + itemId + " does not exist.");
        }
    }


    //TODO-prio update when Item is finished
    public static boolean deleteItem(Item item) throws SQLException, ItemNotFoundException {
        //Validate the input
        if (item == null)
            throw new IllegalArgumentException("Invalid item: item is null.");

        // Check if the item exists in the database
        String sql = "SELECT COUNT(*) FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(item.getItemID())};

        QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
        ResultSet resultSet = queryResult.getResultSet();
        resultSet.next(); // Move to the first row

        // If the item does not exist, throw an exception
        if (resultSet.getInt(1) == 0) {
            queryResult.close();
            throw new ItemNotFoundException(item.getItemID());
        }

        queryResult.close();

        //Prepare a SQL command to delete a item by itemID.
        sql = "DELETE FROM items WHERE itemID = ?";
        params = new String[]{String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executePreparedUpdate(sql, params);

        //Check if the delete was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            // Decrement the count of the item's title. Remove the entry if the count reaches 0.
            storedTitles.put(item.getTitle(), storedTitles.get(item.getTitle()) - 1);
            if (storedTitles.get(item.getTitle()) == 0) {
                storedTitles.remove(item.getTitle());
            }
            return true;
        }
        return false;
    }


    //TODO-test
    //TODO-comment
    //TODO-implement
    //TODO-exception
    public static int getAllowedRentalDaysByID(int itemID) throws SQLException, ItemNotFoundException {
        Item item = getItemByID(itemID);
        return item.getAllowedRentalDays();
    }

    public static int getAvailableCopiesForItem(Item item) {
        return storedTitles.get(item.getTitle()); //TODO-test should work
    }
}