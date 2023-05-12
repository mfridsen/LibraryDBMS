package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 */
public class ItemHandler {
    //Used to speed up searching
    //TODO maybe change to Set?
    private static ArrayList<String> storedTitles = new ArrayList<>();

    //TODO-comment update this comment
    /**
     * To ensure that things are done in the correct order, only DatabaseHandler will retrieve its connection
     * on its own. The rest of the Handlers need to be assigned the connection, by calling their setup methods
     * with the connection as argument after the DatabaseHandlers setup method has been called.
     */
    public static void setup() throws SQLException {
        syncTitles();
    }

    //TODO-comment
    //TODO-test
    public static void syncTitles() throws SQLException {
        if (!storedTitles.isEmpty()) {
            storedTitles.clear();
        }
        storedTitles = retrieveTitlesFromTable();
    }

    //TODO-comment
    //TODO-test
    private static ArrayList<String> retrieveTitlesFromTable() throws SQLException {
        //Execute the query to retrieve all titles
        QueryResult result = DatabaseHandler.executeQuery("SELECT title FROM items ORDER BY itemID ASC");
        ArrayList<String> titles = new ArrayList<>();

        //Add the retrieved titles to the ArrayList
        while (result.getResultSet().next()) {
            titles.add(result.getResultSet().getString("title"));
        }

        //Close the resources
        result.close();
        return titles;
    }

    //TODO-comment
    //TODO-test
    public static void printTitles() {
        System.out.println("\nTitles:");
        int num = 1;
        for (String title : storedTitles) {
            System.out.println(num + ": " + title);
            num++;
        }
    }

    //TODO-comment
    public static ArrayList<String> getStoredTitles() {
        return storedTitles;
    }

    /**
     * Creates a new Item with the specified title and saves it to the database.
     * If the Item creation fails, this method returns null.
     *
     * @param title the title of the new Item object.
     * @return the created Item object on success, null on failure.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static Item createNewItem(String title) throws SQLException {
        //No point creating invalid items
        if (title == null || title.isEmpty()) {
            System.err.println("Error creating a new item: empty title."); //TODO-log
            return null;
        }

        Item newItem = new Item(title);
        newItem.setItemID(saveItem(newItem));
        if (!storedTitles.contains(newItem.getTitle())) //We don't need duplicates
            storedTitles.add(newItem.getTitle());
        return newItem;
    }

    /**
     * Saves an Item object to the database.
     *
     * <p>This method attempts to insert a new item into the 'item' table. It uses the title property of the
     * provided Item object to populate the new record. The database is expected to generate
     * a unique ID for each new item, which is retrieved and returned by this method.</p>
     *
     * @param item the Item object to be saved. This object should have a title set.
     * @return the unique itemID generated by the database for the new item record.
     * @throws SQLException If an error occurs while interacting with the database, or if the database does not
     *      generate a new unique ID for the inserted item.
     */
    public static int saveItem(Item item) throws SQLException {
        //No point saving null or invalid items
        if (item == null) {
            System.err.println("Error inserting item, no ID obtained: item null."); //TODO-log
            return 0;
        }
        if (item.getTitle() == null || item.getTitle().isEmpty()) {
            System.err.println("Error inserting item, no ID obtained: title null or empty."); //TODO-log
            return 0;
        }

        //Prepare query
        String query = "INSERT INTO items (title) VALUES (?)"; //Update these two when more fields are added, as well as javadoc
        String[] params = {item.getTitle()}; //Update these two when more fields are added, as well as javadoc

        //Execute query
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS);

