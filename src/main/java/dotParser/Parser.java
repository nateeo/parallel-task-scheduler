package dotParser;

import graph.Arc;
import graph.WeightedDigraph;
import graph.WeightedVertex;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * THis class contains methods to:
 * parse .dot files to a JGraph data structure
 * generate .dot files from a JGraph data structure
 */
public class Parser {

    /**
     * Parses .dot file and returns a JGraph representation.
     * @param file
     * @return WeightedDigraph, null if there was an error reading the file
     * @throws IOException
     */
    public static WeightedDigraph parseDotFile (File file) {
        HashMap<String, WeightedVertex> vertexHashMap = new HashMap<String, WeightedVertex>();
        WeightedDigraph graph;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Parse graph lines
        String line;
        String[] splitLine;
        ArrayList<Arc> arcQueue = new ArrayList<Arc>();
        try {
            String graphName = br.readLine().split("\"")[1];
            graph = new WeightedDigraph(graphName);

            while (!(line = br.readLine()).equals("}")) {
                splitLine = line.split("\\[");
                String left = splitLine[0].trim();
                String right = splitLine[1];
                double weight;

                if (splitLine[0].trim().length() == 1) { // add single vertex to graph and hashmap
                    weight = getValue(right);
                    WeightedVertex newVertex = new WeightedVertex(left, weight);
                    graph.addVertex(newVertex);
                    vertexHashMap.put(left, newVertex);
                } else { // add arc to queue for processing at the end
                    String[] arcString = left.split("->");
                    weight = getValue(right );
                    arcQueue.add(new Arc (
                            vertexHashMap.get(arcString[0].trim()),
                            vertexHashMap.get(arcString[1].trim()),
                            weight
                    ));
                }
            }
        } catch (IOException e) {
            return null;
        }
        // process arcQueue
        for (Arc arc : arcQueue) {
            DefaultWeightedEdge e = graph.addEdge(arc.getSource(), arc.getDestination());
            graph.setEdgeWeight(e, arc.getWeight());
        }

        return graph;
    }

    private static double getValue(String value) {
        return Double.parseDouble(value.substring(value.indexOf("=") + 1, value.lastIndexOf("]")).trim());
    }
}
