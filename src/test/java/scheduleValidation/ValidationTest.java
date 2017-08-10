package scheduleValidation;

import algorithm.PartialSolution;
import dotParser.Parser;
import graph.Graph;
import org.junit.Before;
import org.junit.Test;
import scheduler.InvalidInputException;
import scheduler.Scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please provide the .dot file");
            args[0]  = reader.readLine();

            String inputFileName = args[0];
            if (!inputFileName.endsWith(".dot")) {
                throw new InvalidInputException("Input file must be dot");
            }
            File inputFile = new File(inputFileName);



            System.out.println("how many processors?");
            args[1] = reader.readLine();

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

        ScheduleValidation.scheduleIsValid(_graph, _solution);
    }
}
