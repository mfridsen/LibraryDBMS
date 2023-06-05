package edu.groupeighteen.librarydbms.model.entities.classification;

import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/29/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test class for the setter methods of the Classification class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationSetterTest
{
    /**
     * Tests the setClassificationID method with a valid ID.
     * This method sets an ID to a classification and verifies that the ID field is set correctly.
     */
    @Test
    @Order(1)
    void testSetClassificationID_ValidID()
    {
        System.out.println("\n1: Testing setClassificationID method with a valid ID...");

        try
        {
            int id = 1;
            Classification classification = new Classification("Fiction");
            classification.setClassificationID(id);
            assertEquals(id, classification.getClassificationID(), "Classification's ID should match the set ID");
        }
        catch (InvalidIDException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationID method with an invalid ID.
     * This method attempts to set an invalid ID to a classification and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testSetClassificationID_InvalidID()
    {
        System.out.println("\n2: Testing setClassificationID method with an invalid ID...");

        try
        {
            int id = -1;
            Classification classification = new Classification("Fiction");

            assertThrows(InvalidIDException.class, () -> classification.setClassificationID(id),
                    "Expected InvalidIDException for invalid ID");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationName method with a valid name.
     * This method sets a name to a classification and verifies that the name field is set correctly.
     */
    @Test
    @Order(3)
    void testSetClassificationName_ValidName()
    {
        System.out.println("\n3: Testing setClassificationName method with a valid name...");

        try
        {
            String name = "Fiction";
            Classification classification = new Classification(name);
            classification.setClassificationName(name);
            assertEquals(name, classification.getClassificationName(),
                    "Classification's name should match the set name");
        }
        catch (InvalidNameException | ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setClassificationName method with an invalid name.
     * This method attempts to set an invalid name to a classification and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(4)
    void testSetClassificationName_InvalidName()
    {
        System.out.println("\n4: Testing setClassificationName method with an invalid name...");

        try
        {
            String name = "a".repeat(Classification.CLASSIFICATION_NAME_LENGTH + 1);
            Classification classification = new Classification("Fiction");

            assertThrows(InvalidNameException.class, () -> classification.setClassificationName(name),
                    "Expected InvalidNameException for invalid name");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the setDescription method.
     * This method sets a description to a classification and verifies that the description field is set correctly.
     */
    @Test
    @Order(5)
    void testSetDescription()
    {
        System.out.println("\n5: Testing setDescription method...");

        try
        {
            String description = "A category for Fiction books.";
            Classification classification = new Classification("Fiction");
            classification.setDescription(description);
            assertEquals(description, classification.getDescription(),
                    "Classification's description should match the set description");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}