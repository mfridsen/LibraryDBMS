package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/23/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class Author extends Entity {

    private int authorID;
    private String authorFirstname;
    private String authorLastName;
    private String biography;

    /**
     * Creation Constructor.
     * @param authorFirstname
     * @param authorLastName
     */
    public Author(String authorFirstname, String authorLastName) {
        this.authorID = 0;
        this.authorFirstname = authorFirstname;
        this.authorLastName = authorLastName;
    }

    /**
     * Retrieval Constructor.
     * @param authorID
     * @param authorFirstname
     * @param authorLastName
     * @param biography
     */
    //TODO-prio CALL SETTERS
    public Author(int authorID, String authorFirstname, String authorLastName, String biography) {
        this.authorID = authorID;
        this.authorFirstname = authorFirstname;
        this.authorLastName = authorLastName;
        this.biography = biography;
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public Author(Author other) {
        this.authorID = other.authorID;
        this.authorFirstname = other.authorFirstname;
        this.authorLastName = other.authorLastName;
        this.biography = other.biography;
    }


    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}