package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;

import java.sql.*;

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
    //The code is cleaner if every Handler class stores a reference to the Connection
    private static Connection connection;

    //TODO-exception comment
    /**
     * To ensure that things are done in the correct order, only DatabaseHandler will retrieve its connection
     * on its own. The rest of the Handlers need to be assigned the connection, by calling their setup methods
     * with the connection as argument after the DatabaseHandlers setup method has been called.
     * @param con the Connection to the database.
     * @throws SQLException
     */
    public static void setup(Connection con) throws SQLException {
        connection = con;
    }

    //TODO-exception throw
    /**
     * Creates a new Item with the specified title and saves it to the database.
     * If the Item creation fails, this method returns null.
     *
     * @param title the title of the new Item object.
     * @return the created Item object on success, null on failure.
     */
    public static Item createNewItem(String title) {
        //No point creating invalid items
        if (title == null || title.equals("")) {
            System.err.println("Error creating a new item: empty title."); //TODO-log
            return null;
        }

        try {
            Item newItem = new Item(title);
            newItem.setItemID(saveItem(newItem));
            return newItem;
        } catch (Exception e) {
            System.err.println("Error creating a new item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //TODO-test
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
        //No point saving null items
        if (item == null) {
            System.err.println("Error inserting item, no ID obtained: item null."); //TODO-log
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

    //TODO-test
    //TODO-exception throw
    /**
     * Retrieves a Item object from the database based on the provided item ID.
     *
     * <p>This method attempts to retrieve the item details from the 'items' table in the database
     * that correspond to the provided item ID. If a item with the given ID exists, a new Item object
     * is created with the retrieved title and the item's ID is set.</p>
     *
     * @param itemID The unique ID of the item to be retrieved.
     * @return The Item object corresponding to the provided ID, or null if no such item is found.
     */
    public static Item getItemByID(int itemID) {
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
        } catch (SQLException e) {
            System.err.println("Error retrieving item by itemID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-exception throw
    //TODO-test
    /**
     * Retrieves a Item object from the database based on the provided title.
     *
     * <p>This method attempts to retrieve the item details from the 'items' table in the database
     * that correspond to the provided title. If a item with the given title exists, a new Item
     * object is created with the retrieved title.</p>
     *
     * @param title The title of the item to be retrieved.
     * @return The Item object corresponding to the provided title, or null if no such item is found.
     */
    public static Item getItemByTitle(String title) {
        //No point getting invalid items
        if (title == null || title.equals("")) {
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
        } catch (SQLException e) {
            System.err.println("Error retrieving item by title: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-test
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
     */
    public boolean updateItem(Item item) throws SQLException {
        //No point updating null items
        if (item == null) {
            System.err.println("Error updating item: item null."); //TODO-log
            return false;
        }

        boolean isUpdated = false;

        //Prepare a SQL command to update a item's title by itemID.
        String sql = "UPDATE items SET title = ? WHERE itemID = ?";
        String[] params = {item.getTitle(), String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Check if the update was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            isUpdated = true;
        } else {
            throw new SQLException("Error updating item:");
        }

        //Return whether the item was updated successfully.
        return isUpdated;
    }

    //TODO-test
    /**
     * Deletes a item from the database.
     *
     * @param item The item to delete.
     * @return true if the item was successfully deleted, false otherwise.
     */
    public boolean deleteItem(Item item) throws SQLException {
        if (item == null) {
            System.err.println("Error deleting item: item null."); //TODO-log
            return false;
        }

        boolean isDeleted = false;

        //Prepare a SQL command to delete a item by itemID.
        String sql = "DELETE FROM items WHERE itemID = ?";
        String[] params = {String.valueOf(item.getItemID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Check if the delete was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            isDeleted = true;

        }

        //Return whether the item was deleted successfully.
        return isDeleted;
    }
}