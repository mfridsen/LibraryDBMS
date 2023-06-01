package control.entities.classification;

import static org.junit.jupiter.api.Assertions.*;

import control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ClassificationHandler;
import edu.groupeighteen.librarydbms.model.entities.Classification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/31/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the GetClassificationByID class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetClassificationByIDTest extends BaseHandlerTest
{
    /**
     *
     */
    @Test
    @Order(1)
    void testGetClassificationByID()
    {
        System.out.println("\n1: Testing GetClassificationByID...");

        Classification classification = ClassificationHandler.getClassificationByID(1);
        assertNotNull(classification);
        assertEquals(1, classification.getClassificationID());
        assertEquals("Physics", classification.getClassificationName());
        assertEquals("Scientific literature on the topic of physics.", classification.getDescription());
        assertFalse(classification.isDeleted());

        System.out.println("\nTEST FINISHED.");
    }
}