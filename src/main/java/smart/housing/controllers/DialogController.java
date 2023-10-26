package smart.housing.controllers;

import javafx.scene.control.Dialog;
import smart.housing.exceptions.EmptyFieldException;

public abstract class DialogController extends SmartHousingController {

    private static final String MSG_EMPTY_FIELD = "%s must not be empty";

    protected <T> void setOnCloseRequest(Dialog<T> dialog) {
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            dialog.setResult(null);
            dialog.hide();
        });
    }

    protected void checkForEmptyInput(String input, String fieldName) throws EmptyFieldException {
        if(input == null || input.length() == 0)
            throw new EmptyFieldException(String.format(MSG_EMPTY_FIELD, fieldName), fieldName);
    }

}