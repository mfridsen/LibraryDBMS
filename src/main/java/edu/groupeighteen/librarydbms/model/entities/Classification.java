package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 */
public class Classification extends Entity {

    //TODO-prio add DELETED to COPY CONSTRUCTORS AND RETRIEVAL CONSTRUCTORS

    private int classificationID; //Primary key
    private String classificationName; //Varchar 255 UNIQUE NOT NULL

    private String description; //SQL TEXT

    /**
     * Creation Constructor.
     * @param classificationName
     */
    public Classification(String classificationName) {
        this.classificationName = classificationName;
    }

    /**
     * Retrieval Constructor.
     * @param classificationID
     * @param classificationName
     * @param description
     */
    public Classification(int classificationID, String classificationName, String description) {
        setClassificationID(classificationID);
        setClassificationName(classificationName);
        setDescription(description);
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public Classification(Classification other) {
        this.classificationID = other.classificationID;
        this.classificationName = other.classificationName;
        this.description = other.description;
    }

    public int getClassificationID() {
        return classificationID;
    }

    public void setClassificationID(int classificationID) {
        this.classificationID = classificationID;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
