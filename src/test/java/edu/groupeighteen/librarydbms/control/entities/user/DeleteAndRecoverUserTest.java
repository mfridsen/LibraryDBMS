package edu.groupeighteen.librarydbms.control.entities.user;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
public class DeleteAndRecoverUserTest extends BaseUserHandlerTest
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

            //Make sure our users are set correctly
            lateFeeUser.setLateFee(1);
            currentRentalsUser.setCurrentRentals(1);
            nonExistingUser.setUserID(9999);
        }
        catch (CreationException | ConstructionException | InvalidLateFeeException
               | InvalidUserRentalsException | InvalidIDException e)
        {
            e.printStackTrace();
            fail("User initialization failed due to: " + e.getCause().getClass().getName()
                    + ". Message: " + e.getMessage());
        }

        System.out.println("\nUSERS INITIALIZED.");
    }

    /**
     * And now we can reset the table and the users back to original states after tests.
     */
    @Override
    @AfterEach
    protected void reset()
    {
        super.reset();
        resetUsersTable();
        initializeUsers();
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
                System.out.println("Testing to delete " + user.getUsername() + ".");

                //Assert user is NOT deleted
                assertFalse(user.isDeleted());
                assertNotNull(UserHandler.getUserByID(user.getUserID()));

                //Delete user
                assertDoesNotThrow(() -> UserHandler.deleteUser(user));

                //Assert user is deleted
                assertTrue(user.isDeleted());

                //Assert user is not allowed to rent
                assertFalse(user.isAllowedToRent());

                //Standard retrieval should NOT return user since it's deleted
                assertNull(UserHandler.getUserByID(user.getUserID()));

                //getDeleted true retrieval should return user
                User retrievedUser = UserHandler.getUserByID(user.getUserID(), true);
                assertNotNull(retrievedUser);
                assertTrue(retrievedUser.isDeleted());
                assertFalse(retrievedUser.isAllowedToRent());

                //Assert username and email still exist in lists
                assertTrue(UserHandler.getStoredUsernames().contains(user.getUsername()));
                assertTrue(UserHandler.getRegisteredEmails().contains(user.getEmail()));

                System.out.println(user.getUsername() + " deleted.");
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

        //Delete once
        assertDoesNotThrow(() -> UserHandler.deleteUser(patron));
        assertTrue(patron.isDeleted());

        //Delete again, should throw DeletionException with cause EntityNotFoundException
        Exception e = assertThrows(DeletionException.class, () -> UserHandler.deleteUser(patron));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

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

        //Delete nonExistingUser, should throw DeletionException with cause EntityNotFoundException
        Exception e = assertThrows(DeletionException.class, () -> UserHandler.deleteUser(nonExistingUser));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

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

        //Delete null, should throw DeletionException with cause NullEntityException
        Exception e = assertThrows(DeletionException.class, () -> UserHandler.deleteUser(null));
        assertTrue(e.getCause() instanceof NullEntityException);

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

        //Delete lateFeeUser, should throw DeletionException with cause InvalidLateFeeException
        Exception e = assertThrows(DeletionException.class, () -> UserHandler.deleteUser(lateFeeUser));
        assertTrue(e.getCause() instanceof InvalidLateFeeException);

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

        //Delete currentRentalsUser, should throw DeletionException with cause InvalidUserRentalsException
        Exception e = assertThrows(DeletionException.class, () -> UserHandler.deleteUser(currentRentalsUser));
        assertTrue(e.getCause() instanceof InvalidUserRentalsException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests recovering a deleted user of each type.
     */
    @Test
    @Order(7)
    void testRecoverUser_deletedUsers()
    {
        System.out.println("\n7: Testing recoverUser method with deleted users of each type...\n");

        //Delete users
        for (User user : users)
        {
            try
            {
                System.out.println("Deleting " + user.getUsername());

                //Assert user is NOT deleted
                assertFalse(user.isDeleted());
                assertNotNull(UserHandler.getUserByID(user.getUserID()));

                //Delete user
                UserHandler.deleteUser(user);

                System.out.println(user.getUsername() + " deleted.");
            }
            catch (InvalidIDException | DeletionException e)
            {
                e.printStackTrace();
                fail("Exception thrown when deleting user " + user.getUsername() + ": " + e.getMessage());
            }
        }

        //Recover users
        for (User user : users)
        {
            try
            {
                System.out.println("Testing to recover " + user.getUsername());

                //Assert user is deleted
                assertTrue(user.isDeleted());
                assertNull(UserHandler.getUserByID(user.getUserID()));

                //Recover user
                UserHandler.recoverUser(user);

                //Assert user is NOT deleted
                assertFalse(user.isDeleted());

                //Standard retrieval should return user
                assertNotNull(UserHandler.getUserByID(user.getUserID()));

                //getDeleted true retrieval should also return user
                User retrievedUser = UserHandler.getUserByID(user.getUserID(), true);
                assertNotNull(retrievedUser);
                assertFalse(retrievedUser.isDeleted());

                //Admin and staff are not allowed to rent
                if (retrievedUser.getUserType() == User.UserType.ADMIN || retrievedUser.getUserType() == User.UserType.STAFF)
                    assertFalse(retrievedUser.isAllowedToRent());
                    //The rest are
                else
                    assertTrue(retrievedUser.isAllowedToRent());

                //Assert username and email still exist in lists
                assertTrue(UserHandler.getStoredUsernames().contains(user.getUsername()));
                assertTrue(UserHandler.getRegisteredEmails().contains(user.getEmail()));

                System.out.println(user.getUsername() + " recovered.");
            }
            catch (InvalidIDException | RecoveryException e)
            {
                e.printStackTrace();
                fail("Exception thrown when recovering user " + user.getUsername() + ": " + e.getMessage());
            }
        }

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

        try
        {
            //Assert user is NOT deleted
            assertFalse(patron.isDeleted());

            //Recover user
            UserHandler.recoverUser(patron);

            //Standard retrieval should return user
            assertNotNull(UserHandler.getUserByID(patron.getUserID()));

            //getDeleted true retrieval should also return user
            User retrievedUser = UserHandler.getUserByID(patron.getUserID(), true);
            assertNotNull(retrievedUser);
            assertFalse(retrievedUser.isDeleted());

        }
        catch (InvalidIDException | RecoveryException e)
        {
            e.printStackTrace();
            fail("Exception thrown when recovering user " + patron.getUsername() + ": " + e.getMessage());
        }

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

        try
        {
            //Assert user does not exist in database
            assertNull(UserHandler.getUserByID(nonExistingUser.getUserID()));
            Exception e = assertThrows(RecoveryException.class, () -> UserHandler.recoverUser(nonExistingUser));
            assertTrue(e.getCause() instanceof EntityNotFoundException);
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
            fail("Exception thrown when recovering user " + patron.getUsername() + ": " + e.getMessage());
        }

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

        Exception e = assertThrows(RecoveryException.class, () -> UserHandler.recoverUser(null));
        assertTrue(e.getCause() instanceof NullEntityException);

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

        for (User user : users)
        {
            try
            {
                //Assert user not deleted
                assertFalse(user.isDeleted());

                //Assert user exists in database
                assertNotNull(UserHandler.getUserByID(user.getUserID()));

                //Hard delete user
                UserHandler.hardDeleteUser(user);

                //Assert user is deleted
                assertTrue(user.isDeleted());

                //Assert user does not exist in database with both settings
                assertNull(UserHandler.getUserByID(user.getUserID()));
                assertNull(UserHandler.getUserByID(user.getUserID(), false));

                //Assert deleted and not allowed to rent
                assertTrue(user.isDeleted());
                assertFalse(user.isAllowedToRent());

                //Assert username and email don't exist in lists
                assertFalse(UserHandler.getStoredUsernames().contains(user.getUsername()));
                assertFalse(UserHandler.getRegisteredEmails().contains(user.getEmail()));
            }
            catch (InvalidIDException | DeletionException e)
            {
                e.printStackTrace();
                if (e instanceof DeletionException)
                    fail("Exception thrown when deleting user " + user.getUsername() + " due to: "
                            + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
                else
                    fail("Exception thrown when deleting user " + user.getUsername() + ": " + e.getMessage());
            }
        }

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

        try
        {
            //Assert not deleted
            assertFalse(patron.isDeleted());

            //Assert exists in database
            assertNotNull(UserHandler.getUserByID(patron.getUserID()));

            //Delete user
            UserHandler.deleteUser(patron);

            //Assert deleted
            assertTrue(patron.isDeleted());

            //Assert exists in database
            assertNull(UserHandler.getUserByID(patron.getUserID()));
            assertNotNull(UserHandler.getUserByID(patron.getUserID(), true));

            //Hard delete
            UserHandler.hardDeleteUser(patron);

            //Assert does not exist in database
            assertNull(UserHandler.getUserByID(patron.getUserID()));
            assertNull(UserHandler.getUserByID(patron.getUserID(), true));

            //Assert deleted and not allowed to rent
            assertTrue(patron.isDeleted());
            assertFalse(patron.isAllowedToRent());

            //Assert username and email don't exist in lists
            assertFalse(UserHandler.getStoredUsernames().contains(patron.getUsername()));
            assertFalse(UserHandler.getRegisteredEmails().contains(patron.getEmail()));
        }
        catch (InvalidIDException | DeletionException e)
        {
            e.printStackTrace();
            if (e instanceof DeletionException)
                fail("Exception thrown when deleting user " + patron.getUsername() + " due to: "
                        + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
            else
                fail("Exception thrown when deleting user " + patron.getUsername() + ": " + e.getMessage());
        }

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

        Exception e = assertThrows(DeletionException.class, () -> UserHandler.hardDeleteUser(nonExistingUser));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

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

        Exception e = assertThrows(DeletionException.class, () -> UserHandler.hardDeleteUser(null));
        assertTrue(e.getCause() instanceof NullEntityException);

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

        Exception e = assertThrows(DeletionException.class, () -> UserHandler.hardDeleteUser(lateFeeUser));
        assertTrue(e.getCause() instanceof InvalidLateFeeException);

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

        Exception e = assertThrows(DeletionException.class, () -> UserHandler.hardDeleteUser(currentRentalsUser));
        assertTrue(e.getCause() instanceof InvalidUserRentalsException);

        System.out.println("\nTEST FINISHED.");
    }
}