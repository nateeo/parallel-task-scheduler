package graphTests;

import graph.Graph;
import graph.Node;
import org.junit.Test;

import java.io.File;

import static dotParser.Parser.parseDotFile;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

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
       assertEquals("example", graph.getName());
       assertTrue(graph.getStart().contains(new Node("a", 2)));
       assertEquals(1, graph.getStart().size());
    }


}
