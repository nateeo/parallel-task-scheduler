package frontend;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
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
    StackPane _graphComponent;
    GridPane _graphLayout;
    PartialSolution _ps;
    int ABSOLUTE_HEIGHT = 700;
    int GRID_HEIGHT = 80;
    double ABSOLUTE_WIDTH = 100;
    int _unitHeight;
    int _numProcessors;
    double _roundingFactor; //needed for accurate rectangle sizes

    public ScheduleGraph(PartialSolution ps){
        _graphComponent = new StackPane();

        _ps = ps;
        _numProcessors = ps.getProcessors().length;
        _graphLayout = new GridPane();
        computeGridDimensions();
        GridPane gridWithBorders = drawGridBorders();

        _graphComponent.getChildren().add(gridWithBorders);
        _graphComponent.getStyleClass().add("style.css");



    }


    public StackPane generateGraph(PartialSolution ps) throws Exception{

        //URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();

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
                //taskName.setStyle("-fx-text-fill: wheat");
                rectangleLayout.getChildren().addAll(taskBlock, taskName);
                rectangleLayout.getStyleClass().add("rectangle");

                _graphLayout.add(rectangleLayout, i, task.getStart(), 1, height);
                System.out.println(task.getStart());


            }
        }

        _graphComponent.getChildren().add(_graphLayout);

        return _graphComponent;
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
        int actualRows = round(preferredRows);
        int gridHeight = (int)Math.ceil(_ps._cost/(double)actualRows);
        System.out.println("Grid height is "+actualRows);

        GridPane backgroundGrid = new GridPane();

        //assign column constraints
        for (int i = 0; i<_numProcessors; i++){
            ColumnConstraints column = new ColumnConstraints(ABSOLUTE_WIDTH);
            backgroundGrid.getColumnConstraints().add(column);
        }
        //assign row constraints
        for (int j = 0; j<gridHeight; j++){

            RowConstraints row = new RowConstraints(Math.round(_unitHeight*actualRows));
            backgroundGrid.getRowConstraints().add(row);
        }

        for (int i = 0; i<_numProcessors; i++){
            for (int j =0; j<gridHeight; j++){
                Rectangle bg = new Rectangle(ABSOLUTE_WIDTH, _unitHeight*actualRows);
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
