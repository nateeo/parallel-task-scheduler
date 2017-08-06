package graph;


import java.util.ArrayList;

/**
 * Class to represent the weighted node
 */
public class Node {
    String _name;
    int _weight;
    ArrayList<Edge> _outgoing;
    ArrayList<Edge> _incoming;

    public Node(String name, int weight) {
        _name = name;
        _weight = weight;
        _outgoing = new ArrayList<Edge>();
        _incoming = new ArrayList<Edge>();
    }

    public void addOutgoingEdge(Node to, int weight) {
        _outgoing.add(new Edge(this, to, weight));
    }

    public void addIncomingEdge(Node from, int weight) {
        _incoming.add(new Edge(from, this, weight));
    }

    public ArrayList<Edge> getOutgoing() {
        return _outgoing;
    }

    public ArrayList<Edge> getIngoing() {
        return _incoming;
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

    @Override
    public String toString() {
        return _name;
    }
}