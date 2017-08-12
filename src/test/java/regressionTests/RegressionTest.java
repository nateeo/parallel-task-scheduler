package regressionTests;

import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import dotParser.Parser;
import graph.Graph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by edisonrho on 12/08/17.
 */
public class RegressionTest {
    String MAPPERTEXT= "input_Mapper.txt";
    Map<String, String[]> _costDictionary;

    @Before
    public void readGraphs(){
        File graphMap = new File(MAPPERTEXT);

        _costDictionary = new HashMap<String, String[]>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(graphMap));
            String line = br.readLine();
            while (line != null){
                String[] graphEntry = line.split(":");

                String[] processorCosts = graphEntry[1].split(",");

                _costDictionary.put(graphEntry[0], processorCosts);
                line = br.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testAllInputs(){
        Set<String> inputFiles = _costDictionary.keySet();
        //loop through each graph
        for (String graphFileName : inputFiles){
            String[] processorArray =_costDictionary.get(graphFileName);
            //create graph
            File file = new File("input-graphs/"+graphFileName);
            Graph graph = Parser.parseDotFile(file);

            int processorNumber = 1;
            //for each processor
            for (String processorString : processorArray){
                int expectedCost = Integer.parseInt(processorString);

                //pass if 0
                if (expectedCost==0){
                    processorNumber++;
                    continue;
                }

                PSPriorityQueue priorityQueue = new PSPriorityQueue(graph, processorNumber);
                PartialSolution ps = null;
                PSManager psManager = new PSManager(processorNumber, graph);
                //find the optimal partial solution
                while (priorityQueue.hasNext()) {
                    ps = priorityQueue.getCurrentPartialSolution();
                    psManager.generateChildren(ps, priorityQueue);
                }
                ps = priorityQueue.getCurrentPartialSolution();

                int testCost = ps._cost;
                assertEquals(expectedCost, testCost);

                processorNumber++;//update processor number




            }


        }
    }



}
