package edu.groupeighteen.librarydbms.control.entities;

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

    //TODO-exception handle
    /**
     * To ensure that things are done in the correct order, only DatabaseHandler will retrieve its connection
     * on its own. The rest of the Handlers need to be assigned the connection, by calling their setup methods
     * after the DatabaseHandlers setup method is called with the connection as argument.
     * @param con the Connection to the database.
     * @throws SQLException
     */
    public static void setup(Connection con) throws SQLException {
        connection = con;
    }

    public static Item createNewItem(String title) {
        try {
            Item newItem = new Item(title);
            newItem.setItemID(saveItem(newItem));
            return newItem;
        } catch (Exception e) {
            System.err.println("Error creating a new item: " + e.getMessage());
            e.printStackTrace(); //TODO-exception handle
            return null;
        }
    }

    //TODO-future comment
    //TODO-test
    //TODO-exception handle
    /**
     *
     * @param item
     * @return
     * @throws SQLException
     */
    public static int saveItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (title) VALUES (?)";
        //Try-with-resources, useful!
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getTitle());
            statement.executeUpdate();

            // Get the generated userID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) { //Try-with-resources, useful!
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert the item, no ID obtained.");
                }
            }
        }
    }
}