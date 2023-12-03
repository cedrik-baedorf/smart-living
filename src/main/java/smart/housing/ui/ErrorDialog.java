package smart.housing.ui;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

        Text errorMesssage = new Text(errorMessage);
        errorMesssage.setWrappingWidth(200);
        errorMesssage.setTextAlignment(TextAlignment.CENTER);

        background.setCenter(errorMesssage);

        this.getDialogPane().setContent(background);
    }

}