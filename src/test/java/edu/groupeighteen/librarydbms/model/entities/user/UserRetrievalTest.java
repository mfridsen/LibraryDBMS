package edu.groupeighteen.librarydbms.model.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserRetrieval class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRetrievalTest
{
    //Valid retrieval
    //Invalid ID

    //Null username
    //Empty username
    //Short username
    //Long username

    //Null password
    //Empty password
    //Short password
    //Long password

    //Null userType

    //Null email
    //Empty email
    //Short email
    //Long email

    //Invalid allowedRentals
    // < 0 currentRentals
    // currentRentals > allowedRentals

    // < 0 lateFee

    // lateFee == 0.0 && allowedRentals > currentRentals AND allowedToRent = false
    // lateFee > 0.0 AND allowedToRent = true
    // currentRentals >= defaultAllowedRentals
    // deleted == true and allowedToRent == true

    /**
     *
     */
    @Test
    @Order(1)
    void testUserRetrieval()
    {
        System.out.println("\n1: Testing UserRetrieval...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }
}