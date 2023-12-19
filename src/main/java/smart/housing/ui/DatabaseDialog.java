package smart.housing.ui;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import smart.housing.SmartLivingApplication;
import smart.housing.controllers.DatabaseDialogController;
import smart.housing.database.DatabaseConnector;

public class DatabaseDialog extends StyledDialog<DatabaseConnector> {

    public DatabaseDialog(SmartLivingApplication application) {
        this.setTitle("Configure Database Connection");
        this.getDialogPane().setContent(application.loadFXML(DatabaseDialogController.VIEW_NAME, new DatabaseDialogController(this)));
        ((Stage)this.getDialogPane().getScene().getWindow()).getIcons().setAll(
                new Image("smart/housing/ui/images/icon_number.png")
        );
        this.getDialogPane().setPrefSize(300, 300);
    }

}