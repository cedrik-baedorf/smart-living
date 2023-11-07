package smart.housing.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Scene scene = new Scene(createContentPane(), 600, 400);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Test Application");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StackPane createContentPane() {
        StackPane pane = new StackPane();

        StyledButton button = new StyledButton("My Text");

        pane.getChildren().add(button);

        return pane;
    }

}