package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import smart.housing.SmartLivingApplication;
import smart.housing.database.LoginManager;
import smart.housing.database.LoginManagerImplementation;
import smart.housing.entities.User;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;

/**
 * Controller to view 'create_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class CreateDialogController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "create_dialog.fxml";

    private final SmartLivingApplication APPLICATION;

    private final Dialog<Boolean> DIALOG;

    @FXML
    DialogPane dialogPane;
    @FXML
    TextField usernameField, lastNameField, firstNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public CreateDialogController(SmartLivingApplication application, Dialog<Boolean> dialog) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        clearErrorMessage();
    }

    private void clearErrorMessage() {
        errorMessage.setTextFill(Color.BLACK);
        errorMessage.setText("");
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _createUser(ActionEvent event) {
        event.consume();
        clearErrorMessage();
        User newUser = new User(usernameField.getText());
        newUser.setPassword(passwordField.getText(), HashAlgorithm.DEFAULT);
        newUser.setLastName(lastNameField.getText());
        newUser.setFirstName(firstNameField.getText());
        LoginManager loginManager = new LoginManagerImplementation(APPLICATION.getDatabaseConnector());
        loginManager.create(newUser);
        DIALOG.setResult(true);
        usernameField.clear();
        passwordField.clear();
        lastNameField.clear();
        firstNameField.clear();
    }



}