package graph;

/**
 * Created by nateeo on 28/07/17.
 */
public class Arc {
    WeightedVertex _source;
    WeightedVertex _destination;
    double _weight;
    public Arc(WeightedVertex source, WeightedVertex destination, double weight) {
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

    public double getWeight() {
        return _weight;
    }
}
