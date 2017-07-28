package graph;

/**
 * Vertex representation of a task that takes a certain time to finish (weight)
 */
public class WeightedVertex {
    private String _name;
    private int _weight;

    public WeightedVertex(String name) {
        _name = name;
        _weight = -1;
    }

    public WeightedVertex(String name, int weight) {
        _name = name;
        _weight = weight;
    }

    public String getName() {
        return _name;
    }

    public int getWeight() {
        return _weight;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof WeightedVertex) {
            String otherName = ((WeightedVertex)other).getName();
            return _name.equals(otherName);
        } else {
            return false;
        }
    }
}
