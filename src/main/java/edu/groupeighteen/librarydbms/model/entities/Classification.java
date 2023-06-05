package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 * <p>
 * Represents a Classification in a publishing system.
 * The Classification class extends Entity and contains information about the classification, including
 * classification name and description.
 * It provides methods to set and get these values, enforcing various constraints.
 */
public class Classification extends Entity
{
    /**
     * Constant defining the maximum length of the classification name.
     */
    public static final int CLASSIFICATION_NAME_LENGTH;

    /*
     * Helps with adherence to DRY, now if we want to change the rules we only need to do so in one place.
     */
    static
    {
        int[] metaData = DatabaseHandler.getClassificationMetaData();
        CLASSIFICATION_NAME_LENGTH = metaData[0];
    }

    /**
     * Unique identifier of the classification. Must be greater than 0.
     */
    private int classificationID;

    /**
     * Classification's name. Must not be null or empty, and must be unique.
     */
    private String classificationName;

    /**
     * Classification's description.
     */
    private String description;

    /**
     * Constructor for creating a new Classification.
     *
     * @param classificationName The name of the classification.
     * @throws ConstructionException if the classification's name is null, empty, or too long.
     */
    public Classification(String classificationName)
    throws ConstructionException
    {
        super();
        try
        {
            this.classificationID = 0;
            setClassificationName(classificationName);
            this.description = null;
        }
        catch (Exception e)
        {
            throw new ConstructionException("Classification Creation Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Constructor for retrieving an existing Classification.
     *
     * @param classificationID   The unique identifier of the classification.
     * @param classificationName The name of the classification.
     * @param description        The description of the classification.
     * @param deleted            Indicates whether the classification is deleted.
     * @throws ConstructionException if the classificationID is invalid or if the classification's name is null,
     *                               empty, or too long.
     */
    public Classification(int classificationID, String classificationName, String description, boolean deleted)
    throws ConstructionException
    {
        super(deleted);
        try
        {
            setClassificationID(classificationID);
            setClassificationName(classificationName);
            setDescription(description);
        }
        catch (InvalidIDException | InvalidNameException e)
        {
            throw new ConstructionException("Classification Retrieval Construction failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Copy constructor for Classification.
     *
     * @param other The Classification object to be copied.
     */
    public Classification(Classification other)
    {
        super(other);
        this.classificationID = other.classificationID;
        this.classificationName = other.classificationName;
        this.description = other.description;
    }

    /**
     * Returns the unique identifier of the classification.
     *
     * @return The classification's ID.
     */
    public int getClassificationID()
    {
        return classificationID;
    }

    /**
     * Sets the unique identifier of the classification.
     *
     * @param classificationID The classification's ID.
     * @throws InvalidIDException if the classificationID is not greater than 0.
     */
    public void setClassificationID(int classificationID)
    throws InvalidIDException
    {
        if (classificationID <= 0)
            throw new InvalidIDException("Classification ID must be greater than 0. Received: " + classificationID);
        this.classificationID = classificationID;
    }

    /**
     * Returns the name of the classification.
     *
     * @return The classification's name.
     */
    public String getClassificationName()
    {
        return classificationName;
    }

    /**
     * Sets the name of the classification.
     *
     * @param classificationName The classification's name.
     * @throws InvalidNameException if the name is null, empty, or too long.
     */
    public void setClassificationName(String classificationName)
    throws InvalidNameException
    {
        if (classificationName == null || classificationName.isEmpty())
            throw new InvalidNameException("Classification name cannot be null or empty.");
        if (classificationName.length() > CLASSIFICATION_NAME_LENGTH)
            throw new InvalidNameException("Classification name must be at most " + CLASSIFICATION_NAME_LENGTH +
                    " characters. Received: " + classificationName.length());
        this.classificationName = classificationName;
    }

    /**
     * Returns the description of the classification.
     *
     * @return The classification's description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description of the classification.
     *
     * @param description The classification's description.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}