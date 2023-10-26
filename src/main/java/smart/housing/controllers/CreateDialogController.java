package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import smart.housing.SmartLivingApplication;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;
import smart.housing.entities.User;

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
        try {
            createUser();
        } catch (EmptyFieldException exception) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText(exception.getMessage());
        } finally {
            usernameField.clear();
            passwordField.clear();
            lastNameField.clear();
            firstNameField.clear();
        }
    }

    public void createUser() {
        checkForEmptyInput(usernameField.getText(), "username");
        checkForEmptyInput(passwordField.getText(), "password");
        checkForEmptyInput(lastNameField.getText(), "surname");
        checkForEmptyInput(firstNameField.getText(), "first name");

        LoginService loginService = new LoginServiceImplementation(APPLICATION.getDatabaseConnector());

        User newUser = new User(usernameField.getText(), passwordField.getText(), loginService.getHashAlgorithm());
        newUser.setLastName(lastNameField.getText());
        newUser.setFirstName(firstNameField.getText());

        loginService.create(newUser);
        DIALOG.setResult(true);
    }

}