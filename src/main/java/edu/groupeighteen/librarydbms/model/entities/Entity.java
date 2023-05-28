package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public abstract class Entity
{

    /**
     * Creation Constructor.
     */
    public Entity()
    {
        this.deleted = false; //Newly created entities are not deleted by default
    }

    /**
     * Retrieval Constructor.
     *
     * @param deleted
     */
    public Entity(boolean deleted)
    {
        this.deleted = deleted;
    }

    /**
     * Copy Constructor.
     *
     * @param other
     */
    public Entity(Entity other)
    {
        this.deleted = other.deleted;
    }

    protected boolean deleted;

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }
}