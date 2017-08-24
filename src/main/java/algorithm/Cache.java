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

    public Cache(int processorCount) {
        Comparator<PartialSolution> comparator = (PartialSolution o1, PartialSolution o2) -> {
            double costDiff = o1._cost - o2._cost;
            if (costDiff == 0) {
                int nodeDiff = o1._nodes.size() - o2._nodes.size();
                if (nodeDiff == 0) {
                    // go through starting nodes
                    for (int i = 0; i < _processorCount; i++) {
                        int startNodeDiff = o1._startingNodes[i] - o2._startingNodes[i];
                        if (startNodeDiff != 0) return startNodeDiff;
                    }
                    // go through each processor, return if size different
                    for (int i = 0; i < _processorCount; i++) {
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
                    // same!
                    return 0;
                } else {
                    return nodeDiff;
                }
            } else {
                return costDiff > 0 ? 1 : -1;
            }
        };
        _treeSet = new TreeSet<>(comparator);
        _processorCount = processorCount;
    }

    public boolean add(PartialSolution ps) {
        return _treeSet.add(ps);
    }


}
