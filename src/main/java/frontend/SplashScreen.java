package frontend;

import frontend.Main;
import graph.Graph;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import javafx.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class SplashScreen implements Initializable {

    Graph _graph;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _graph = Main.getGraph();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeRect(100,100,300,300);
    }

    public void changeScene(String filePathToXML, ActionEvent actionEvent) throws Exception{
        URL url = new File(filePathToXML).toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene nextScene = new Scene(root);
        Stage loadingPage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        loadingPage.setScene(nextScene);
    }
}
