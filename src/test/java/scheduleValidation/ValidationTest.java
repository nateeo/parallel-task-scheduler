package scheduleValidation;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import dotParser.Parser;
import graph.Graph;
import graph.Node;
import org.junit.Before;
import org.junit.Test;
import scheduler.InvalidInputException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static scheduler.Scheduler.parseConsole;

/**
 * Created by zihaoyang on 8/08/17.
 */
public class ValidationTest {

    PartialSolution _solution;
    Graph _graph;



    @Before
    public void runAlgorithm() {

        String[] args = new String[2];

        try {

            String inputFileName = "src/test/resources/example.dot";
            if (!inputFileName.endsWith(".dot")) {
                throw new InvalidInputException("Input file must be dot");
            }
            args[0] = inputFileName;
            File inputFile = new File(inputFileName);


            args[1] = "2";

            _solution = parseConsole(args);
            _graph = Parser.parseDotFile(inputFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidInputException e) {
            System.out.println();
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong with your input");
            e.printStackTrace();
        }
    }

    @Test
    public void makeInvalid0and3() {
        String[] args = new String[2];

        try {

            String inputFileName = "src/test/resources/testInvalid.dot";
            if (!inputFileName.endsWith(".dot")) {
                throw new InvalidInputException("Input file must be dot");
            }
            args[0] = inputFileName;
            File inputFile = new File(inputFileName);

            args[1] = "2";

            _graph = Parser.parseDotFile(inputFile);

        } catch (InvalidInputException e) {
            System.out.println();
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong with your input");
            e.printStackTrace();
        }

        List<Node> nodes = _graph.getNodes();
        Node nodeA = null;
        Node nodeB = null;
        Node nodeC = null;
        Node nodeD = null;
        for (Node node: nodes) {
            if (node.getName().equals("a")) {
                nodeA = node;
            } else if (node.getName().equals("b")) {
                nodeB = node;
            } else if (node.getName().equals("c")) {
                nodeC = node;
            } else if (node.getName().equals("d")) {
                nodeD = node;
            }
        }
        PartialSolution invalidSolution = new PartialSolution(2);

        ProcessorSlot slotA = new ProcessorSlot(nodeA, 8, 10, 2);
        ProcessorSlot slotB = new ProcessorSlot(nodeB, 0, 3, 1);
        ProcessorSlot slotC = new ProcessorSlot(nodeC,10, 13, 2);
        ProcessorSlot slotD = new ProcessorSlot(nodeD,12, 14, 2);

        ArrayList<ProcessorSlot> processor1 =  new ArrayList<ProcessorSlot>();
        processor1.add(slotB);

        ArrayList<ProcessorSlot> processor2 =  new ArrayList<ProcessorSlot>();
        processor2.add(slotA);
        processor2.add(slotC);
        processor2.add(slotD);

        invalidSolution._processors[0] = processor1;
        invalidSolution._processors[1] = processor2;


        assertFalse(ScheduleValidation.scheduleIsValid(_graph, invalidSolution));
    }

    @Test
    public void testScheduleValidity() {

        assertTrue(ScheduleValidation.scheduleIsValid(_graph, _solution));
    }
}
