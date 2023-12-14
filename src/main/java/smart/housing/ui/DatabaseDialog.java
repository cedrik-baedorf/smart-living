package smart.housing.ui;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import smart.housing.SmartLivingApplication;
import smart.housing.controllers.DatabaseDialogController;
import smart.housing.database.DatabaseConnector;

import java.util.Optional;

public class DatabaseDialog extends Dialog<DatabaseConnector> {

    public DatabaseDialog(SmartLivingApplication application) {
        this.setTitle("Configure Database Connection");
        this.setDialogPane(application.loadFXML(DatabaseDialogController.VIEW_NAME, new DatabaseDialogController(this)));
        ((Stage)this.getDialogPane().getScene().getWindow()).getIcons().add(new Image("smart/housing/ui/images/icon_number.png"));
    }

}