package frontend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TitleController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleNextButtonClick(ActionEvent actionEvent) throws Exception {
        changeScene("src/main/java/frontend/LoadingPage.fxml",actionEvent);
    }

    @FXML
    public void handleCloseButton() {
        Boolean closeOperation = DialogBox.displayConfirmDialogBox("Exit","Are you sure you want to close?");
        if (closeOperation) {
            Platform.exit();
        }
    }

    @FXML
    public void handleSettingsButton(ActionEvent actionEvent) throws Exception{
        changeScene("src/main/java/frontend/Settings.fxml",actionEvent);
    }

    public void changeScene(String filePathToXML, ActionEvent actionEvent) throws Exception{
        URL url = new File(filePathToXML).toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene nextScene = new Scene(root);
        Stage loadingPage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        loadingPage.setScene(nextScene);
    }
}
