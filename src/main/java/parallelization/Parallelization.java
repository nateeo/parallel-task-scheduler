package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.PartialSolution;//####[5]####
import graph.Graph;//####[6]####
import java.util.ArrayList;//####[8]####
import java.util.HashMap;//####[9]####
import java.util.Map;//####[10]####
import java.util.PriorityQueue;//####[11]####
import java.util.concurrent.ExecutionException;//####[12]####
//####[12]####
//-- ParaTask related imports//####[12]####
import pt.runtime.*;//####[12]####
import java.util.concurrent.ExecutionException;//####[12]####
import java.util.concurrent.locks.*;//####[12]####
import java.lang.reflect.*;//####[12]####
import pt.runtime.GuiThread;//####[12]####
import java.util.concurrent.BlockingQueue;//####[12]####
import java.util.ArrayList;//####[12]####
import java.util.List;//####[12]####
//####[12]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[18]####
public class Parallelization {//####[19]####
    static{ParaTask.init();}//####[19]####
    /*  ParaTask helper method to access private/protected slots *///####[19]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[19]####
        if (m.getParameterTypes().length == 0)//####[19]####
            m.invoke(instance);//####[19]####
        else if ((m.getParameterTypes().length == 1))//####[19]####
            m.invoke(instance, arg);//####[19]####
        else //####[19]####
            m.invoke(instance, arg, interResult);//####[19]####
    }//####[19]####
//####[21]####
    private PSPriorityQueue _parentQueue;//####[21]####
//####[22]####
    private PSManager _psManager;//####[22]####
//####[23]####
    private Graph _graph;//####[23]####
//####[24]####
    private int _processors;//####[24]####
//####[25]####
    private int _cores;//####[25]####
//####[26]####
    private PSPriorityQueueChild[] _childQueues;//####[26]####
//####[27]####
    private ArrayList<PartialSolution> _solutions;//####[27]####
//####[30]####
    public Parallelization(PSPriorityQueue priorityQueue, PSManager psManager, int cores) {//####[30]####
        _parentQueue = priorityQueue;//####[31]####
        _psManager = psManager;//####[32]####
        _cores = cores;//####[33]####
        _childQueues = new PSPriorityQueueChild[cores];//####[34]####
        _solutions = new ArrayList<PartialSolution>();//####[35]####
        initialiseQueues();//####[37]####
    }//####[39]####
//####[41]####
    public void initialiseQueues() {//####[41]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[42]####
        for (int i = 0; i < _cores; i++) //####[44]####
        {//####[44]####
            _childQueues[i] = queues[i];//####[45]####
        }//####[46]####
        System.out.println("************Child queues************");//####[47]####
        printChildQueues();//####[48]####
        System.out.println("************Child Queues************");//####[49]####
    }//####[50]####
//####[52]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_method = null;//####[52]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet() {//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[52]####
            try {//####[52]####
                __pt__threadQueue_PSPriorityQueueChild_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[52]####
                    PSPriorityQueueChild.class//####[52]####
                });//####[52]####
            } catch (Exception e) {//####[52]####
                e.printStackTrace();//####[52]####
            }//####[52]####
        }//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setParameters(childQueue);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setTaskIdArgIndexes(0);//####[52]####
        taskinfo.addDependsOn(childQueue);//####[52]####
        taskinfo.setParameters(childQueue);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(0);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setParameters(childQueue);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue) {//####[52]####
        PartialSolution ps = null;//####[54]####
        while (childQueue.hasNext()) //####[55]####
        {//####[55]####
            ps = childQueue.getCurrentPartialSolution();//####[56]####
            _psManager.generateChildren(ps, childQueue);//####[57]####
        }//####[58]####
        System.out.println("child queue size " + childQueue.size());//####[59]####
        ps = childQueue.getCurrentPartialSolution();//####[60]####
        _solutions.add(ps);//####[62]####
    }//####[63]####
//####[63]####
//####[65]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[65]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[66]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[67]####
        for (int i = 0; i < _cores; i++) //####[68]####
        {//####[68]####
            TaskID id = threadQueue(_childQueues[i]);//####[70]####
            g.add(id);//####[72]####
        }//####[74]####
        g.waitTillFinished();//####[75]####
        printSolutions();//####[77]####
        PartialSolution solution = null;//####[79]####
        int finalTime = -1;//####[80]####
        for (int i = 0; i < _solutions.size(); i++) //####[81]####
        {//####[81]####
            int psFinishTime = _solutions.get(i)._latestSlot.getFinish();//####[82]####
            if (finalTime == -1 || psFinishTime < finalTime) //####[83]####
            {//####[83]####
                solution = _solutions.get(i);//####[84]####
                finalTime = psFinishTime;//####[85]####
            }//####[86]####
        }//####[87]####
        return solution;//####[88]####
    }//####[89]####
//####[91]####
    public void printSolutions() {//####[91]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[93]####
        for (PartialSolution solution : _solutions) //####[95]####
        {//####[95]####
            System.out.println(solution.toString());//####[96]####
        }//####[97]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[99]####
    }//####[100]####
//####[102]####
    public void printChildQueues() {//####[102]####
        for (int i = 0; i < _childQueues.length; i++) //####[103]####
        {//####[103]####
            System.out.println("queue " + i);//####[105]####
            PSPriorityQueueChild queue = _childQueues[i];//####[107]####
            queue.printQueue();//####[108]####
        }//####[109]####
    }//####[110]####
}//####[110]####
