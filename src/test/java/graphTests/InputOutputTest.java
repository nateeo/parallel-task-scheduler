package graphTests;

import graph.WeightedDigraph;
import graph.WeightedVertex;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Test;

import java.io.File;

import static dotParser.Parser.parseDotFile;
import static org.junit.Assert.assertEquals;

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
        WeightedDigraph graph = parseDotFile(exampleDotFile);
        assertEquals("example", graph.getName());
        assertEquals(exampleGraphRepresentation, graph.toString());

        String weights = "";
        for(WeightedVertex vertex : graph.vertexSet()) {
            weights += " " + vertex.getWeight();
        }
        assertEquals("2.0 3.0 3.0 2.0", weights.trim());

        String edgeWeights = "";
        for(DefaultWeightedEdge edge : graph.edgeSet()) {
            edgeWeights += " " + graph.getEdgeWeight(edge);
        }
        assertEquals("1.0 2.0 2.0 1.0", edgeWeights.trim());
    }


}
