package edu.groupeighteen.librarydbms.model.entities.classification;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/28/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the Classification class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */

@Suite
@SelectClasses({
        ClassificationCreationTest.class,
        ClassificationRetrievalTest.class,
        ClassificationCopyTest.class,
        ClassificationSetterTest.class
})
public class ClassificationTestSuite
{

}