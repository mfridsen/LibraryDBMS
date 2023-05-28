package edu.groupeighteen.librarydbms.model.entities;

import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 */
public class Classification extends Entity
        //TODO-prio METADATA AND CONSTANTS
{

    //TODO-prio add DELETED to COPY CONSTRUCTORS AND RETRIEVAL CONSTRUCTORS

    private int classificationID; //Primary key
    private String classificationName; //Varchar 255 UNIQUE NOT NULL

    private String description; //SQL TEXT

    /**
     * Creation Constructor.
     *
     * @param classificationName
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
     * Retrieval Constructor.
     *
     * @param classificationID
     * @param classificationName
     * @param description
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
     * Copy Constructor.
     *
     * @param other
     */
    public Classification(Classification other)
    {
        super(other);
        this.classificationID = other.classificationID;
        this.classificationName = other.classificationName;
        this.description = other.description;
    }

    public int getClassificationID()
    {
        return classificationID;
    }

    public void setClassificationID(int classificationID)
    throws InvalidIDException
    {
        if (classificationID <= 0)
            throw new InvalidIDException("Classification ID must be greater than 0. Received: " + classificationID);
        this.classificationID = classificationID;
    }

    public String getClassificationName()
    {
        return classificationName;
    }

    public void setClassificationName(String classificationName)
    throws InvalidNameException
    {
        if (classificationName == null || classificationName.isEmpty())
            throw new InvalidNameException("Classification name cannot be null or empty.");
        if (classificationName.length() > 255)
            throw new InvalidNameException("Classification name must be at most 255 characters. " +
                                                   "Received: " + classificationName.length());
        this.classificationName = classificationName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
