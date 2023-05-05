package model.entities;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Rental class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalTest {
    //TODO-future the DummyTestClass templates do not really adhere to DRY, since if I change this one I 
    // generally have to change those as well, look into that

    private static final String testClassTextBlock = """
               -----------------------
                Testing Rental Class \s
               -----------------------\s
            """;

    //TODO put actual valid test data here        
    private static final String[] validTestStrings =
            {"1st valid test string",
                    "2nd valid test string"
            };

    //TODO put actual invalid test data here
    private static final String[] invalidTestStrings =
            {"1st invalid test string",
                    "2nd invalid test string"
            };

    /**
     * Dummy method for validating test data.
     */
    private boolean isValidTestString(String testString) {
        return !testString.contains("invalid");
    }

    /**
     * Tests all constructors in Rental.
     */
    @Test
    @Order(1)
    public void testCreateRental() {
        System.out.println("\n" + testClassTextBlock);
        System.out.println("1: Testing creating Rental...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
    }

    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields. Technically we already tested all our getters but whatever.
     */
    @Test
    @Order(2)
    public void testGettersAndSetters() {
        System.out.println("\n2: Testing getters and setters...");
        System.out.println("No test implemented here yet!");
        //TODO Write more code here
    }

    /**
     * Dummy test method for looping through valid and invalid test data.
     */
    @Test
    @Order(3)
    public void testIsValidTestString() {
        System.out.println("THIS IS A DUMMY TEST METHOD TEMPLATE THAT LOOPS THROUGH VALID AND INVALID TEST DATA!");

        //Test valid test strings
        System.out.println("Testing valid test strings...");
        for (String validTestString : validTestStrings) {
            System.out.println(validTestString + ", should return true: " + isValidTestString(validTestString));
            assertTrue(isValidTestString(validTestString));
        }

        //Test invalid test strings
        System.out.println("Testing invalid test strings...");
        for (String invalidTestString : invalidTestStrings) {
            System.out.println(invalidTestString + ", should return false: " + isValidTestString(invalidTestString));
            assertFalse(isValidTestString(invalidTestString));
        }
    }
}