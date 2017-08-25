package frontend;

import algorithm.PartialSolution;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class StatsGenerator {

    private ProgressIndicator _progressBar;
    private TableView<String> _tableView;
    private PartialSolution _ps;
    private TableColumn<String, String> _tableColumn;



    public StatsGenerator(ProgressIndicator pb, TableView tableView){
        //_ps = ps;
        _progressBar = pb;
        _tableView = tableView;
    }

    public void updateStats(double psValue, int ce, int se, int te) {
        _progressBar.setProgress(psValue);
        //_tableView.edit(0,"Value").set(ce);
    }
}