package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.*;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidISBNException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static edu.groupeighteen.librarydbms.control.entities.ItemHandler.getItemByID;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 6/1/2023
 * <p>
 * Class containing static utility methods for the ItemHandler class.
 */
public class ItemHandlerUtils
{
    /**
     * Checks whether a given title is null or empty. If so, throws an InvalidTitleException,
     * which must be handled.
     *
     * @param title the title to check.
     * @throws InvalidTitleException if title is null or empty.
     */
    static void checkEmptyTitle(String title)
    throws InvalidTitleException
    {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Empty title.");
    }

    /**
     * Validates the given ISBN.
     *
     * @param ISBN the ISBN to validate
     * @throws InvalidISBNException if the ISBN is empty or exceeds the maximum length
     */
    static void validateISBN(String ISBN)
    throws InvalidISBNException
    {
        if (ISBN == null || ISBN.isEmpty())
            throw new InvalidISBNException("Empty ISBN.");
        if (ISBN.length() > Literature.LITERATURE_ISBN_LENGTH)
            throw new InvalidISBNException("Too long ISBN.");
    }

    /**
     * Validates the given classification name.
     *
     * @param classificationName the classification name to validate
     * @throws InvalidNameException if the classification name is empty or exceeds the maximum length
     */
    static void validateEmptyClassificationName(String classificationName)
    throws InvalidNameException
    {
        if (classificationName == null || classificationName.isEmpty())
            throw new InvalidNameException("Classification name is empty.");
        if (classificationName.length() > Classification.CLASSIFICATION_NAME_LENGTH)
            throw new InvalidNameException("Classification name is too long.");
    }

    /**
     * Checks whether a given itemID is invalid (<= 0). If so, throws an InvalidIDException,
     * which must be handled.
     *
     * @param itemID the ID to check.
     * @throws InvalidIDException if itemID <= 0.
     */
    static void checkValidItemID(int itemID)
    throws InvalidIDException
    {
        if (itemID <= 0)
            throw new InvalidIDException("Error retrieving item by itemID: invalid itemID " + itemID);
    }

    /**
     * Checks whether a given Item is null. If so, throws a NullEntityException which must be handled.
     *
     * @param item the item to check.
     * @throws NullEntityException if item is null.
     */
    static void checkNullItem(Item item)
    throws NullEntityException
    {
        if (item == null)
            throw new NullEntityException("Invalid item: item is null.");
    }

