package graph;


import java.util.ArrayList;

/**
 * Class to represent the weighted node
 */
public class Node {
    String _name;
    int _weight;
    ArrayList<Edge> _outgoing;
    ArrayList<Edge> _ingoing;

    public Node(String name, int weight) {
        _name = name;
        _weight = weight;
        _outgoing = new ArrayList<Edge>();
    }

    public void addEdge(Node to, int weight) {
        _outgoing.add(new Edge(to, weight));
    }

    public ArrayList<Edge> getOutgoing() {
        return _outgoing;
    }

    public ArrayList<Edge> getIngoing() {
        return _ingoing;
    }

    public String getName() {
        return _name;
    }

    public int getWeight() {
        return _weight;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            return this._name.equals(((Node)other)._name);
        }
        return false;
    }
}