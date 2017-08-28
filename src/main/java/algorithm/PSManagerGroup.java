package algorithm;

import frontend.Listener;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nateeo on 26/08/2017.
 */
public class PSManagerGroup {
    ArrayList<PSManager> managers = new ArrayList<>();
    public int _cores;
    public int currentIndex = 0;
    PSManager _psManager;
    public static Listener _listener;

    public PSManagerGroup(int cores) {
        _cores = cores;
    }

    public void add(PSManager ps) {
        managers.add(ps);
        if (managers.size() == _cores && Scheduler._visualize) start(); // begin timer
    }

    public void start() {
        new Thread(new Runnable() {
            Timer t = new Timer();
            @Override
            public void run() {
                TimerTask task = new TimerTask() {
                    public void run() {
                        _psManager = managers.get(currentIndex);
                        if (Scheduler._stopTimer) this.cancel();
                        if (_listener != null) {
                            boolean isFinished = false;
                            if (_psManager._currentStatPS._nodes.size() == _psManager._graph.getNodes().size()) isFinished = true;
                            _listener.updateThread(isFinished, _psManager._currentStatPS, currentIndex, _psManager._nodeVisitCounts, _psManager._memory, _psManager._cost,
                                    _psManager._currentFinishTime, _psManager._statesExplored, _psManager._loaded);
                            currentIndex = (currentIndex + 1) % _cores;
                        }
                    }
                };
                t.schedule(task, Scheduler.DELAY_TIME, Scheduler.REFRESH_TIME);
            }
        }).start();
    }
}
