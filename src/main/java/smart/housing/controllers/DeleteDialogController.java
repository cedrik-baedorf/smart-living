package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import smart.housing.SmartLivingApplication;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;
import smart.housing.entities.User;

/**
 * Controller to view 'delete_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class DeleteDialogController extends DialogController {

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

    private final SmartLivingApplication APPLICATION;
    private final Dialog<Boolean> DIALOG;

    private final User USER;

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
    public DeleteDialogController(SmartLivingApplication application, Dialog<Boolean> dialog, User user) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
        this.USER = user;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        usernameLabel.setText(String.format(DELETE_USER, USER.getUsername()));
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
        LoginService loginService = new LoginServiceImplementation(APPLICATION.getDatabaseConnector());
        loginService.delete(USER.getUsername(), passwordField.getText());
        DIALOG.setResult(true);
        usernameLabel.setText("");
        passwordField.clear();
    }



}