    static void validateItem(Item item)
    throws EntityNotFoundException, InvalidIDException, NullEntityException
    {
        try
        {
            checkNullItem(item);

            if (getItemByID(item.getItemID()) == null)
                throw new EntityNotFoundException("Item with ID " + item.getItemID() + " not found in table.");
        }
        catch (RetrievalException e)
        {
            ExceptionHandler.HandleFatalException("Failed to validate Item by due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the given ID is invalid.
     *
     * @param ID The ID to be checked.
     * @return True if the ID is invalid, false otherwise.
     */
    static boolean invalidID(int ID)
    {
        return (ID <= 0);
    }

    /**
     * Checks if a barcode is already registered.
     *
     * @param barcode The barcode to be checked.
     * @return True if the barcode is already registered, false otherwise.
     */
    static boolean barcodeTaken(String barcode)
    {
        return (ItemHandler.getRegisteredBarcodes().contains(barcode));
    }

    /**
     * Retrieves an existing Author object.
     *
     * @param authorID The ID of the author to be retrieved.
     * @return The retrieved Author object.
     * @throws EntityNotFoundException If no author with the given ID is found.
     */
    static Author getExistingAuthor(int authorID)
    throws EntityNotFoundException
    {
        Author author = AuthorHandler.getAuthorByID(authorID, false);
        if (author == null)
            throw new EntityNotFoundException("Author with ID " + authorID + "not found.");
        return author;
    }

    /**
     * Retrieves an existing Classification object.
     *
     * @param classificationID The ID of the classification to be retrieved.
     * @return The retrieved Classification object.
     * @throws EntityNotFoundException If no classification with the given ID is found.
     */
    static Classification getExistingClassification(int classificationID)
    throws EntityNotFoundException, InvalidIDException
    {
        Classification classification = ClassificationHandler.getClassificationByID(classificationID);
        if (classification == null)
            throw new EntityNotFoundException("Classification with ID " + classificationID + " not found.");
        return classification;
    }

    /**
     * Retrieves the old title of the given item from the database.
     *
     * @param item the item to retrieve the old title for
     * @return the old title of the item
     * @throws InvalidIDException      if the item ID is invalid
     * @throws EntityNotFoundException if the item is not found in the database
     * @throws RetrievalException      if an error occurs during retrieval
     */
    static String retrieveOldTitle(Item item)
    throws InvalidIDException, EntityNotFoundException, RetrievalException
    {
        // Get the old item
        Item oldItem = getItemByID(item.getItemID());
        // Check if the item exists in the database
        if (oldItem == null)
            throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
        return oldItem.getTitle();
    }

    /**
     * Retrieves the old barcode of the given item from the database.
     *
     * @param item the item to retrieve the old barcode for
     * @return the old barcode of the item
     * @throws InvalidIDException      if the item ID is invalid
     * @throws RetrievalException      if an error occurs during retrieval
     * @throws EntityNotFoundException if the item is not found in the database
     */
    static String retrieveOldBarcode(Item item)
    throws InvalidIDException, RetrievalException, EntityNotFoundException
    {
        // Get the old item
        Item oldItem = getItemByID(item.getItemID());
        // Check if the item exists in the database
        if (oldItem == null)
            throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
        return oldItem.getBarcode();
    }

    /**
     * Constructs a Literature object using the data from the given ResultSet.
     *
     * @param resultSet the ResultSet containing the literature data
     * @return the constructed Literature object
     */
    static Literature constructRetrievedLiterature(ResultSet resultSet)
    {
        Literature literature = null;

        try
        {
            literature = new Literature(
                    resultSet.getBoolean("deleted"),
                    resultSet.getInt("literatureID"),
                    resultSet.getString("title"),
                    Item.ItemType.valueOf(resultSet.getString("itemType")),
                    resultSet.getString("barcode"),
                    resultSet.getInt("authorID"),
                    resultSet.getInt("classificationID"),
                    AuthorHandler.getAuthorByID(resultSet.getInt("authorID"), false).getAuthorFirstname(),
                    AuthorHandler.getAuthorByID(resultSet.getInt("authorID"), false).getAuthorLastName(),
                    ClassificationHandler.getClassificationByID(
                            resultSet.getInt("classificationID")).getClassificationName(),
                    resultSet.getInt("allowedRentalDays"),
                    resultSet.getBoolean("available"),
                    resultSet.getString("ISBN")
            );
        }
        catch (ConstructionException | SQLException | InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Literature by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return literature;
    }

    /**
     * Constructs a Film object using the data from the given ResultSet.
     *
     * @param resultSet the ResultSet containing the film data
     * @return the constructed Film object
     */
    static Film constructRetrievedFilm(ResultSet resultSet)
    {
        Film film = null;

        try
        {
            film = new Film(
                    resultSet.getBoolean("deleted"),
                    resultSet.getInt("filmID"),
                    resultSet.getString("title"),
                    Item.ItemType.valueOf(resultSet.getString("itemType")),
                    resultSet.getString("barcode"),
                    resultSet.getInt("authorID"),
                    resultSet.getInt("classificationID"),
                    AuthorHandler.getAuthorByID(resultSet.getInt("authorID"), false).getAuthorFirstname(),
                    AuthorHandler.getAuthorByID(resultSet.getInt("authorID"), false).getAuthorLastName(),
                    ClassificationHandler.getClassificationByID(
                            resultSet.getInt("classificationID")).getClassificationName(),
                    resultSet.getInt("allowedRentalDays"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("ageRating"),
                    resultSet.getString("countryOfProduction"),
                    resultSet.getString("actors")
            );
        }
        catch (ConstructionException | SQLException | InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Film by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return film;
    }

    /**
     * Prints the stored titles and their counts.
     */
    static void printTitles()
    {
        System.out.println("\nTitles:");
        ItemHandler.getStoredTitles().forEach(
                (title, count) -> System.out.println("Title: " + title + " Copies: " + count));
    }

    /**
     * Prints the list of Items with their IDs and titles.
     *
     * @param itemList the list of Items to print
     */
    public static void printItemList(List<Item> itemList)
    {
        System.out.println("Items:");
        int count = 1;
        for (Item item : itemList)
        {
            System.out.println(count + " itemID: " + item.getItemID() + ", title: " + item.getTitle());
            count++;
        }
    }
}