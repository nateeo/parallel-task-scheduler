package graphTests;

import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static dotParser.Parser.parseDotFile;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing input and output file processing
 * Author: Nathan Hur, Sueyeon Lee
 */
public class InputTest {
    private File exampleDotFile = new File("src/test/resources/exampleSmall.dot");
    private File exampleDot2File = new File("src/test/resources/exampleLarge.dot");
    private File nodes10RandomFile = new File("src/test/resources/Nodes_10_Random.dot");
    private Node a = new Node(0, "a", 2);
    private Node b = new Node(1, "b", 3);
    private Node c = new Node(2, "c", 3);
    private Node d = new Node(3, "d", 2);


    @Test
    /**
     * Test if the parser correctly processes the example graph's vertices, arcs and name
     */
    public void parseInput() {
       Graph graph = parseDotFile(exampleDotFile);
       assertEquals("exampleSmall", graph.getName());
       assertTrue(graph.getStart().contains(new Node(0, "a", 2)));
       assertEquals(1, graph.getStart().size());
    }

    @Test
    /**
     * Test if large inputs can be processed correctly
     */
    public void largeInput() {
        Graph graph = parseDotFile(exampleDot2File);
        assertEquals("exampleLarge", graph.getName());
        assertEquals("Incorrect number of nodes", graph.getNodes().size(), 11);
        assertEquals("Incorrect number of edges", graph.getEdges().size(), 14);
    }

    @Test
    /**
     * Test if parser ignores lines in incorrect format.
     */
    public void graph10() {
        Graph graph = parseDotFile(nodes10RandomFile);
        assertEquals("Random_Nodes_10_Density_1.90_CCR_10.00_WeightType_Random", graph.getName());
        assertEquals("Incorrect number of nodes",graph.getNodes().size(), 10);
        assertEquals("Incorrect number of edges",graph.getEdges().size(), 19);
    }

    @Test
    /**
     * Test if the correct nodes have been parsed
     */
    public void correctNodes(){
        Graph graph = parseDotFile(exampleDotFile);
        ArrayList<Node> nodes = new ArrayList<Node>(graph.getNodes());
        assertTrue("Incorrect node", nodes.contains(a));
        assertTrue("Incorrect node", nodes.contains(b));
        assertTrue("Incorrect node", nodes.contains(c));
        assertTrue("Incorrect node", nodes.contains(d));
    }

    @Test
    /**
     * Test if the correct edges have been parsed
     */
    public  void correctEdges(){
        Graph graph = parseDotFile(exampleDotFile);
        ArrayList<Edge> edges = new ArrayList<Edge>(graph.getEdges());
        assertTrue("Incorrect edge", edges.contains(new Edge(a, b, 4)));
        assertTrue("Incorrect edge", edges.contains(new Edge(a, c, 2)));
        assertTrue("Incorrect edge", edges.contains(new Edge(b, d, 2)));
        assertTrue("Incorrect edge", edges.contains(new Edge(c, d, 1)));
    }
}
