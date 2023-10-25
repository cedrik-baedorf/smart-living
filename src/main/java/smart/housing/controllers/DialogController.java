package smart.housing.controllers;

import javafx.scene.control.Dialog;

public abstract class DialogController extends SmartHousingController {

    protected <T> void setOnCloseRequest(Dialog<T> dialog) {
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            dialog.setResult(null);
            dialog.hide();
        });
    }

}