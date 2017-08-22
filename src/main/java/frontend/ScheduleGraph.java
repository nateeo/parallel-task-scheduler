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
    int ABSOLUTE_HEIGHT = 800;
    int GRID_HEIGHT = 80;
    int ABSOLUTE_WIDTH = 100;
    int _unitHeight;
    int _numProcessors;

    public ScheduleGraph(PartialSolution ps){
        _graphComponent = new StackPane();

        _ps = ps;
        _numProcessors = ps.getProcessors().length;
        _graphLayout = new GridPane();
        //_graphLayout.getStylesheets().add("style.css");
        computeGridDimensions();
        GridPane gridWithBorders = drawGridBorders();

        _graphComponent.getChildren().add(gridWithBorders);


    }


    public StackPane generateGraph(PartialSolution ps) throws Exception{

        //URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();


        //drawGridBorders();

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

        //_graphLayout.setGridLinesVisible(true);
        _graphComponent.getChildren().add(_graphLayout);

        return _graphComponent;
    }

    private void computeGridDimensions(){
        int totalTime = _ps._cost;
        _unitHeight = ABSOLUTE_HEIGHT/totalTime;
        //assign column constraints
        for (int i = 0; i<_ps.getProcessors().length; i++){
            ColumnConstraints column = new ColumnConstraints(ABSOLUTE_WIDTH);
            _graphLayout.getColumnConstraints().add(column);
        }
        //assign row constraints
        for (int j = 0; j<totalTime; j++){
            RowConstraints row = new RowConstraints(_unitHeight);
            _graphLayout.getRowConstraints().add(row);
        }


    }

    private GridPane drawGridBorders(){
        double absolute = ABSOLUTE_HEIGHT;
        double finishTime = _ps._cost;
        double gridHeightInRealTime = GRID_HEIGHT*finishTime/absolute;
        double roundedGridHeightInRealTime = round(gridHeightInRealTime);
        int roundedGridHeightInGridTime = (int)Math.round(roundedGridHeightInRealTime*absolute/finishTime);

        //compute number of cells required in y direction
        int height = ABSOLUTE_HEIGHT/roundedGridHeightInGridTime;


        GridPane gridBorderPane = new GridPane();
        for (int i = 0; i<_numProcessors; i++){
            ColumnConstraints column = new ColumnConstraints(ABSOLUTE_WIDTH);
            gridBorderPane.getColumnConstraints().add(column);
        }
        //assign row constraints
        for (int j = 0; j<height; j++){
            RowConstraints row = new RowConstraints(roundedGridHeightInGridTime);
            gridBorderPane.getRowConstraints().add(row);
        }

        for (int i = 0; i<_numProcessors; i++){
            for (int j = 0; j<height; j++){
                //Pane pane = new Pane();
                //pane.getStyleClass().add("gridCell");
                Rectangle pane = new Rectangle(ABSOLUTE_WIDTH, roundedGridHeightInGridTime);
                pane.setFill(Color.HONEYDEW);
                pane.setStroke(Color.DARKGRAY);
                pane.setStrokeWidth(1);
                pane.getStyleClass().add("grid-cell");
                gridBorderPane.add(pane,i,j);
            }
        }

        return gridBorderPane;






    }

    private double round(double gridHeightInRealTime){
        System.out.println("40px in real time is:" + gridHeightInRealTime);
        double roundedValue=gridHeightInRealTime;
        if (0<=gridHeightInRealTime && gridHeightInRealTime<=4){
            return roundedValue;
        } else if (5<=gridHeightInRealTime && gridHeightInRealTime<=99){
            roundedValue = Math.round(gridHeightInRealTime/5)*5;
        } else {
            roundedValue = Math.round(gridHeightInRealTime/100)*100;
        }
        System.out.println("Rounded value: " +roundedValue);
        return roundedValue;
    }

}
