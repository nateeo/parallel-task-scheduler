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
 * task graph. The graph is outputted as a Javafx ScrollPane object, thus integrating
 * the output should be trivial.
 * Classes that call this class should first input a partial solution to set up the class,
 * then subsequently call generateGraph().
 */
public class ScheduleGraphGenerator {
    //input partial solution
    private PartialSolution _ps;

    //JAVAFX PANES
    private ScrollPane _returnPane;//top level container
    private Pane _decoratedGraph;//second level container with axis labels
    private StackPane _graphComponent;//third level container with graph and grids
    private GridPane _graphLayout;//fourth level container with just graph

    //FIXED DIMENSIONS
    private int ABSOLUTE_HEIGHT = 700;//height of the output graph
    private int DESIRED_WIDTH= 260;//width of output graph
    private int ABSOLUTE_WIDTH = 100;//width of grid cell
    private int GRID_HEIGHT = 80;//preferred grid height. Will be rounded to a 'neat' number

    //RELATIVE DIMENSIONS - relative to the graph input
    private int _unitHeight;//height of a gridlayout cell
    private int _numProcessors;//number of processors for partial solution
    private int _gridHeight;//height of a grid cell
    private int _actualRows;//number of grid cell rows.

    /**
     * Constructor for create an empty graph.
     * Used to initialize the visualization
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
     * Constructor for generating a grpah based off a partial solution.
     * create the generator based off of the given partial solution
     * by updating the fields to be used to generate graph.
     * @param ps target partial solution
     */
    public ScheduleGraphGenerator(PartialSolution ps){
        _graphComponent = new StackPane();
        _ps = ps;
        _numProcessors = ps.getProcessors().length;
        _graphLayout = new GridPane();

        //divide the graph width to processor columns based on how many processors there are
        ABSOLUTE_WIDTH = DESIRED_WIDTH/_numProcessors;

        computeGridDimensions();//calculate dimensions of graph

        //use the preferred grid height size to compute how many grid rows it would require
        int preferredRows = GRID_HEIGHT/_unitHeight;
        _actualRows = round(preferredRows);//round grid to neat number
        //compute background grid height based on the solution's cost underestimate
        //and the the number of grid rows required for the preferred grid height
        _gridHeight = (int)Math.ceil(_ps._cost/(double)_actualRows);
        GridPane gridWithBorders = drawGridBorders();//draws the background grid of the graph

        _graphComponent.getChildren().add(gridWithBorders);
        _graphComponent.getStylesheets().add("style.css");

        _decoratedGraph = new HBox();
        _decoratedGraph.getStylesheets().add("style.css");

    }

    /**
     * Generates the graph based on the partial solution as well as the preferred dimensions of the
     * graph.
     * @return Pane Node containing the graph
     */
    public ScrollPane generateGraph() {
        _returnPane = new ScrollPane();

        ArrayList<ProcessorSlot>[] processorLists = _ps.getProcessors();
        for (int i = 0; i<processorLists.length; i++){
            for (ProcessorSlot task : processorLists[i]){
                //generate the task rectangle
                int height = task.getNode().getWeight();

                StackPane rectangleLayout = new StackPane();//stackpane as the task rectangle
                rectangleLayout.setMinWidth(ABSOLUTE_WIDTH);
                rectangleLayout.setMaxWidth(ABSOLUTE_WIDTH);
                //translate the task's weight to the gridlayout unit system
                rectangleLayout.setMinHeight(_unitHeight*height);
                rectangleLayout.setMaxHeight(_unitHeight*height);
                Text taskName = new Text(task.getNode().getName());
                taskName.setFill(Color.WHITE);
                rectangleLayout.getChildren().addAll(taskName);
                rectangleLayout.getStyleClass().add("rectangle");
                //place the task into the graph with the appropriate coordinate.
                _graphLayout.add(rectangleLayout, i, task.getStart(), 1, height);

            }
        }
        _graphComponent.getChildren().add(_graphLayout);

        //enclose the graph into a decorated graph with labels
        decorateGraph();

        _returnPane.setContent(_decoratedGraph);//set content of scrollpane

        return _returnPane;
    }



    /**
     * Helper function for decorating the graph with labels in the axis.
     */
    private void decorateGraph(){
        VBox rightContainer = new VBox();//container for graph and top axis
        //text label container
        HBox topLabelContainer = new HBox();

        //label the top axis with processor numbers
        for (int i = 0; i<_numProcessors; i++){
            StackPane labelPane = new StackPane();
            labelPane.setMinWidth(ABSOLUTE_WIDTH);
            labelPane.setPrefWidth(ABSOLUTE_WIDTH);
            labelPane.setMinHeight(25);
            Text processorText = new Text((i+1)+"");//processors start at 1
            labelPane.getChildren().addAll(processorText);
            topLabelContainer.getChildren().add(labelPane);
        }

        //container for top axis label
        StackPane topNameContainer = new StackPane();
        Text topName = new Text("Processor ID");
        topNameContainer.setMinHeight(20);
        topNameContainer.setPrefHeight(20);
        topNameContainer.getChildren().add(topName);

        //populate the graph+label+grid labels
        rightContainer.getChildren().addAll(topNameContainer, topLabelContainer, _graphComponent);

        //decorate horizontal (i.e. everything)
        HBox leftContainer = new HBox();
        VBox leftGridContainer = new VBox();//grid label container
        StackPane bufferPane = new StackPane();//buffer to start the grid from top.
        bufferPane.setMinHeight(47);
        bufferPane.setMinWidth(10);
        Text ZeroUnit = new Text("0");
        bufferPane.setAlignment(Pos.BOTTOM_CENTER);
        bufferPane.getChildren().add(ZeroUnit);//initialize grid with 0
        leftGridContainer.getChildren().add(bufferPane);

        //populate the left grid labels
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

        Text leftLabel = new Text("Time");//left label
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
     * Generates an empty graph. To be used at the start of the program where
     * the algorithm has not started yet.
     * @return empty graph
     */
    public ScrollPane initialise(){
        //generate the graph layout grids
        GridPane backgroundGrid = drawGridBorders();
        ScrollPane scrollPane = new ScrollPane();

        //decorate the empty graph with units.
        VBox rightContainer = new VBox();
        //text label container
        HBox topLabelContainer = new HBox();
        //for each processor, label with the processor ID.
        for (int i = 0; i<_numProcessors; i++){
            StackPane labelPane = new StackPane();
            labelPane.setMinWidth(ABSOLUTE_WIDTH);
            labelPane.setPrefWidth(ABSOLUTE_WIDTH);
            labelPane.setMinHeight(25);
            Text processorText = new Text((i+1)+"");
            labelPane.getChildren().addAll(processorText);

            topLabelContainer.getChildren().add(labelPane);
        }

        StackPane topNameContainer = new StackPane();
        Text topName = new Text("Processor ID");
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
        //for each cell, generate a background grid cell.
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
