package graphTests;

import algorithm.PSManager;
import graph.Graph;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static dotParser.Parser.parseDotFile;

/**
 * Created by nateeo on 1/08/17.
 */
public class InputOutputTest {
    private File exampleDotFile = new File("src/test/resources/example.dot");
    private String exampleGraphRepresentation = "([a, b, c, d], [(a,b), (a,c), (b,d), (c,d)])";

    @Test
    /**
     * Test if the parser correctly processes the example graph's vertices, arcs and name
     */
    public void parseInput() {
       Graph graph = parseDotFile(exampleDotFile);
        HashMap<String, Integer> stuff = PSManager.bottomLevelCalculator(graph);

        for (Map.Entry<String, Integer> e : stuff.entrySet()) {
            System.out.println(e.getKey() + " blw is " + e.getValue());
        }
    }


}
