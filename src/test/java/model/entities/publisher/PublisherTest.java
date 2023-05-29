package model.entities.publisher;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/28/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the Publisher class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */

@Suite
@SelectClasses({
        PublisherCreationTest.class,
        PublisherRetrievalTest.class,
        PublisherCopyTest.class,
        PublisherSettersTest.class
})
public class PublisherTest {

}