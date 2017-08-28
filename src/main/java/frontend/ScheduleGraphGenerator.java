package frontend;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import scheduler.Scheduler;

import java.util.ArrayList;

/**
 * Created by edisonrho on 20/08/17.
 *
 * The class uses a partial solution to generate a Gantt-chart style of a scheduled
 * task graph. The graph is outputted as a Javafx Pane object, thus integrating
 * the output should be trivial.
 */
public class ScheduleGraphGenerator {
    //input partial solution
    private PartialSolution _ps;

    //JAVAFX PANES
    private ScrollPane _returnPane;
    private Pane _decoratedGraph;
    private StackPane _graphComponent;
    private GridPane _graphLayout;

    //FIXED DIMENSIONS
    private int ABSOLUTE_HEIGHT = 700;//height of the output
    private int DESIRED_WIDTH= 260;
    private int ABSOLUTE_WIDTH = 100;
    private int GRID_HEIGHT = 80;//preferred grid height. Will be rounded to a 'neat' number

    //RELATIVE DIMENSIONS - relative to the graph input
    private int _unitHeight;
    private int _numProcessors;
    private int _gridHeight;
    private int _actualRows;

    /**
     * Create empty graph
     */
    public ScheduleGraphGenerator(){
        _numProcessors = Scheduler._processors;
        ABSOLUTE_WIDTH = DESIRED_WIDTH/_numProcessors;
        _unitHeight = 3;

        _ps = new PartialSolution(_numProcessors);
        _actualRows = 20;
        _gridHeight = 12;


    }

