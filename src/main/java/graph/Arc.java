package graph;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Created by nateeo on 28/07/17.
 */
public class Arc {
    WeightedVertex _source;
    WeightedVertex _destination;
    int _weight;
    public Arc(WeightedVertex source, WeightedVertex destination, int weight) {
        _source = source;
        _destination = destination;
        _weight = weight;
    }

    public WeightedVertex getSource() {
        return _source;
    }

    public WeightedVertex getDestination() {
        return _destination;
    }

    public int getWeight() {
        return _weight;
    }
}
