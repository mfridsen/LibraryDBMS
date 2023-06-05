package edu.groupeighteen.librarydbms.control.entities.item;

import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandlerUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/1/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetAllItems class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAllItemsTest extends BaseHandlerTest
{
    /**
     *
     */
    @Test
    @Order(1)
    void testGetAllItems()
    {
        System.out.println("\n1: Testing GetAllItems...");

        ItemHandlerUtils.printItemList(ItemHandler.getAllItems());

        System.out.println("\nTEST FINISHED.");
    }
}