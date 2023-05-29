package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/29/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class Film extends Item
{
    //Publisher Country

    public Film(String title)
    throws ConstructionException
    {
        super(title);
    }

    public Film(int itemID, String title, int allowedRentalDays, boolean available)
    throws ConstructionException
    {
        super(itemID, title, allowedRentalDays, available);
    }

    public Film(Item other)
    {
        super(other);
    }
}