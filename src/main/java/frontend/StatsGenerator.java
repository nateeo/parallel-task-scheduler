package frontend;

import algorithm.PartialSolution;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class StatsGenerator {

    private ProgressIndicator _progressBar;
    private TableView<String> _tableView;
    private PartialSolution _ps;
    private Label _timer;
    private Label _currentFinishTime;
    private Label _underestimate;
    private Label _statesExplored;
    private Label _memory;


    public StatsGenerator(ProgressIndicator progressBar, Label timer, Label currentFinishTime, Label underestimate, Label statesExplored, Label memory) {
        _progressBar = progressBar;
        _timer = timer;
        _currentFinishTime = currentFinishTime;
        _underestimate = underestimate;
        _statesExplored = statesExplored;
        _memory = memory;
    }

    public void updateStats(double pvalue, int timer, int currentFinishTime, int underestimates, int statesExplored, int memory) {
        _progressBar.setProgress(pvalue);
        _timer.setText(Integer.toString(timer));
        _currentFinishTime.setText(Integer.toString(currentFinishTime));
        _underestimate.setText(Integer.toString(underestimates));
        _statesExplored.setText(Integer.toString(statesExplored));
        _memory.setText(Integer.toString(memory));
    }
}