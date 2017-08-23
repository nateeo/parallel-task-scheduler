package frontend;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by edisonrho on 20/08/17.
 *
 * log stack
 * - convert fields from double to int
 */
public class ScheduleGraph {
    Pane _decoratedGraph;
    StackPane _graphComponent;
    GridPane _graphLayout;
    PartialSolution _ps;
    int ABSOLUTE_HEIGHT = 700;
    int GRID_HEIGHT = 80;
    double ABSOLUTE_WIDTH = 100;
    int _unitHeight;
    int _numProcessors;
    int _gridHeight;
    int _actualRows;
    double _roundingFactor; //needed for accurate rectangle sizes

    public ScheduleGraph(PartialSolution ps){
        _graphComponent = new StackPane();

        _ps = ps;
        _numProcessors = ps.getProcessors().length;
        _graphLayout = new GridPane();
        computeGridDimensions();
        GridPane gridWithBorders = drawGridBorders();

        _graphComponent.getChildren().add(gridWithBorders);
        _graphComponent.getStylesheets().add("style.css");

        _decoratedGraph = new HBox();
        _decoratedGraph.getStylesheets().add("style.css");


    }


    public Pane generateGraph(PartialSolution ps) throws Exception{

        ArrayList<ProcessorSlot>[] processorLists = ps.getProcessors();
        for (int i = 0; i<processorLists.length; i++){
            for (ProcessorSlot task : processorLists[i]){
                //generate the rectangle
                int height = task.getNode().getWeight();
                Rectangle taskBlock = new Rectangle(ABSOLUTE_WIDTH, _unitHeight*height);
                taskBlock.setFill(Color.GREEN);
                taskBlock.setStroke(Color.WHITE);
                taskBlock.setStrokeWidth(1);
                taskBlock.getStyleClass().add("rectangle");
                //stackpane encloses rectangle and text (required to nest text inside rectangle)
                StackPane rectangleLayout = new StackPane();
                Text taskName = new Text(task.getNode().getName());
                rectangleLayout.getChildren().addAll(taskBlock, taskName);
                rectangleLayout.getStyleClass().add("rectangle");

                _graphLayout.add(rectangleLayout, i, task.getStart(), 1, height);
                System.out.println(task.getStart());


            }
        }
        _graphComponent.getChildren().add(_graphLayout);


        decorateGraph();











        return _decoratedGraph;
    }

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

    private void computeGridDimensions(){
        int totalTime = _ps._cost;
        _unitHeight = ABSOLUTE_HEIGHT/totalTime;
        double roundedUnitHeight = Math.round(_unitHeight);
        _roundingFactor = roundedUnitHeight/_unitHeight;

        System.out.println("UNIT HEIGHT:" + _unitHeight);
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

    private GridPane drawGridBorders(){
        int preferredRows = GRID_HEIGHT/_unitHeight;
        _actualRows = round(preferredRows);
        _gridHeight = (int)Math.ceil(_ps._cost/(double)_actualRows);
        System.out.println("Grid height is "+_actualRows);

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

        for (int i = 0; i<_numProcessors; i++){
            for (int j =0; j<_gridHeight; j++){
                Rectangle bg = new Rectangle(ABSOLUTE_WIDTH, _unitHeight*_actualRows);
                bg.setStrokeWidth(1);
                bg.setStroke(Color.DARKGRAY);
                bg.setFill(Color.LINEN);

                backgroundGrid.add(bg,i,j);
            }
        }

        return backgroundGrid;





    }

    private int round(int rows){
        int roundedRows = rows;
        if (1<=roundedRows && roundedRows<=4){
            return roundedRows;
        } else if (5<=roundedRows && roundedRows<=99){
            roundedRows = Math.round(roundedRows/5)*5;
        } else {
            roundedRows = Math.round(roundedRows/100)*100;
        }
        return roundedRows;
    }

}
