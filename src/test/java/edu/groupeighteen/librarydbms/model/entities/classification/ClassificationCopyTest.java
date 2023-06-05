package edu.groupeighteen.librarydbms.model.entities.classification;

import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/29/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test class for the copy constructor of the Classification class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationCopyTest
{
    /**
     * Tests the copy constructor of the Classification class with valid data.
     * This method creates a copy of a Classification and checks that the fields of the copy match those of the original.
     */
    @Test
    @Order(1)
    void testCopyConstructor_ValidData()
    {
        System.out.println("\n1: Testing the copy constructor of the Classification class with valid data...");

        try
        {
            Classification original = new Classification(1, "Fiction",
                    "A type of literary genre", false);
            Classification copy = new Classification(original);

            assertEquals(original.getClassificationID(), copy.getClassificationID(),
                    "The Classification ID of the copy should match the original");
            assertEquals(original.getClassificationName(), copy.getClassificationName(),
                    "The Classification name of the copy should match the original");
            assertEquals(original.getDescription(), copy.getDescription(),
                    "The Classification description of the copy should match the original");
            assertEquals(original.isDeleted(), copy.isDeleted(),
                    "The Classification deleted status of the copy should match the original");
        }
        catch (ConstructionException e)
        {
            fail("Valid operations should not throw exceptions. " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}