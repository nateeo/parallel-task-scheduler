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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static scheduler.Scheduler.parseConsole;

/**
 * Created by zihaoyang on 8/08/17.
 */
public class ValidationTest {

    PartialSolution _solution;
    Graph _graph;



    @Test
    public void runAlgorithm() {

        String[] args = new String[2];

        try {

            System.out.println("HELLO START TEST 1");

            String inputFileName = "example2.dot";
            if (!inputFileName.endsWith(".dot")) {
                throw new InvalidInputException("Input file must be dot");
            }
            args[0] = inputFileName;
            File inputFile = new File(inputFileName);


            args[1] = "2";

            _solution = parseConsole(args);
            _graph = Parser.parseDotFile(inputFile);

            assertTrue(ScheduleValidation.scheduleIsValid(_graph, _solution));

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
    public void runInvalid1and2Algorithm(){

        String[] args = new String[2];

        try {

            System.out.println("HELLLLOOOOOO START TEST 2");

            String inputFileName = "src/test/resources/testInvalid.dot";
            if (!inputFileName.endsWith(".dot")) {
                throw new InvalidInputException("Input file must be dot");
            }
            args[0] = inputFileName;
            File inputFile = new File(inputFileName);

            args[1] = "2";

            _graph = Parser.parseDotFile(inputFile);
            PartialSolution _invalidSolution = new PartialSolution(2);
            ArrayList<ProcessorSlot> ps1 = new ArrayList<>();
            ArrayList<ProcessorSlot> ps2 = new ArrayList<>();
            _invalidSolution._processors[0] = ps1;
            _invalidSolution._processors[1] = ps2;

            List<Node> nodes = new ArrayList<>(_graph.getNodes());
            for (int i = 0; i < nodes.size(); i++){
                if (nodes.get(i).equals(new Node("a", 2))){
                    ProcessorSlot pslot = new ProcessorSlot(nodes.get(i), 0, 1, 1);
                    _invalidSolution._processors[0].add(0,pslot);
                } else if (nodes.get(i).equals(new Node("b", 3))) {
                    ProcessorSlot pslot = new ProcessorSlot(nodes.get(i), 4, 7, 2);
                    _invalidSolution._processors[1].add(0,pslot);
                } else if (nodes.get(i).equals(new Node("c", 3))) {
                    ProcessorSlot pslot = new ProcessorSlot(nodes.get(i), 7, 10, 2);
                    _invalidSolution._processors[1].add(1, pslot);
                } else {
                    ProcessorSlot pslot = new ProcessorSlot(nodes.get(i), 10, 12, 1);
                    _invalidSolution._processors[0].add(1, pslot);
                }
            }

        assertTrue(ScheduleValidation.scheduleIsValid(_graph, _invalidSolution));


        } catch (InvalidInputException e) {
            System.out.println();
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong with your input");
            e.printStackTrace();
        }

    }
//
//    @Test
//    public void testScheduleValidity() {
//        assertTrue(ScheduleValidation.scheduleIsValid(_graph, _solution));
//    }

}