        //Get the generated itemID
        ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
        if (generatedKeys.next()) {
            int itemID = generatedKeys.getInt(1);
            queryResult.close();
            return itemID;
        } else {
            queryResult.close();
            throw new SQLException("Failed to insert the item, no ID obtained.");
        }
    }

    //TODO-exception might want to throw a custom exception (like ItemNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * Retrieves a Item object from the database based on the provided item ID.
     *
     * <p>This method attempts to retrieve the item details from the 'items' table in the database
     * that correspond to the provided item ID. If a item with the given ID exists, a new Item object
     * is created with the retrieved title and the item's ID is set.</p>
     *
     * @param itemID The unique ID of the item to be retrieved.
     * @return The Item object corresponding to the provided ID, or null if no such item is found.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static Item getItemByID(int itemID) throws SQLException {
        //No point getting impossible items
        if (itemID <= 0) {
            System.err.println("Error retrieving item by itemID: invalid itemID " + itemID); //TODO-log
            return null;
        }

        //Prepare a SQL query to select an item by itemID.
        String query = "SELECT title FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(itemID)};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);
        ResultSet resultSet = queryResult.getResultSet();
        
        try {
            //If the ResultSet contains data, create a new Item object using the retrieved title 
            //and set the item's itemID.
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                Item item = new Item(title);
                item.setItemID(itemID);
                return item;
            }
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-exception might want to throw a custom exception (like ItemNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * Retrieves a Item object from the database based on the provided title.
     *
     * <p>This method attempts to retrieve the item details from the 'items' table in the database
     * that correspond to the provided title. If a item with the given title exists, a new Item
     * object is created with the retrieved title.</p>
     *
     * @param title The title of the item to be retrieved.
     * @return The Item object corresponding to the provided title, or null if no such item is found.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static Item getItemByTitle(String title) throws SQLException {
        //No point getting invalid items
        if (title == null || title.isEmpty()) {
            System.err.println("Error retrieving item by title: empty title."); //TODO-log
            return null;
        }

        //Prepare a SQL query to select an item by title.
        String query = "SELECT itemID FROM items WHERE title = ?";
        String[] params = {title};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

        try {
            ResultSet resultSet = queryResult.getResultSet();
            //If the ResultSet contains data, create a new Item object using the retrieved title,
            //and set the item's itemID.
            if (resultSet.next()) {
                Item item = new Item(title);
                item.setItemID(resultSet.getInt("itemID"));
                return item;
            }
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    /**
     * Updates the corresponding item's record in the database with the details of the provided Item object.
     *
     * This method prepares a SQL UPDATE command to modify the existing item's title based on
     * the Item object's itemID. It sets the values for the prepared statement using the Item object's data and
     * executes the update.
     *
     * @param item The Item object containing the updated details of the item.
     *             The item's itemID should correspond to an existing item in the database.
     * @return true if the item's record was successfully updated, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static boolean updateItem(Item item) throws SQLException {
        //No point updating null items
        if (item == null) {
            System.err.println("Error updating item: item null."); //TODO-log
            return false;
        }
        if (item.getItemID() <= 0) {
            System.err.println("Error updating item: invalid itemID " + item.getItemID()); //TODO-log
            return false;
        }

        //Prepare a SQL command to update a item's title by itemID.
        String sql = "UPDATE items SET title = ? WHERE itemID = ?";
        String[] params = {item.getTitle(), String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Check if the update was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            //Return whether the item was updated successfully.
            return true;
        } else {
            throw new SQLException("Error updating item:");
        }
    }

    //TODO-test re-test and expand comment
    /**
     * Deletes a item from the database.
     *
     * @param item The item to delete.
     * @return true if the item was successfully deleted, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static boolean deleteItem(Item item) throws SQLException {
        boolean isDeleted = false;
        //No point deleting null items
        if (item == null) {
            System.err.println("Error deleting item: item null."); //TODO-log
            return false;
        }
        if (item.getItemID() <= 0) {
            System.err.println("Error deleting item: invalid itemID " + item.getItemID()); //TODO-log
            return false;
        }

        //Prepare a SQL command to delete a item by itemID.
        String sql = "DELETE FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Check if the delete was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            isDeleted = true;

            //Check if there are still other items with the same title
            sql = "SELECT COUNT(*) FROM items WHERE title = ?";
            params[0] = item.getTitle();
            QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params);
            ResultSet resultSet = queryResult.getResultSet();
            resultSet.next(); //Move to the first row

            //If no other items with the same title exist, remove the title from storedTitles
            if (resultSet.getInt(1) == 0) {
                storedTitles.remove(item.getTitle());
            }

            queryResult.close();
        }

        //Return whether the item was deleted successfully.
        return isDeleted;
    }
}