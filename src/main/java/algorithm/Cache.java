package algorithm;

import logger.Logger;

import java.util.*;

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
    private SortedSet<PartialSolution>  _treeSet;
    private int _processorCount;
    private int[] _firstNormalisedProcessors;
    private int[] _secondNormalisedProcessors;

    private Comparator<PartialSolution> comparator = (PartialSolution o1, PartialSolution o2) -> {
        int costDiff = o1._cost - o2._cost;

        // if the PartialSolutions have the same cost
        if (costDiff == 0) {
            int nodeDiff = o1._nodes.size() - o2._nodes.size();
            // same cost and same number of nodes
            if (nodeDiff == 0) {
                // go through starting nodes
                for (int i = 0; i < _processorCount; i++) {
                    int startNodeDiff = o1._startingNodes[i] - o2._startingNodes[i];
                    if (startNodeDiff != 0) return startNodeDiff;
                }
                // go through each processor, return if size different
                for (int i = 0; i < _processorCount; i++) {
                    if (o1._startingNodes[i] != 0) {
                    ArrayList<ProcessorSlot> o1Processor = o1.getProcessors()[o1._startingNodeIndices[i]];
                    ArrayList<ProcessorSlot> o2Processor = o2.getProcessors()[o2._startingNodeIndices[i]];
                    int size1 = o1Processor.size();
                    int size2 = o2Processor.size();
                    if (size1 != size2) return size1 - size2;
                    for (int j = 1; j < size1; j++) {
                        int slotNodeDiff = o1Processor.get(j).getNode().getId() - o2Processor.get(j).getNode().getId();
                        if (slotNodeDiff != 0) return slotNodeDiff;
                    }
                }
                }
                // same!
                return 0;
            } else {
                return nodeDiff;
            }
        } else {
            return costDiff;
        }
    };

    public Cache(int processorCount) {
        TreeSet<PartialSolution> ts = new TreeSet<>(comparator);
        _treeSet = Collections.synchronizedSortedSet(ts);
        _processorCount = processorCount;
    }

    public boolean add(PartialSolution ps) {
        return _treeSet.add(ps);
    }

    public boolean contains(PartialSolution ps) {
       return _treeSet.contains(ps);
    }


}
