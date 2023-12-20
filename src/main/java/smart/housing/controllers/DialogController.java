package smart.housing.controllers;

import javafx.scene.control.Dialog;
import smart.housing.exceptions.EmptyFieldException;

import java.util.Collection;

public abstract class DialogController extends SmartHousingController {

    private static final String MSG_EMPTY_FIELD = "%s must not be empty";

    protected <T> void setOnCloseRequest(Dialog<T> dialog) {
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            dialog.setResult(null);
            dialog.hide();
        });
    }

    private void checkForEmptyInputString(String input, String fieldName) throws EmptyFieldException {
        if(input == null || input.length() == 0)
            throw new EmptyFieldException(String.format(MSG_EMPTY_FIELD, fieldName), fieldName);
    }

    private void checkForEmptyInputNumber(Number input, String fieldName) throws EmptyFieldException {
        if(input == null || input.doubleValue() == 0.0)
            throw new EmptyFieldException(String.format(MSG_EMPTY_FIELD, fieldName), fieldName);
    }

    private <T> void checkForEmptyInputCollection(Collection<T> input, String fieldName) throws EmptyFieldException {
        if(input == null || input.isEmpty())
            throw new EmptyFieldException(String.format(MSG_EMPTY_FIELD, fieldName), fieldName);
    }

    /**
     * This method is used to check for missing input values. If present, this method
     * throws an {@link EmptyFieldException}.
     * Empty values are:
     *  - default values for primitive data types
     *  - empty string for type String
     *  - null for other objects
     * @param input field value
     * @param fieldName name of the empty field
     * @throws EmptyFieldException contains error message on the field in question
     */
    protected void checkForEmptyInput(Object input, String fieldName) throws EmptyFieldException {
        if(input == null)
            throw new EmptyFieldException(String.format(MSG_EMPTY_FIELD, fieldName), fieldName);
        else if(input.getClass().equals(String.class))
            checkForEmptyInputString((String) input, fieldName);
        else if(input.getClass().equals(Double.class))
            checkForEmptyInputNumber((Number) input, fieldName);
        else if(input instanceof Collection<?>)
            checkForEmptyInputCollection((Collection<?>) input, fieldName);
    }

}