    /**
     * create the generator based off of the given partial solution.
     * @param ps
     */
    public ScheduleGraphGenerator(PartialSolution ps){
        _graphComponent = new StackPane();
        _ps = ps;
        _numProcessors = ps.getProcessors().length;
        ABSOLUTE_WIDTH = DESIRED_WIDTH/_numProcessors;
        _graphLayout = new GridPane();
        computeGridDimensions();//calculate dimensions of graph
        //use the preferred grid height size to compute how many grid rows it would require
        int preferredRows = GRID_HEIGHT/_unitHeight;
        _actualRows = round(preferredRows);//round grid to neat number
        _gridHeight = (int)Math.ceil(_ps._cost/(double)_actualRows);
        GridPane gridWithBorders = drawGridBorders();//draws the background grid of the graph

        _graphComponent.getChildren().add(gridWithBorders);
        _graphComponent.getStylesheets().add("style.css");

        _decoratedGraph = new HBox();
        _decoratedGraph.getStylesheets().add("style.css");

//        _returnPane = new ScrollPane();
//        _returnPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

    /**
     * Generates the graph based on the partial solution as well as the preferred dimensions of the
     * graph.
     * @return Pane Node containing the graph
     * @throws Exception
     */
    public ScrollPane generateGraph() {
        _returnPane = new ScrollPane();

        ArrayList<ProcessorSlot>[] processorLists = _ps.getProcessors();
        for (int i = 0; i<processorLists.length; i++){
            for (ProcessorSlot task : processorLists[i]){
                //generate the rectangle
                int height = task.getNode().getWeight();

                StackPane rectangleLayout = new StackPane();
                rectangleLayout.setMinWidth(ABSOLUTE_WIDTH);
                rectangleLayout.setMaxWidth(ABSOLUTE_WIDTH);
                rectangleLayout.setMinHeight(_unitHeight*height);
                rectangleLayout.setMaxHeight(_unitHeight*height);
                Text taskName = new Text(task.getNode().getName());
                taskName.setFill(Color.WHITE);
                rectangleLayout.getChildren().addAll(taskName);
                rectangleLayout.getStyleClass().add("rectangle");

                _graphLayout.add(rectangleLayout, i, task.getStart(), 1, height);


            }
        }
        _graphComponent.getChildren().add(_graphLayout);

        decorateGraph();

        _returnPane.setContent(_decoratedGraph);

        return _returnPane;
    }



    /**
     * Helper function for decorating the graph with labels
     */
    private void decorateGraph(){
        VBox rightContainer = new VBox();
        //text label container
        HBox topLabelContainer = new HBox();
        for (int i = 0; i<_numProcessors; i++){



            StackPane labelPane = new StackPane();
            labelPane.setMinWidth(ABSOLUTE_WIDTH);
            labelPane.setPrefWidth(ABSOLUTE_WIDTH);
            labelPane.setMinHeight(25);
            Text processorText = new Text(i+"");
            labelPane.getChildren().addAll(processorText);


            topLabelContainer.getChildren().add(labelPane);
        }

        StackPane topNameContainer = new StackPane();
        Text topName = new Text("Processors");
        topNameContainer.setMinHeight(20);
        topNameContainer.setPrefHeight(20);
        topNameContainer.getChildren().add(topName);


        rightContainer.getChildren().addAll(topNameContainer, topLabelContainer, _graphComponent);

        //decorate horizontal
        HBox leftContainer = new HBox();
        VBox leftGridContainer = new VBox();
        StackPane bufferPane = new StackPane();
        bufferPane.setMinHeight(47);
        bufferPane.setMinWidth(10);
        Text ZeroUnit = new Text("0");
        bufferPane.setAlignment(Pos.BOTTOM_CENTER);
        bufferPane.getChildren().add(ZeroUnit);
        leftGridContainer.getChildren().add(bufferPane);


        int labelCounter = 0;
        for (int i = 0; i <_gridHeight; i++){
            StackPane pane = new StackPane();
            pane.setMinHeight(_unitHeight*_actualRows);
            pane.setMinWidth(20);

            labelCounter+= _actualRows;
            Text numberLabel = new Text(labelCounter+"");

            pane.setAlignment(Pos.BOTTOM_CENTER);
            pane.getChildren().add(numberLabel);

            leftGridContainer.getChildren().add(pane);
        }

        Text leftLabel = new Text("Time");
        StackPane leftLabelContainer = new StackPane();
        leftLabelContainer.getChildren().add(leftLabel);
        leftContainer.getChildren().addAll(leftLabelContainer, leftGridContainer);

        _decoratedGraph.getChildren().addAll(leftContainer, rightContainer);

    }

    /**
     * Helper function for computing the height of each grid unit.
     * Uses the maximum finish time of the partial solution to compute the conversion
     * factor when it is translated into the given window dimensions.
     */
    private void computeGridDimensions(){
        int totalTime = _ps._cost;
        _unitHeight = ABSOLUTE_HEIGHT/totalTime;
        if (_unitHeight==0){
            _unitHeight=1;
        }

        //assign column constraints
        for (int i = 0; i<_numProcessors; i++){
            ColumnConstraints column = new ColumnConstraints(ABSOLUTE_WIDTH);
            _graphLayout.getColumnConstraints().add(column);
        }
        //assign row constraints
        for (int j = 0; j<totalTime; j++){

            RowConstraints row = new RowConstraints(Math.round(_unitHeight));
            _graphLayout.getRowConstraints().add(row);
        }




    }

    /**
     * initialise schedule with a blank schedule
     * @return
     */
    public ScrollPane initialise(){
        GridPane backgroundGrid = drawGridBorders();
        ScrollPane scrollPane = new ScrollPane();

        VBox rightContainer = new VBox();
        //text label container
        HBox topLabelContainer = new HBox();
        for (int i = 0; i<_numProcessors; i++){
            StackPane labelPane = new StackPane();
            labelPane.setMinWidth(ABSOLUTE_WIDTH);
            labelPane.setPrefWidth(ABSOLUTE_WIDTH);
            labelPane.setMinHeight(25);
            Text processorText = new Text(i+"");
            labelPane.getChildren().addAll(processorText);

            topLabelContainer.getChildren().add(labelPane);
        }

        StackPane topNameContainer = new StackPane();
        Text topName = new Text("Processors");
        topNameContainer.setMinHeight(20);
        topNameContainer.setPrefHeight(20);
        topNameContainer.getChildren().add(topName);


        rightContainer.getChildren().addAll(topNameContainer, topLabelContainer, backgroundGrid);
        scrollPane.setContent(rightContainer);

        return scrollPane;


    }

    /**
     * Helper function for creating a ruled grid.
     * @return
     */
    private GridPane drawGridBorders(){

        GridPane backgroundGrid = new GridPane();

        //assign column constraints
        for (int i = 0; i<_numProcessors; i++){
            ColumnConstraints column = new ColumnConstraints(ABSOLUTE_WIDTH);
            backgroundGrid.getColumnConstraints().add(column);
        }
        //assign row constraints
        for (int j = 0; j<_gridHeight; j++){

            RowConstraints row = new RowConstraints(Math.round(_unitHeight*_actualRows));
            backgroundGrid.getRowConstraints().add(row);
        }
        //create the grid
        for (int i = 0; i<_numProcessors; i++){
            for (int j =0; j<_gridHeight; j++){

                Pane bg = new Pane();
                bg.setMinWidth(ABSOLUTE_WIDTH);
                bg.setMaxWidth(ABSOLUTE_WIDTH);
                bg.setMinHeight(_unitHeight*_actualRows);
                bg.setMaxHeight(_unitHeight*_actualRows);
                bg.getStyleClass().add("grid-cell");
                backgroundGrid.add(bg,i,j);
            }
        }

        return backgroundGrid;


    }

    /**
     * round the preferred number of arbitrary rows to a 'neat', rounded value
     * for nice labelling of the graph.
     * @param rows
     * @return
     */
    private int round(int rows){
        int roundedRows = rows;
        if (1<=roundedRows && roundedRows<=4){
            return roundedRows;
        } else if (5<=roundedRows && roundedRows<=99){
            roundedRows = Math.round(roundedRows/5)*5;//round to nearest 5
        } else {
            roundedRows = Math.round(roundedRows/100)*100;//round to nearest 100
        }
        return roundedRows;
    }

}
