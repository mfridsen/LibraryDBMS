package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.*;
import edu.groupeighteen.librarydbms.model.exceptions.*;
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
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
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
    public static void checkEmptyTitle(String title)
    throws InvalidTitleException
    {
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Empty title.");
    }

    /**
     * Checks whether a given itemID is invalid (<= 0). If so, throws an InvalidIDException,
     * which must be handled.
     *
     * @param itemID the ID to check.
     * @throws InvalidIDException if itemID <= 0.
     */
    public static void checkValidItemID(int itemID)
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
    public static void checkNullItem(Item item)
    throws NullEntityException
    {
        if (item == null)
            throw new NullEntityException("Invalid item: item is null.");
    }

    /**
     * Checks if the given ID is invalid.
     *
     * @param ID The ID to be checked.
     * @return True if the ID is invalid, false otherwise.
     */
    public static boolean invalidID(int ID)
    {
        return (ID <= 0);
    }

    /**
     * Checks if a barcode is already registered.
     *
     * @param barcode The barcode to be checked.
     * @return True if the barcode is already registered, false otherwise.
     */
    public static boolean barcodeTaken(String barcode)
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
    public static Author getExistingAuthor(int authorID)
    throws EntityNotFoundException
    {
        Author author = AuthorHandler.getAuthorByID(authorID);
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
    public static Classification getExistingClassification(int classificationID)
    throws EntityNotFoundException
    {
        Classification classification = ClassificationHandler.getClassificationByID(classificationID);
        if (classification == null)
            throw new EntityNotFoundException("Classification with ID " + classificationID + " not found.");
        return classification;
    }

    public static String retrieveOldTitle(Item item)
    throws InvalidIDException, EntityNotFoundException, RetrievalException
    {
        // Get the old item
        Item oldItem = getItemByID(item.getItemID());
        // Check if the item exists in the database
        if (oldItem == null)
            throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
        return oldItem.getTitle();
    }

    public static String retrieveOldBarcode(Item item)
    throws InvalidIDException, RetrievalException, EntityNotFoundException
    {
        // Get the old item
        Item oldItem = getItemByID(item.getItemID());
        // Check if the item exists in the database
        if (oldItem == null)
            throw new EntityNotFoundException("Delete failed: could not find Item with ID " + item.getItemID());
        return oldItem.getBarcode();
    }

    public static Literature constructRetrievedLiterature(ResultSet itemResultSet, ResultSet literatureResultSet) {
        try {
            if (itemResultSet.next() && literatureResultSet.next()) {
                return new Literature(
                        itemResultSet.getBoolean("deleted"),
                        itemResultSet.getInt("itemID"),
                        itemResultSet.getString("title"),
                        Item.ItemType.valueOf(itemResultSet.getString("itemType")),
                        itemResultSet.getString("barcode"),
                        itemResultSet.getInt("authorID"),
                        itemResultSet.getInt("classificationID"),
                        AuthorHandler.getAuthorByID(itemResultSet.getInt("authorID")).
                                getAuthorFirstname() + " " + AuthorHandler.getAuthorByID(itemResultSet.
                                getInt("authorID")).getAuthorLastName(),
                        ClassificationHandler.getClassificationByID(itemResultSet.
                                getInt("classificationID")).getClassificationName(),
                        itemResultSet.getInt("allowedRentalDays"),
                        itemResultSet.getBoolean("available"),
                        literatureResultSet.getString("ISBN")
                );
            }
        } catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Literature by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        return null;
    }

    public static Film constructRetrievedFilm(ResultSet itemResultSet, ResultSet filmResultSet) {
        try {
            if (itemResultSet.next() && filmResultSet.next()) {
                return new Film(
                        itemResultSet.getBoolean("deleted"),
                        itemResultSet.getInt("itemID"),
                        itemResultSet.getString("title"),
                        Item.ItemType.valueOf(itemResultSet.getString("itemType")),
                        itemResultSet.getString("barcode"),
                        itemResultSet.getInt("authorID"),
                        itemResultSet.getInt("classificationID"),
                        AuthorHandler.
                                getAuthorByID(itemResultSet.getInt("authorID")).getAuthorFirstname() + " " +
                                AuthorHandler.
                                        getAuthorByID(itemResultSet.getInt("authorID")).getAuthorLastName(),
                        ClassificationHandler.
                                getClassificationByID(itemResultSet.getInt("classificationID")).
                                getClassificationName(),
                        itemResultSet.getInt("allowedRentalDays"),
                        itemResultSet.getBoolean("available"),
                        filmResultSet.getInt("ageRating"),
                        filmResultSet.getString("countryOfProduction"),
                        filmResultSet.getString("actors")
                );
            }
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Film by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        return null;
    }



    public static Literature constructRetrievedLiterature(ResultSet resultSet)
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
                    AuthorHandler.getAuthorByID(resultSet.getInt("authorID")).
                            getAuthorFirstname() + " " + AuthorHandler.getAuthorByID(resultSet.
                            getInt("authorID")).getAuthorLastName(),
                    ClassificationHandler.getClassificationByID(resultSet.
                            getInt("classificationID")).getClassificationName(),
                    resultSet.getInt("allowedRentalDays"),
                    resultSet.getBoolean("available"),
                    resultSet.getString("ISBN")
            );
        }
        catch (ConstructionException | SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Literature by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return literature;
    }


    public static Film constructRetrievedFilm(ResultSet resultSet)
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
                    AuthorHandler.
                            getAuthorByID(resultSet.getInt("authorID")).getAuthorFirstname() + " " +
                            AuthorHandler.
                                    getAuthorByID(resultSet.getInt("authorID")).getAuthorLastName(),
                    ClassificationHandler.
                            getClassificationByID(resultSet.getInt("classificationID")).
                            getClassificationName(),
                    resultSet.getInt("allowedRentalDays"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("ageRating"),
                    resultSet.getString("countryOfProduction"),
                    resultSet.getString("actors")
            );
        }
        catch (ConstructionException | SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve Film by ID due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return film;
    }

    /**
     * Prints the stored titles and their counts.
     */
    public static void printTitles()
    {
        System.out.println("\nTitles:");
        ItemHandler.getStoredTitles().forEach((title, count) -> System.out.println("Title: " + title + " Copies: " + count));
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
        }
    }
}