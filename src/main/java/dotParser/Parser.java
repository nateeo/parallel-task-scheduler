package dotParser;

import graph.Arc;
import graph.WeightedDigraph;
import graph.WeightedVertex;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to parse .dot files to a JGraph data structure and produce .dot output
 */
public class Parser {

    public static WeightedDigraph parseDotFile (File file) throws IOException {
        WeightedDigraph graph = new WeightedDigraph();
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Parse graph lines
        String line;
        String[] splitLine;
        ArrayList<Arc> arcQueue = new ArrayList<Arc>();
        String graphName = br.readLine().split(" ")[1]; // TODO: handle graph name

        while ((line = br.readLine()) != "}") {
            splitLine = line.split("\\[");
            String left = splitLine[0].trim();
            String right = splitLine[1];
            int weight;

            if (splitLine[0].length() == 1) { // add single vertex
                weight = right.charAt(right.indexOf("=") + 1);
                graph.addVertex(new WeightedVertex(left, weight));
            } else { // add arc to queue for processing at the end
                String[] arcString = left.split("->");
                weight = right.charAt(right.indexOf("=") + 1);
                arcQueue.add(new Arc(
                        new WeightedVertex(arcString[0].trim()),
                        new WeightedVertex(arcString[1].trim()),
                        weight
                ));
            }
        }
        // process arcQueue
        for (Arc arc : arcQueue) {
            DefaultWeightedEdge e = graph.addEdge(arc.getSource(), arc.getDestination());
            graph.setEdgeWeight(e, arc.getWeight());
        }

        return graph;
    }
}
