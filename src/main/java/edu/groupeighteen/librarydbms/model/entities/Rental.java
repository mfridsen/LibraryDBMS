package edu.groupeighteen.librarydbms.model.entities;

import java.time.LocalDateTime;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 4/27/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 */
public class Rental {

    //TODO-future add more fields and methods
    //TODO-comment everything

    private int rentalID; //Primary key
    private int userID; //Foreign key referencing User
    private int itemID; //Foreign key referencing Item
    private LocalDateTime rentalDate; //Date of creation

    public Rental() {

    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/


}