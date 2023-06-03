package edu.groupeighteen.librarydbms.control.entities.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.CreationException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;
import org.junit.jupiter.api.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/2/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the DeleteUser class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteAndRecoverUserTest
{
    //A nice batch of user objects to use over and over
    private static User admin;
    private static User staff;
    private static User patron;
    private static User student;
    private static User teacher;
    private static User researcher;
    private static User lateFeeUser;
    private static User currentRentalsUser;
    private static User nonExistingUser; //CONSTRUCTOR NOT createNewUser

    private static User[] users;

    /**
     * Let's setup the needed users ahead of time.
     */
    @BeforeAll
    protected static void customSetup()
    {
        initializeUsers();
    }

    /**
     * And now we can reset them back to original states after tests.
     */
    void resetUsers()
    {
        initializeUsers();
    }

    /**
     * Initializes the batch and prepares them for testing. Further preparation might be needed in specific tests.
     */
    protected static void initializeUsers()
    {
        System.out.println("\nInitializing users...");

        try
        {
            admin = UserHandler.createNewUser("admin", "adminPass",
                    "admin@mail.com", User.UserType.ADMIN);
            staff = UserHandler.createNewUser("staff", "staffPass",
                    "staff@mail.com", User.UserType.STAFF);
            patron = UserHandler.createNewUser("patron", "patronPass",
                    "patron@mail.com", User.UserType.PATRON);
            student = UserHandler.createNewUser("student", "studentPass",
                    "student@mail.com", User.UserType.STUDENT);
            teacher = UserHandler.createNewUser("teacher", "teacherPass",
                    "teacher@mail.com", User.UserType.TEACHER);
            researcher = UserHandler.createNewUser("researcher", "researcherPass",
                    "researcher@mail.com", User.UserType.RESEARCHER);

            users = new User[]{admin, staff, patron, student, teacher, researcher};

            lateFeeUser = UserHandler.createNewUser("lateFeeUser", "lateFeeUserPass",
                    "lateFeeUser@mail.com", User.UserType.PATRON);
            currentRentalsUser = UserHandler.createNewUser("currentRentalsUser", "currentRentalsUserPass",
                    "currentRentalsUser@mail.com", User.UserType.PATRON);
            nonExistingUser = new User("nonExistingUser", "nonExistingPassword",
                    "nonExisting@mail.com", User.UserType.TEACHER); //CONSTRUCTOR NOT createNewUser

            //Make sure our users have fees and current rentals
            lateFeeUser.setLateFee(1);
            currentRentalsUser.setCurrentRentals(1);
        }
        catch (CreationException | ConstructionException | InvalidLateFeeException | InvalidUserRentalsException e)
        {
            e.printStackTrace();
            fail("User initialization failed due to: " + e.getCause().getClass().getName()
            + ". Message: " + e.getMessage());
        }

        System.out.println("\nUSERS INITIALIZED.");
    }

    /**
     * Tests deleting a valid existing user of each type.
     */
    @Test
    @Order(1)
    void testDeleteUser_validExistingUsers()
    {
        System.out.println("\n1: Testing deleteUser method with valid existing users of each type...\n");

        for (User user : users)
        {
            try
            {
                System.out.println("Testing to delete " + user.getUsername());
                //Assert user is NOT deleted
                assertFalse(user.isDeleted());
                assertNotNull(UserHandler.getUserByID(user.getUserID()));

                //Delete user
                assertDoesNotThrow(() -> UserHandler.deleteUser(user));

                //Assert user is deleted
                assertTrue(user.isDeleted());

                //Standard retrieval should NOT return user since it's deleted
                assertNull(UserHandler.getUserByID(user.getUserID()));

                //getDeleted true retrieval should return user
                User retrievedUser = UserHandler.getUserByID(user.getUserID(), true);
                assertNotNull(retrievedUser);
                assertTrue(retrievedUser.isDeleted());
            }
            catch (InvalidIDException e)
            {
                e.printStackTrace();
                fail("Exception thrown when testing to delete user " + user.getUsername() + ": " + e.getMessage());
            }
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests deleting an already deleted user.
     */
    @Test
    @Order(2)
    void testDeleteUser_deletedUser()
    {
        System.out.println("\n2: Testing deleteUser method with an already deleted user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests deleting a non-existing user.
     */
    @Test
    @Order(3)
    void testDeleteUser_nonExistingUser()
    {
        System.out.println("\n3: Testing deleteUser method with a non-existing user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests deleting a null user.
     */
    @Test
    @Order(4)
    void testDeleteUser_nullUser()
    {
        System.out.println("\n4: Testing deleteUser method with null user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests deleting a user with late fee.
     */
    @Test
    @Order(5)
    void testDeleteUser_userWithLateFee()
    {
        System.out.println("\n5: Testing deleteUser method with a user who has late fees...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests deleting a user with current rentals.
     */
    @Test
    @Order(6)
    void testDeleteUser_userWithCurrentRentals()
    {
        System.out.println("\n6: Testing deleteUser method with a user who has current rentals...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests recovering a deleted user of each type.
     */
    @Test
    @Order(7)
    void testRecoverUser_deletedUsers()
    {
        System.out.println("\n7: Testing recoverUser method with deleted users of each type...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests recovering a non-deleted user.
     */
    @Test
    @Order(8)
    void testRecoverUser_nonDeletedUser()
    {
        System.out.println("\n8: Testing recoverUser method with a non-deleted user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests recovering a non-existing user.
     */
    @Test
    @Order(9)
    void testRecoverUser_nonExistingUser()
    {
        System.out.println("\n9: Testing recoverUser method with a non-existing user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests recovering a null user.
     */
    @Test
    @Order(10)
    void testRecoverUser_nullUser()
    {
        System.out.println("\n10: Testing recoverUser method with null user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a valid existing user of each type.
     */
    @Test
    @Order(11)
    void testHardDeleteUser_validExistingUsers()
    {
        System.out.println("\n11: Testing hardDeleteUser method with valid existing users of each type...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a deleted user.
     */
    @Test
    @Order(12)
    void testHardDeleteUser_deletedUser()
    {
        System.out.println("\n12: Testing hardDeleteUser method with an already deleted user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a non-existing user.
     */
    @Test
    @Order(13)
    void testHardDeleteUser_nonExistingUser()
    {
        System.out.println("\n13: Testing hardDeleteUser method with a non-existing user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a null user.
     */
    @Test
    @Order(14)
    void testHardDeleteUser_nullUser()
    {
        System.out.println("\n14: Testing hardDeleteUser method with null user...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a user with late fee.
     */
    @Test
    @Order(15)
    void testHardDeleteUser_userWithLateFee()
    {
        System.out.println("\n15: Testing hardDeleteUser method with a user who has late fees...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests hard deleting a user with current rentals.
     */
    @Test
    @Order(16)
    void testHardDeleteUser_userWithCurrentRentals()
    {
        System.out.println("\n16: Testing hardDeleteUser method with a user who has current rentals...");

        // implementation goes here

        System.out.println("\nTEST FINISHED.");
    }
}