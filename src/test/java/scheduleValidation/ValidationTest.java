package scheduleValidation;

import algorithm.PartialSolution;
import dotParser.Parser;
import graph.Graph;
import org.junit.Before;
import org.junit.Test;
import scheduler.InvalidInputException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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

            String inputFileName = "example2.dot";
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
    public void testScheduleValidity() {
        assertTrue(ScheduleValidation.scheduleIsValid(_graph, _solution));
    }
}
