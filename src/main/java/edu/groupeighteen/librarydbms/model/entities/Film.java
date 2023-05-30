package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidAgeRatingException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;

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
    public static final int FILM_COUNTRY_LENGTH;
    public static final int FILM_MAX_AGE_RATING = 18;

    static
    {
        int[] metaData = DatabaseHandler.getFilmMetaData();
        FILM_COUNTRY_LENGTH = metaData[0];
    }

    private int ageRating;
    private String publisherCountry; //Can be null, since it's possible we might not know the origin of a movie
    private String listOfActors; //Not an actual list, cause databases. Actors entity is too much bother for now


    public Film(String title, int authorID, int classificationID, String barcode, int ageRating)
    throws ConstructionException
    {
        super(title, ItemType.FILM, barcode, authorID, classificationID);
        try
        {
            setAgeRating(ageRating);
            this.publisherCountry = null;
            this.listOfActors = null;
        }
        catch (InvalidAgeRatingException e)
        {
            throw new ConstructionException("Film Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }



    public Film(boolean deleted, int itemID, String title, ItemType type, String barcode, int authorID,
                int classificationID, String authorName, String classificationName, int allowedRentalDays,
                boolean available, int ageRating, String publisherCountry, String listOfActors)
    throws ConstructionException
    {
        super(deleted, itemID, title, type, barcode, authorID, classificationID, authorName, classificationName,
                allowedRentalDays, available);
        try
        {
            setAgeRating(ageRating);
            setPublisherCountry(publisherCountry);
            setListOfActors(listOfActors);
        }
        catch (InvalidAgeRatingException | InvalidNameException e)
        {
            throw new ConstructionException("Film Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }


    public Film(Film other)
    {
        super(other);
        this.ageRating = other.ageRating;
        this.publisherCountry = other.publisherCountry;
        this.listOfActors = other.listOfActors;
    }

    public int getAgeRating()
    {
        return ageRating;
    }

    public void setAgeRating(int ageRating)
    throws InvalidAgeRatingException
    {
        if (ageRating < 0)
            throw new InvalidAgeRatingException("Cannot set an age rating lower than 0.");
        if (ageRating > FILM_MAX_AGE_RATING)
            throw new InvalidAgeRatingException("Cannot set an age rating higher than " + FILM_MAX_AGE_RATING + ".");
        this.ageRating = ageRating;
    }

    public String getPublisherCountry()
    {
        return publisherCountry;
    }

    public void setPublisherCountry(String publisherCountry)
    throws InvalidNameException
    {
        if (publisherCountry.length() > FILM_COUNTRY_LENGTH)
            throw new InvalidNameException("Film country name cannot be greater than " + FILM_COUNTRY_LENGTH + ".");
        this.publisherCountry = publisherCountry;
    }

    public String getListOfActors()
    {
        return listOfActors;
    }

    public void setListOfActors(String listOfActors)
    {
        this.listOfActors = listOfActors;
    }
}