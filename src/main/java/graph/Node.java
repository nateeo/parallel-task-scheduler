package graph;


import java.util.ArrayList;

/**
 * Class to represent the weighted node
 */
public class Node {
    static int _idCounter = 0;
    String _name;
    int _id;
    int _weight;
    ArrayList<Edge> _outgoing;
    ArrayList<Edge> _incoming;

    public Node(String name, int weight) {
        _id = _idCounter++;
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

    public ArrayList<Edge> getIncoming() {
        return _incoming;
    }

    public ArrayList<Node> getParentNodes() {
        ArrayList<Node> parents = new ArrayList<Node>();
        for (Edge e : _incoming) {
            parents.add(e._from);
        }
        return parents;
    }

    public int getId() {
        return _id;
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