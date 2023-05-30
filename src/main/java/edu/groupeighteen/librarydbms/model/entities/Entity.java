package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/18/2023
 * <p>
 * This class represents a generic entity object in a library database management system.
 * Each entity in the system is either active or deleted, a state that is stored internally within each entity.
 * This abstract class can be extended to create specific entities with additional properties and behavior.
 */
public abstract class Entity
{
    /**
     * Variable to store the deletion status of the entity.
     */
    protected boolean deleted;

    /**
     * Default constructor for Entity.
     * Constructs a new entity object with a default 'deleted' state of 'false', indicating it's active.
     */
    public Entity()
    {
        this.deleted = false; //Newly created entities are not deleted by default
    }

    /**
     * Constructs an entity object using the provided deleted status.
     *
     * @param deleted a boolean indicating whether this entity has been deleted or not.
     */
    public Entity(boolean deleted)
    {
        this.deleted = deleted;
    }

    /**
     * Copy constructor for Entity.
     * Constructs a new entity object by copying the state of another entity.
     *
     * @param other the other entity object to copy.
     */
    public Entity(Entity other)
    {
        this.deleted = other.deleted;
    }

    /**
     * Getter method for the deleted state of the entity.
     *
     * @return boolean value representing whether the entity has been deleted.
     */
    public boolean isDeleted()
    {
        return deleted;
    }

    /**
     * Setter method for the deleted state of the entity.
     *
     * @param deleted boolean value to set the deleted state of the entity.
     */
    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }
}