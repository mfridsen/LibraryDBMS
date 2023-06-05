package edu.groupeighteen.librarydbms.model.entities.classification;

import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
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
 * Test class for Classification creation constructor.
 * This class aims to test the behavior of the Classification creation constructor under various circumstances,
 * including valid and invalid inputs.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationCreationTest
{
    /**
     * Tests the Classification creation constructor with valid data.
     * This method creates a Classification with a valid name and verifies that the fields are set correctly.
     */
    @Test
    @Order(1)
    void testCreationConstructor_ValidData()
    {
        System.out.println("\n1: Testing Classification creation constructor with valid data...");

        try
        {
            String name = "Fiction";
            Classification classification = new Classification(name);
            assertFalse(classification.isDeleted());
            assertEquals(name, classification.getClassificationName(), "Classification name should match the set name");
            assertEquals(0, classification.getClassificationID(),
                    "Classification ID should be 0 for newly created Classification");
            assertNull(classification.getDescription(), "Description should be null for newly created Classification");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Classification creation constructor with a null name.
     * This method attempts to create a Classification with a null name and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(2)
    void testCreationConstructor_NullName()
    {
        System.out.println("\n2: Testing Classification creation constructor with a null name...");

        String name = null;
        assertThrows(ConstructionException.class, () -> new Classification(name),
                "Expected ConstructionException for null Classification name");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Classification creation constructor with a name that is too long.
     * This method attempts to create a Classification with a name that exceeds the maximum length and checks that the appropriate exception is thrown.
     */
    @Test
    @Order(3)
    void testCreationConstructor_LongName()
    {
        System.out.println("\n3: Testing Classification creation constructor with a long name...");

        String name = "a".repeat(Classification.CLASSIFICATION_NAME_LENGTH + 1);
        assertThrows(ConstructionException.class, () -> new Classification(name),
                "Expected ConstructionException for long Classification name");

        System.out.println("\nTEST FINISHED.");
    }
}
