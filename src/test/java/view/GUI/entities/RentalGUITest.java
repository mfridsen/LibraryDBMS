package view.GUI.entities;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.view.GUI.entities.rental.RentalGUI;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/14/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalGUITest {
    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }

    public static void main(String[] args) {
        //Need to setup everything before GUI
        LibraryManager.setup();

        try {
            new RentalGUI(null, RentalHandler.createNewRental(1, 1, LocalDateTime.now()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}