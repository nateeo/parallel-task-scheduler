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
    }//####[47]####
//####[49]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_method = null;//####[49]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet() {//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            try {//####[49]####
                __pt__threadQueue_PSPriorityQueueChild_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[49]####
                    PSPriorityQueueChild.class//####[49]####
                });//####[49]####
            } catch (Exception e) {//####[49]####
                e.printStackTrace();//####[49]####
            }//####[49]####
        }//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setTaskIdArgIndexes(0);//####[49]####
        taskinfo.addDependsOn(childQueue);//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setQueueArgIndexes(0);//####[49]####
        taskinfo.setIsPipeline(true);//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue) {//####[49]####
        System.out.println("THREAD DOING SHIT");//####[50]####
        int[][] saved = null;//####[51]####
        PartialSolution ps = null;//####[52]####
        while (childQueue.hasNext()) //####[53]####
        {//####[53]####
            ps = childQueue.getCurrentPartialSolution();//####[54]####
            int[][] temp = _psManager.shit(ps, childQueue, saved);//####[55]####
            saved = temp;//####[56]####
            System.out.println("child queue size " + childQueue.size());//####[57]####
        }//####[58]####
        ps = childQueue.getCurrentPartialSolution();//####[59]####
        System.out.println("8====D adding\n" + ps);//####[60]####
        _solutions.add(ps);//####[61]####
    }//####[62]####
//####[62]####
//####[64]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[64]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[65]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[66]####
        for (int i = 0; i < _cores; i++) //####[67]####
        {//####[67]####
            System.out.println("~~~~~ THread: " + i);//####[68]####
            TaskID id = threadQueue(_childQueues[i]);//####[69]####
            System.out.println("THREAD ID: " + id);//####[70]####
            g.add(id);//####[71]####
            System.out.println("~~~~~ THread finished: " + i);//####[72]####
        }//####[73]####
        g.waitTillFinished();//####[74]####
        System.out.println("FINAL SOLUTION ~~~~~~~~~~~");//####[75]####
        for (PartialSolution p : _solutions) //####[76]####
        {//####[76]####
            System.out.println("NEWWWWWW\n" + p.toString());//####[77]####
        }//####[78]####
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
}//####[89]####
