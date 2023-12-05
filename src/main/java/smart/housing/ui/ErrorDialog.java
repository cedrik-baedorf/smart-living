package smart.housing.ui;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ErrorDialog extends Dialog<Void> {

    public ErrorDialog(String errorMessage) {
        this.setTitle("Error");

        ((Stage)this.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image("smart/housing/ui/images/icon_error.png")
        );

        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            this.setResult(null);
            this.hide();
        });

        BorderPane background = new BorderPane();

        ErrorMessage errorMesssage = new ErrorMessage();
        errorMesssage.setText(errorMessage);

        background.setCenter(errorMesssage);

        this.getDialogPane().setContent(background);
    }

}