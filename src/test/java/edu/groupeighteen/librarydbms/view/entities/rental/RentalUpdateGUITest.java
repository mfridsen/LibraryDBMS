package edu.groupeighteen.librarydbms.view.entities.rental;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;

import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/15/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalUpdateGUI class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
public class RentalUpdateGUITest
{

    public static void main(String[] args)
    throws SQLException, EntityNotFoundException, EntityNotFoundException, RentalNotAllowedException,
           InvalidIDException, InvalidTypeException
    {
        LibraryManager.setup();

        new RentalUpdateGUI(null, RentalHandler.createNewRental(1, 1));


    }
}