package algorithm;

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

    private TreeSet<PartialSolution> _treeSet;
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
                // check keysets are same size
                Set<Integer> firstSet = o1._id.keySet();
                Set<Integer> secondSet = o2._id.keySet();
                int setDiff = firstSet.size() - secondSet.size();
                // same cost, same number of nodes, same keyset sides
                if (setDiff == 0) {
                    // go through all normalised processors
                    Iterator<Integer> first = firstSet.iterator();
                    Iterator<Integer> second = secondSet.iterator();
                    while (first.hasNext() && second.hasNext()) {
                        int index = 0;
                        int firstProcessorStartNode = first.next();
                        int secondProcessorStartNode = second.next();
                        int startNodeDiff = firstProcessorStartNode - secondProcessorStartNode;
                        if (startNodeDiff != 0) {
                            return startNodeDiff;
                        }
                        _firstNormalisedProcessors[index] = o1._id.get(firstProcessorStartNode);
                        _secondNormalisedProcessors[index] = o2._id.get(secondProcessorStartNode);
                        index++;
                    }
                    // if we made it here, we need to start checking individual normalised processors size then nodes
                    ArrayList[] firstProcessors = o1.getProcessors();
                    ArrayList[] secondProcessors = o2.getProcessors();
                    // check each size
                    for (int i = 0; i < _processorCount; i++) {
                        int processorSizeDiff = firstProcessors[_firstNormalisedProcessors[i]].size() - secondProcessors[_secondNormalisedProcessors[i]].size();
                        if (processorSizeDiff != 0) {
                            return processorSizeDiff;
                        }
                    }
                    // check each list
                    for (int i = 0; i < _processorCount; i++) {
                        ArrayList<ProcessorSlot> processorOne = firstProcessors[_firstNormalisedProcessors[i]];
                        ArrayList<ProcessorSlot> processorTwo = secondProcessors[_secondNormalisedProcessors[i]];
                        for (int j = 0; j < processorOne.size(); j++) {
                            int diff = processorOne.get(j).getNode().getId() - processorTwo.get(j).getNode().getId();
                            if (diff != 0) {
                                return diff;
                            }
                        }
                    }
                    return 0;
                } else {
                    return setDiff;
                }
            } else {
                return nodeDiff;
            }
        } else {
            return costDiff;
        }
    };

    /**
     * constructs a cache object
     * @param processorCount
     */
    public Cache(int processorCount) {
        _treeSet = new TreeSet<>(comparator);
        _processorCount = processorCount;
        _firstNormalisedProcessors = new int[_processorCount];
        _secondNormalisedProcessors = new int [_processorCount];
    }

    public boolean add(PartialSolution ps) {
        return _treeSet.add(ps);
    }

    public boolean contains(PartialSolution ps) {
       return _treeSet.contains(ps);
    }


}
