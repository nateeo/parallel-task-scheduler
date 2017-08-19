package frontend;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Pop-up dialog box which blocks all other input to the main window until user clicks
 * 'yes' or 'no' button.
 */
public class DialogBox {

    private static boolean _answer;

    /**
     * Displays the pop-up dialog box which prevents user from clicking main window.
     * Returns true if user clicked "yes" and false if user clicked "no".
     * @param title
     * @param message
     * @return
     */
    public static boolean displayConfirmDialogBox(String title, String message){
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);

        //Add message and yes and no buttons
        Label label = new Label();
        label.setText(message);
        Button noButton = new Button("No");
        Button yesButton = new Button("Yes");

        //set up action handlers for each button
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _answer = false;
                window.close();
            }
        });
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _answer = true;
                window.close();
            }
        });

        VBox layout = new VBox();
        HBox buttons = new HBox(40);
        buttons.getChildren().addAll(yesButton, noButton);
        layout.getChildren().addAll(label, buttons);
        buttons.setPadding(new Insets(10,5,5,5));
        layout.setPadding(new Insets(10,5,10,5));
        buttons.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return _answer;
    }

    /**
     * Displays an error message. Prevents main gui window from being clicked until this error message is closed.
     * @param title
     * @param message
     */
    public static void errorDialogBox(String title, String message){
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);

        //Add message
        Label label = new Label();
        label.setText(message);
        Button okButton = new Button("OK");

        //ok button assigned to window close
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        //set up layouts
        VBox layout = new VBox();
        HBox buttons = new HBox(40);
        buttons.getChildren().add(okButton);
        layout.getChildren().addAll(label, buttons);
        buttons.setPadding(new Insets(10,5,5,5));
        layout.setPadding(new Insets(10,5,10,5));
        buttons.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return ;
    }

}