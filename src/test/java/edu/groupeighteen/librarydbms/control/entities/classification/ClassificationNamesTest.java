package edu.groupeighteen.librarydbms.control.entities.classification;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ClassificationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-31
 */
public class ClassificationNamesTest extends BaseHandlerTest
{

    @BeforeEach
    @Override
    protected void setupAndReset()
    {
        try
        {
            setupConnectionAndTables();
            ClassificationHandler.reset();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testSyncClassificationNames()
    {
        ClassificationHandler.setup();
        Assertions.assertEquals(0, ClassificationHandler.getStoredClassificationNames().size());
    }

    @Test
    @Order(2)
    public void testPrintClassificationNames()
    {
        // Redirect System.out to a mock PrintStream
        var mockPrintStream = mock(java.io.PrintStream.class);
        var originalOut = System.out;
        System.setOut(mockPrintStream);

        ClassificationHandler.printClassificationNames();

        // Verify that the expected output is printed
        verify(mockPrintStream).println("\nClassificationNames");

        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    @Order(3)
    void testSetup_WithSomeClassificationsInDatabase()
    {
        System.out.println("\n8: Testing setup method with some classifications in the database...");

        // Check that storedClassificationNames is empty
        Assertions.assertEquals(0, ClassificationHandler.getStoredClassificationNames().size());


        // Insert some classification into the database without using createNewClassification (which automatically increments storedClassi)
        String query = "INSERT INTO classifications (classificationName, description, deleted" + "VALUES (?, ?, ?)";
        String[] params1 = {"Physics", "Scientific literature on the topic of physics", "0"};
        String[] params2 = {"Chemistry", "Scientific literature on the topic of chemistry", "0"};
        String[] params3 = {"Horror", "The best genre", "0"};
        DatabaseHandler.executePreparedQuery(query, params1);
        DatabaseHandler.executePreparedQuery(query, params2);
        DatabaseHandler.executePreparedQuery(query, params3);


        // Call the setup method
        ClassificationHandler.setup();

        // Verify that there are the excepted amount of classifications in stored classification.
        Assertions.assertEquals(3, ClassificationHandler.getStoredClassificationNames().size());

        System.out.println("\nTEST FINISHED.");

    }
}
