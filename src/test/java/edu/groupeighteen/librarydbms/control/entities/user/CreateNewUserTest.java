package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the CreateNewUser class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateNewUserTest extends BaseUserHandlerTest
{

    /**
     * Test case for the createNewItem method with a valid admin user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(1)
    void testCreateNewItem_ValidAdmin()
    {
        System.out.println("\n1: Testing createNewItem method with a valid admin user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a valid staff user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(2)
    void testCreateNewItem_ValidStaff()
    {
        System.out.println("\n2: Testing createNewItem method with a valid staff user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a valid patron user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(3)
    void testCreateNewItem_ValidPatron()
    {
        System.out.println("\n3: Testing createNewItem method with a valid patron user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a valid student user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(4)
    void testCreateNewItem_ValidStudent()
    {
        System.out.println("\n4: Testing createNewItem method with a valid student user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a valid teacher user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(5)
    void testCreateNewItem_ValidTeacher()
    {
        System.out.println("\n5: Testing createNewItem method with a valid teacher user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a valid researcher user.
     * This test will verify all fields and UserHandler lists.
     */
    @Test
    @Order(6)
    void testCreateNewItem_ValidResearcher()
    {
        System.out.println("\n6: Testing createNewItem method with a valid researcher user...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a duplicate username.
     */
    @Test
    @Order(7)
    void testCreateNewItem_DuplicateUsername()
    {
        System.out.println("\n7: Testing createNewItem method with a duplicate username...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a duplicate email.
     */
    @Test
    @Order(8)
    void testCreateNewItem_DuplicateEmail()
    {
        System.out.println("\n8: Testing createNewItem method with a duplicate email...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a null username.
     */
    @Test
    @Order(9)
    void testCreateNewItem_NullUsername()
    {
        System.out.println("\n9: Testing createNewItem method with a null username...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with an empty username.
     */
    @Test
    @Order(10)
    void testCreateNewItem_EmptyUsername()
    {
        System.out.println("\n10: Testing createNewItem method with an empty username...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a short username of length 3.
     */
    @Test
    @Order(11)
    void testCreateNewItem_ShortUsername()
    {
        System.out.println("\n11: Testing createNewItem method with a short username of length 3...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a long username of length 20.
     */
    @Test
    @Order(12)
    void testCreateNewItem_LongUsername()
    {
        System.out.println("\n12: Testing createNewItem method with a long username of length 20...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a null password.
     */
    @Test
    @Order(13)
    void testCreateNewItem_NullPassword()
    {
        System.out.println("\n13: Testing createNewItem method with a null password...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with an empty password.
     */
    @Test
    @Order(14)
    void testCreateNewItem_EmptyPassword()
    {
        System.out.println("\n14: Testing createNewItem method with an empty password...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a short password of length 8.
     */
    @Test
    @Order(15)
    void testCreateNewItem_ShortPassword()
    {
        System.out.println("\n15: Testing createNewItem method with a short password of length 8...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a long password of length 50.
     */
    @Test
    @Order(16)
    void testCreateNewItem_LongPassword()
    {
        System.out.println("\n16: Testing createNewItem method with a long password of length 50...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a null email.
     */
    @Test
    @Order(17)
    void testCreateNewItem_NullEmail()
    {
        System.out.println("\n17: Testing createNewItem method with a null email...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with an empty email.
     */
    @Test
    @Order(18)
    void testCreateNewItem_EmptyEmail()
    {
        System.out.println("\n18: Testing createNewItem method with an empty email...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a short email of length 6.
     */
    @Test
    @Order(19)
    void testCreateNewItem_ShortEmail()
    {
        System.out.println("\n19: Testing createNewItem method with a short email of length 6...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a long email of length 255.
     */
    @Test
    @Order(20)
    void testCreateNewItem_LongEmail()
    {
        System.out.println("\n20: Testing createNewItem method with a long email of length 255...");


        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for the createNewItem method with a null userType.
     */
    @Test
    @Order(21)
    void testCreateNewItem_NullUserType()
    {
        System.out.println("\n21: Testing createNewItem method with a null userType...");


        System.out.println("\nTEST FINISHED.");
    }
}