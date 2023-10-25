package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * Controller to view 'delete_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class DeleteDialogController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "delete_dialog.fxml";

    /**
     * Prepared <code>String</code> messages
     */
    private static final String
        DELETE_USER = "Confirm deletion of user %s",
        ENTER_PASSWORD = "Please enter a password";

    private final Dialog<String> DIALOG;

    private final String username;

    @FXML
    DialogPane dialogPane;
    @FXML
    Label usernameLabel, errorMessage;
    @FXML
    PasswordField passwordField;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public DeleteDialogController(Dialog<String> dialog, String username) {
        this.DIALOG = dialog;
        this.username = username;
    }

    public void initialize() {
        usernameLabel.setText(String.format(DELETE_USER, username));
        clearErrorMessage();
    }

    private void clearErrorMessage() {
        errorMessage.setTextFill(Color.BLACK);
        errorMessage.setText("");
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _confirmDeletion(ActionEvent event) {
        event.consume();
        clearErrorMessage();
        String password = passwordField.getText();
        if(password != null && password.length() > 0)
            DIALOG.setResult(passwordField.getText());
        else {
            errorMessage.setText(ENTER_PASSWORD);
            errorMessage.setTextFill(Color.RED);
            return;
        }
        usernameLabel.setText("");
        passwordField.clear();
    }



}