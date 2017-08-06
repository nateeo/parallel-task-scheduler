<<<<<<< Updated upstream
package dotParser;

import graph.Edge;
import graph.Graph;
import graph.Node;

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
     * @return Graph, null if there was an error reading the file
     * @throws IOException
     */
    public static Graph parseDotFile (File file) {
        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        HashMap startNodes;
        Graph graph;
        int totalMinimumWork = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Parse graph lines
        String line;
        String[] splitLine;
        ArrayList<String[]> arcQueue = new ArrayList<String[]>();
        try {
            String graphName = br.readLine().split("\"")[1];
            graph = new Graph(graphName);

            while (!(line = br.readLine()).equals("}")) {
                splitLine = line.split("\\[");
                String left = splitLine[0].trim();
                String right = splitLine[1];
                int weight;

                if (splitLine[0].trim().length() == 1) { // add single vertex to graph and hashmap, as well as weight to min work
                    weight = getValue(right);
                    Node newVertex = new Node(left, weight);
                    totalMinimumWork += weight;
                    nodeMap.put(left, newVertex);
                } else { // add arc to queue for processing at the end
                    arcQueue.add(new String[]{left, right});
                }
            }
        } catch (IOException e) {
            return null;
        }

        // clone nodeMap to find possible starting nodes
        startNodes = new HashMap<String, Node>(nodeMap);
        // process arcQueue
        for (String[] string : arcQueue) {
            String[] arcString = string[0].split("->");
            String from = arcString[0].trim();
            String to =  arcString[1].trim();
            Node fromNode = nodeMap.get(from);
            Node toNode = nodeMap.get(to);
            int weight = getValue(string[1]);
            graph.addEdge(new Edge(fromNode, toNode, weight));
            fromNode.addOutgoingEdge(toNode, weight);
            toNode.addIncomingEdge(fromNode, weight);
            startNodes.remove(nodeMap.get(to).getName());
        }

        // set some useful fields in graph Object
        graph.setStart(new ArrayList<Node>(startNodes.values()));
        graph.setNodes(new ArrayList<Node>(nodeMap.values()));
        graph.setTotalMinimumWork(totalMinimumWork);

        return graph;
    }

    /**
     * Outputs a graph to a .dot file
     * @param name name of output file
     */
    public static void outputGraphToFile(String name) {

    }

    private static int getValue(String value) {
        return Integer.parseInt(value.substring(value.indexOf("=") + 1, value.lastIndexOf("]")).trim());
    }
}
=======
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

    /**
     * Outputs a graph to a .dot file
     * @param name name of output file
     */
    public static void outputGraphToFile(String name) {

    }

    private static double getValue(String value) {
        return Double.parseDouble(value.substring(value.indexOf("=") + 1, value.lastIndexOf("]")).trim());
    }
}
>>>>>>> Stashed changes
