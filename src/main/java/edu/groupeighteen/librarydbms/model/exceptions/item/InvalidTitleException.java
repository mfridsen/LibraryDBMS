package edu.groupeighteen.librarydbms.model.exceptions.item;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.exceptions
 * @contact matfir-1@student.ltu.se
 * @date 5/21/2023
 * Custom InvalidTitleException class. Used to make Exceptions clearer.
 */
public class InvalidTitleException extends Exception
{
    public InvalidTitleException(String message)
    {
        super(message);
    }
}