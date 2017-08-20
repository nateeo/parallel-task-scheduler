package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.PartialSolution;//####[5]####
import graph.Graph;//####[6]####
import java.util.HashMap;//####[8]####
import java.util.Map;//####[9]####
import java.util.PriorityQueue;//####[10]####
import java.util.concurrent.ExecutionException;//####[11]####
//####[11]####
//-- ParaTask related imports//####[11]####
import pt.runtime.*;//####[11]####
import java.util.concurrent.ExecutionException;//####[11]####
import java.util.concurrent.locks.*;//####[11]####
import java.lang.reflect.*;//####[11]####
import pt.runtime.GuiThread;//####[11]####
import java.util.concurrent.BlockingQueue;//####[11]####
import java.util.ArrayList;//####[11]####
import java.util.List;//####[11]####
//####[11]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[17]####
public class Parallelization {//####[18]####
    static{ParaTask.init();}//####[18]####
    /*  ParaTask helper method to access private/protected slots *///####[18]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[18]####
        if (m.getParameterTypes().length == 0)//####[18]####
            m.invoke(instance);//####[18]####
        else if ((m.getParameterTypes().length == 1))//####[18]####
            m.invoke(instance, arg);//####[18]####
        else //####[18]####
            m.invoke(instance, arg, interResult);//####[18]####
    }//####[18]####
//####[20]####
    private PSPriorityQueue _parentQueue;//####[20]####
//####[21]####
    private PSManager _psManager;//####[21]####
//####[22]####
    private Graph _graph;//####[22]####
//####[23]####
    private int _processors;//####[23]####
//####[24]####
    private int _cores;//####[24]####
//####[25]####
    private PSPriorityQueueChild[] _childQueues;//####[25]####
//####[26]####
    private PartialSolution _solution;//####[26]####
//####[27]####
    private double _finalTime;//####[27]####
//####[30]####
    public Parallelization(PSPriorityQueue priorityQueue, PSManager psManager, int cores) {//####[30]####
        _parentQueue = priorityQueue;//####[31]####
        _psManager = psManager;//####[32]####
        _cores = cores;//####[33]####
        _childQueues = new PSPriorityQueueChild[cores];//####[34]####
        _solution = null;//####[35]####
        _finalTime = Double.POSITIVE_INFINITY;//####[36]####
        initialiseQueues();//####[38]####
    }//####[40]####
//####[42]####
    public void initialiseQueues() {//####[42]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[43]####
        for (int i = 0; i < _cores; i++) //####[45]####
        {//####[45]####
            _childQueues[i] = queues[i];//####[46]####
        }//####[47]####
    }//####[48]####
//####[50]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_method = null;//####[50]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet() {//####[50]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[50]####
            try {//####[50]####
                __pt__threadQueue_PSPriorityQueueChild_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[50]####
                    PSPriorityQueueChild.class//####[50]####
                });//####[50]####
            } catch (Exception e) {//####[50]####
                e.printStackTrace();//####[50]####
            }//####[50]####
        }//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue) {//####[50]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[50]####
        return threadQueue(childQueue, new TaskInfo());//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskInfo taskinfo) {//####[50]####
        // ensure Method variable is set//####[50]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[50]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[50]####
        }//####[50]####
        taskinfo.setParameters(childQueue);//####[50]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[50]####
        taskinfo.setInstance(this);//####[50]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue) {//####[50]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[50]####
        return threadQueue(childQueue, new TaskInfo());//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[50]####
        // ensure Method variable is set//####[50]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[50]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[50]####
        }//####[50]####
        taskinfo.setTaskIdArgIndexes(0);//####[50]####
        taskinfo.addDependsOn(childQueue);//####[50]####
        taskinfo.setParameters(childQueue);//####[50]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[50]####
        taskinfo.setInstance(this);//####[50]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue) {//####[50]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[50]####
        return threadQueue(childQueue, new TaskInfo());//####[50]####
    }//####[50]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[50]####
        // ensure Method variable is set//####[50]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[50]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[50]####
        }//####[50]####
        taskinfo.setQueueArgIndexes(0);//####[50]####
        taskinfo.setIsPipeline(true);//####[50]####
        taskinfo.setParameters(childQueue);//####[50]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[50]####
        taskinfo.setInstance(this);//####[50]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[50]####
    }//####[50]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue) {//####[50]####
        System.out.println("THREAD DOING SHIT");//####[51]####
        PartialSolution ps = null;//####[52]####
        while (childQueue.hasNext()) //####[53]####
        {//####[53]####
            ps = childQueue.getCurrentPartialSolution();//####[54]####
            _psManager.generateChildren(ps, childQueue);//####[55]####
        }//####[56]####
        ps = childQueue.getCurrentPartialSolution();//####[57]####
        if (ps._currentFinishTime < _finalTime) //####[58]####
        {//####[58]####
            _solution = ps;//####[59]####
            _finalTime = ps.getCurrentFinshTime();//####[60]####
        }//####[61]####
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
            g.add(id);//####[70]####
            System.out.println("~~~~~ THread finished: " + i);//####[71]####
        }//####[72]####
        g.waitTillFinished();//####[73]####
        return _solution;//####[74]####
    }//####[75]####
}//####[75]####
