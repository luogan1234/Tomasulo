package tomasulo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    private TomasuloCore core;

    @Override
    public void start(Stage primaryStage) {
        try {
            core = new TomasuloCore();

            Button btn = new Button();
            btn.setText(core.bProperty().toString());
            core.bProperty().addListener(new ChangeListener<Object>(){
                @Override
                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                    btn.setText(core.bProperty().toString());
                }
            });

            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    core.action();
                }
            });

            StackPane root = new StackPane();
            root.getChildren().add(btn);
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}