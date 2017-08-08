package scheduleValidation;

import algorithm.PartialSolution;
import graph.Graph;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zihaoyang on 8/08/17.
 */
public class ValidationTest {

    PartialSolution _partialSolution;
    Graph _graph;



    @Before
    public void runAlgorithm() {

    }

    @Test
    public void testScheduleValidity() {
        ScheduleValidation.scheduleIsValid();
    }
}
