package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a graph and contains some metadata
 */
public class Graph {
    String _name;
    ArrayList<Node> _start;
    ArrayList<Node> _nodes;
    ArrayList<Edge> _edges;
    int _totalMinimumWork;

    public Graph(String name) {
        _name = name;
        _edges = new ArrayList<Edge>();
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
    }

    public ArrayList<Node> getStart() {
        return _start;
    }

    public List<Node> getNodes() { return _nodes; }

    public ArrayList<Edge> getEdges() {
        return _edges;
    }

    public Edge getEdge(Edge e) {
        return _edges.get(_edges.indexOf(e));
    }

    public String getName() {
        return _name;
    }

    public int totalMinimumWork() {
        return _totalMinimumWork;
    }

    // TODO string representation for duplicate state removal
    @Override
    public String toString() {
        String nodes = "";
        return nodes;
    }

}
