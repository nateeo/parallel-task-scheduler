package algorithm;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * This class manages the storage and checking of duplicates
 * It stores partial solutions in a tree set
 * author: nhur714
 */
public class Cache {
    private TreeSet<PartialSolution> _treeSet;
    private int _processorCount;

    /*
     * Tree sorted by: cost -> node count -> first processor -> second processor ...
     */
    private Comparator<PartialSolution> comparator = (PartialSolution o1, PartialSolution o2) -> {
        int costDiff = o1._cost - o2._cost;
        if (costDiff == 0) {
            int nodeDiff = o1._nodes.size() - o2._nodes.size();
            if (nodeDiff == 0) {
                int firstProcessorDiff = o1._processors[0].size() - o2._processors[0].size();
                if (firstProcessorDiff == 0) {
                    return 0;
                } else {
                    return firstProcessorDiff;
                }
            } else {
                return nodeDiff;
            }
        } else {
            return costDiff;
        }
    };

    public Cache(int processorCount) {
        _treeSet = new TreeSet<>(comparator);
        _processorCount = processorCount;
    }

    public boolean add(PartialSolution ps) {
        return _treeSet.add(ps);
    }

    public boolean contains(PartialSolution ps) {
       return _treeSet.contains(ps);
    }


}
