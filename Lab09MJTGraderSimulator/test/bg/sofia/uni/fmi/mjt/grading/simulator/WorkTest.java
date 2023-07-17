package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkTest {

    @Test
    void testWith10Students() {
        Work work = new Work(new CodePostGrader(20),10 , 2000);
        work.commit();
        assertEquals(10,work.getGrader().getSubmittedAssignmentsCount());
    }

    @Test
    void testWith100Students() {
        Work work = new Work(new CodePostGrader(20),100 , 2000);
        work.commit();
        assertEquals(100,work.getGrader().getSubmittedAssignmentsCount());
    }

    @Test
    void testGradedAssignmentsCount() {
        Work work = new Work(new CodePostGrader(20),10 , 2000);
        work.commit();
        int totalGraded = work.getGrader().getAssistants().
                stream().
                mapToInt(Assistant::getNumberOfGradedAssignments).
                sum();
        assertEquals(10,totalGraded);

    }


}
