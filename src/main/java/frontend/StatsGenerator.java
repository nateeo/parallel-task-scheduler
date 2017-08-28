package frontend;

import algorithm.PartialSolution;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.concurrent.TimeUnit;

/**
 *  This class is created for visualisation of useful to stats of the running algorithm, it is able to update itself
 *  by updating the text inside label components.
 *  Author: Edison Rho, Samule Li
 */

public class StatsGenerator {

    private ProgressIndicator _progressBar;
    private Label _timer;
    private Label _currentThread;
    private Label _currentFinishTime;
    private Label _underestimate;
    private Label _statesExplored;
    private Label _memory;
    private AnimationTimer _animationTimer;


    /**
     * generators the front end stats
     * @param progressBar
     * @param timer
     * @param currentThread
     * @param currentFinishTime
     * @param underestimate
     * @param statesExplored
     * @param memory
     */
    public StatsGenerator(ProgressIndicator progressBar, Label timer, Label currentThread, Label currentFinishTime, Label underestimate, Label statesExplored, Label memory) {

        _progressBar = progressBar;
        _timer = timer;
        _currentFinishTime = currentFinishTime;
        _currentThread = currentThread;
        _underestimate = underestimate;
        _statesExplored = statesExplored;
        _memory = memory;
        long startTime = System.currentTimeMillis();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillisec = System.currentTimeMillis() - startTime;
                String output= String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(elapsedMillisec),
                        TimeUnit.MILLISECONDS.toSeconds(elapsedMillisec) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillisec))
                );
                Platform.runLater(() -> {
                    _timer.setText(output);
                });

            }
        }.start();
    }

    /**
     * updates the values of all the stats in the front end
     * @param isFinished
     * @param pvalue
     * @param currentThread
     * @param currentFinishTime
     * @param underestimates
     * @param statesExplored
     * @param memory
     */
    public void updateStats(boolean isFinished, double pvalue, int currentThread, int currentFinishTime, int underestimates, int statesExplored, int memory) {

        String currentThreadText = currentThread == 0 ? "Main" : currentThread + "";
        _progressBar.setProgress(pvalue);
        _currentThread.setText(currentThreadText);
        _currentFinishTime.setText((currentFinishTime + ""));
        _underestimate.setText((underestimates + ""));
        _statesExplored.setText((statesExplored+ ""));
        _memory.setText((memory + ""));
        if (isFinished) {
            String finalTime = _timer.getText();
            _timer = new Label();
            _timer.setText(finalTime);
        }
    }
}