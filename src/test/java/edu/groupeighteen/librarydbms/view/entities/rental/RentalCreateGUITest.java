package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/17/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalCreateGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalCreateGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        new RentalCreateGUI(null);
    }
}