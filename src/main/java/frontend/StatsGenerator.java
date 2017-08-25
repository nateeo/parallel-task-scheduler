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
}