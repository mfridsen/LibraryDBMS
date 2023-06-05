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
 * Represents a film item.
 * Extends the Item class.
 */
public class Film extends Item
{
    /**
     * The length of the film country name.
     */
    public static final int FILM_COUNTRY_LENGTH;

    /**
     * The maximum age rating for a film.
     */
    public static final int FILM_MAX_AGE_RATING = 18;

    /*
     * This block initializes the FILM_COUNTRY_LENGTH from the database metadata.
     * The first value of the retrieved metadata array is assumed to be the FILM_COUNTRY_LENGTH.
     */
    static
    {
        int[] metaData = DatabaseHandler.getFilmMetaData();
        FILM_COUNTRY_LENGTH = metaData[0];
    }

    private int ageRating;
    private String countryOfProduction; //Can be null, since it's possible we might not know the origin of a movie
    private String listOfActors; //Not an actual list, cause databases. Actors entity is too much bother for now

    /**
     * Creation Constructor. Constructs a Film object with the given parameters.
     *
     * @param title            the title of the film
     * @param authorID         the ID of the author
     * @param classificationID the ID of the classification
     * @param barcode          the barcode of the film
     * @param ageRating        the age rating of the film
     * @throws ConstructionException if the construction fails
     */
    public Film(String title, int authorID, int classificationID, String barcode, int ageRating)
    throws ConstructionException
    {
        super(title, ItemType.FILM, barcode, authorID, classificationID);
        try
        {
            setAgeRating(ageRating);
            this.countryOfProduction = null;
            this.listOfActors = null;
        }
        catch (InvalidAgeRatingException e)
        {
            throw new ConstructionException("Film Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieval Constructor. Constructs a Film object with the given parameters.
     *
     * @param deleted             true if the film is deleted, false otherwise
     * @param itemID              the ID of the film
     * @param title               the title of the film
     * @param type                the type of the film
     * @param barcode             the barcode of the film
     * @param authorID            the ID of the author
     * @param classificationID    the ID of the classification
     * @param authorFirstname     Name of the author or director of the item.
     * @param authorLastname      Last name of the author or director of the item.
     * @param classificationName  the name of the classification
     * @param allowedRentalDays   the number of allowed rental days
     * @param available           true if the film is available, false otherwise
     * @param ageRating           the age rating of the film
     * @param countryOfProduction the country of the film's publisher
     * @param listOfActors        the list of actors in the film
     * @throws ConstructionException if the construction fails
     */
    public Film(boolean deleted, int itemID, String title, ItemType type, String barcode, int authorID,
                int classificationID, String authorFirstname, String authorLastname, String classificationName,
                int allowedRentalDays, boolean available, int ageRating, String countryOfProduction,
                String listOfActors)
    throws ConstructionException
    {
        super(deleted, itemID, title, type, barcode, authorID, classificationID, authorFirstname, authorLastname,
                classificationName, allowedRentalDays, available);
        try
        {
            setAgeRating(ageRating);
            setCountryOfProduction(countryOfProduction);
            setListOfActors(listOfActors);
        }
        catch (InvalidAgeRatingException | InvalidNameException e)
        {
            throw new ConstructionException("Film Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructs a Film object by copying another Film object.
     *
     * @param other the Film object to copy
     */
    public Film(Film other)
    {
        super(other);
        this.ageRating = other.ageRating;
        this.countryOfProduction = other.countryOfProduction;
        this.listOfActors = other.listOfActors;
    }

    /**
     * Retrieves the age rating of the film.
     *
     * @return the age rating
     */
    public int getAgeRating()
    {
        return ageRating;
    }

    /**
     * Sets the age rating of the film.
     *
     * @param ageRating the age rating to set
     * @throws InvalidAgeRatingException if the age rating is invalid
     */
    public void setAgeRating(int ageRating)
    throws InvalidAgeRatingException
    {
        if (ageRating < 0)
            throw new InvalidAgeRatingException("Cannot set an age rating lower than 0.");
        if (ageRating > FILM_MAX_AGE_RATING)
            throw new InvalidAgeRatingException("Cannot set an age rating higher than " + FILM_MAX_AGE_RATING + ".");
        this.ageRating = ageRating;
    }

    /**
     * Retrieves the country of the film's publisher.
     *
     * @return the publisher country
     */
    public String getCountryOfProduction()
    {
        return countryOfProduction;
    }

    /**
     * Sets the country of the film's publisher.
     *
     * @param countryOfProduction the publisher country to set
     * @throws InvalidNameException if the publisher country name is invalid
     */
    public void setCountryOfProduction(String countryOfProduction)
    throws InvalidNameException
    {
        if (countryOfProduction != null && countryOfProduction.length() > FILM_COUNTRY_LENGTH)
            throw new InvalidNameException("Film country name cannot be greater than " + FILM_COUNTRY_LENGTH + ".");
        this.countryOfProduction = countryOfProduction;
    }

    /**
     * Retrieves the list of actors in the film.
     *
     * @return the list of actors
     */
    public String getListOfActors()
    {
        return listOfActors;
    }

    /**
     * Sets the list of actors in the film.
     *
     * @param listOfActors the list of actors to set
     */
    public void setListOfActors(String listOfActors)
    {
        this.listOfActors = listOfActors;
    }
}