package model.entities.item;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Item class.
 */
@Suite
@SelectClasses({
        ItemCreationTest.class,
        ItemRetrievalTest.class,
        ItemCopyTest.class,
        ItemSettersTest.class
})
public class ItemTest
{
    //TODO-prio remember deleted
}