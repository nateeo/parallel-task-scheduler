package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // TODO string representation for duplicate state removal
    @Override
    public String toString() {
        String nodes = "";
        return nodes;
    }

}
