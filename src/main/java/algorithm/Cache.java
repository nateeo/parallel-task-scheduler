package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * This class manages the storage and checking of duplicates
 * It stores partial solutions in a tree set
 *
 * The tree set is sorted according to the order:
 * cost -> total nodes -> processors allocated -> set of (ordered) start nodes on processors
 * -> each normalised processor size -> each normalised processor nodes
 * author: nhur714
 */
public class Cache {
    private TreeSet<PartialSolution> _treeSet;
    private int _processorCount;

    private Comparator<PartialSolution> comparator = (PartialSolution o1, PartialSolution o2) -> {
        int costDiff = o1._cost - o2._cost;
        if (costDiff == 0) {
            int nodeDiff = o1._nodes.size() - o2._nodes.size();
            if (nodeDiff == 0) {
                // check all start nodes are the same
                for (int i = 0; i < _processorCount; i++) {
                    int startDiff = o1._startingNodes[i] - o2._startingNodes[i];
                    if (startDiff != 0) {
                        return startDiff;
                    }
                }
                // if we made it here, we need to start checking individual normalised processors size then nodes
                ArrayList[] firstProcessors = o1.getProcessors();
                ArrayList[] secondProcessors = o2.getProcessors();
                // check each size diff
                for (int i = 0; i < _processorCount; i++) {
                    int processorSizeDiff = firstProcessors[o1._startingNodeIndices[i]].size() - secondProcessors[o2._startingNodeIndices[i]].size();
                    if (processorSizeDiff != 0) {
                        return processorSizeDiff;
                    }
                }
                // check each list
                for (int i = 0; i < _processorCount; i++) {
                    ArrayList<ProcessorSlot> processorOne = firstProcessors[o1._startingNodeIndices[i]];
                    ArrayList<ProcessorSlot> processorTwo = secondProcessors[o2._startingNodeIndices[i]];
                    for (int j = 0; j < processorOne.size(); j++) {
                        int diff = processorOne.get(j).getNode().getId() - processorTwo.get(j).getNode().getId();
                        if (diff != 0) {
                            return diff;
                        }
                    }
                }
                return 0;
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
