package graph;

import java.util.*;

/**
 * Class that represents a graph and contains some metadata
 */
public class Graph {
    String _name;
    ArrayList<Node> _start;
    ArrayList<Node> _nodes;
    ArrayList<Edge> _edges;
    HashMap<Integer, HashMap<Integer, Edge>> _edgeMap; // fromId => < toId, Edge >
    int _totalMinimumWork;
    public HashMap<String, Integer> _bottomLevelWork;

    public Graph(String name) {
        _name = name;
        _edges = new ArrayList<Edge>();
        _edgeMap = new HashMap<>();
    }

    public void setStart(ArrayList<Node> start) {
        _start = start;
    }

    public void setNodes(ArrayList<Node> nodes){
        _nodes = nodes;
    }

    public void setTotalMinimumWork(int work) {
        _totalMinimumWork = work;
    }

    public void addEdge(Edge e) {
        _edges.add(e);
        if (!_edgeMap.containsKey(e.getFrom().getId())) {
            _edgeMap.put(e.getFrom().getId(), new HashMap<>());
        }
        _edgeMap.get(e.getFrom().getId()).put(e.getTo().getId(), e);
    }

    public ArrayList<Node> getStart() {
        return _start;
    }

    public List<Node> getNodes() { return _nodes; }

    public ArrayList<Edge> getEdges() {
        return _edges;
    }

    public Edge getEdge(int fromId, int toId) {
        if (!_edgeMap.containsKey(fromId)) {
            System.out.println("no edge from " + fromId + "to " + toId + " in graph " + _name);
            for (int i = 0; i < _edges.size(); i++) {
                System.out.println(_edges.get(i)._from.getId() + " " +  _edges.get(i)._to.getId());
            }
        }
        return _edgeMap.get(fromId).get(toId);
    }

    public String getName() {
        return _name;
    }

    public int totalMinimumWork() {
        return _totalMinimumWork;
    }

    /**
     * Calculates the bottomLevel value for all nodes in the graph, this only has to be run once in
     * the initialization. The bottom level of a source node is the sum of all the weights of each individual
     * node in the longest path originating from the source node. This function works backwards starting from
     * the leaf nodes moving up towards the root nodes.
     * the graph that contains all the nodes and edges parsed from an input .dot file
     * @return HashMap<String, Integer> of bottomLevels. The String is the name of the node and the Integer
     * is the bottom level value that is calculated.
     */
    public void bottomLevelCalculator() {
        List<Node> allNodes = this.getNodes();
        HashMap<String, Integer> bottomLevels = new HashMap<String, Integer>(allNodes.size());
        Queue<Node> queuedNodes = new LinkedList<Node>();
        Node predecessorNode;
        Node currentNode;
        int maxBottomLevel;
        int currentNodeBL;
        boolean allSuccessorsCalculated;

        // Looks for all leaf nodes.
        for(Node node: allNodes) {
            if(node.getOutgoing().isEmpty()) {
                queuedNodes.add(node);
            }
        }

        // Goes through the Queue of nodes and adds their bottom level to the hashmap.
        while(!queuedNodes.isEmpty()) {
            currentNode = queuedNodes.remove();
            maxBottomLevel = 0;
            if (!currentNode.getOutgoing().isEmpty()) {
                // Grabs all successor nodes and calculates the bottom level based on the max value of all it's successors.
                for (Edge successors : currentNode.getOutgoing()) {
                    currentNodeBL = bottomLevels.get(successors.getTo().getName());
                    if (currentNodeBL > maxBottomLevel) {
                        maxBottomLevel = currentNodeBL;
                    }
                }
            }

            bottomLevels.put(currentNode.getName(),maxBottomLevel + currentNode.getWeight());

            // Grabs all predecessor nodes and checks if all their successor nodes have been calculated,
            // if the node names exist on the hashmap. If true, adds node to the queue
            if (!currentNode.getIncoming().isEmpty()) {
                for (Edge predecessors : currentNode.getIncoming()) {
                    predecessorNode = predecessors.getFrom();
                    allSuccessorsCalculated = true;
                    for (Edge pSuccessors : predecessorNode.getOutgoing()) {
                        if (!bottomLevels.containsKey(pSuccessors.getTo().getName())) {
                            allSuccessorsCalculated = false;
                        }
                    }
                    if (allSuccessorsCalculated) {
                        queuedNodes.add(predecessorNode);
                    }
                }
            }
        }
        _bottomLevelWork = bottomLevels;
    }

    // TODO string representation for duplicate state removal
    @Override
    public String toString() {
        String nodes = "";
        return nodes;
    }

}
