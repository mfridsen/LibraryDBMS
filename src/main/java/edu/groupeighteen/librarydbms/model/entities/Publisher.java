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
public class Publisher extends Entity {

    private int publisherID;
    private String publisherName;
    private String email;

    /**
     * Creation Constructor.
     * @param publisherName
     */
    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * Retrieval Constructor.
     * @param publisherID
     * @param publisherName
     * @param email
     */
    public Publisher(int publisherID, String publisherName, String email) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
        this.email = email;
    }

    /**
     * Copy Constructor.
     * @param other
     */
    public Publisher(Publisher other) {
        this.publisherID = other.publisherID;
        this.publisherName = other.publisherName;
        this.email = other.email;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}