package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import smart.housing.SmartLivingApplication;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.services.UserManagementService;
import smart.housing.services.UserManagementServiceImplementation;
import smart.housing.entities.User;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledComboBox;
import smart.housing.ui.StyledPasswordField;
import smart.housing.ui.StyledTextField;

import java.util.Arrays;

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

    private final UserManagementService SERVICE;

    private final Dialog<Boolean> DIALOG;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField usernameField, lastNameField, firstNameField;
    @FXML StyledPasswordField passwordField;
    @FXML ErrorMessage errorMessage;
    @FXML StyledComboBox<UserRole> roleComboBox;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public CreateDialogController(UserManagementService service, Dialog<Boolean> dialog) {
        this.SERVICE = service;
        this.DIALOG = dialog;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        loadRoles();
        errorMessage.clear();
        initializeKeyMappings();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * This method loads all roles into the <code>roleComboBox</code> the current user
     * is allowed to assign to a new user
     */
    private void loadRoles() {
        roleComboBox.setItems(FXCollections.observableList(Arrays.stream(UserRole.values())
            .filter(userRole -> SERVICE.getServiceUser().getRole().outranks(userRole))
            .toList()
        ));
        roleComboBox.setValue(UserRole.DEFAULT_ROLE);
    }

    public void initializeKeyMappings() {
        usernameField.switchFocusOnKeyPressed(KeyCode.DOWN, passwordField);
        passwordField.switchFocusOnKeyPressed(KeyCode.UP, usernameField);

        passwordField.switchFocusOnKeyPressed(KeyCode.DOWN, firstNameField);
        firstNameField.switchFocusOnKeyPressed(KeyCode.UP, passwordField);

        firstNameField.switchFocusOnKeyPressed(KeyCode.DOWN, lastNameField);
        lastNameField.switchFocusOnKeyPressed(KeyCode.UP, firstNameField);

        lastNameField.switchFocusOnKeyPressed(KeyCode.DOWN, roleComboBox);
    }

    public void _createUser(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        try {
            createUser();
        } catch (EmptyFieldException exception) {
            errorMessage.displayError(exception.getMessage(), 5);
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

        User newUser = new User(usernameField.getText(), passwordField.getText(), SERVICE.getHashAlgorithm());
        newUser.setLastName(lastNameField.getText());
        newUser.setFirstName(firstNameField.getText());
        newUser.setRole(roleComboBox.getValue());

        SERVICE.create(newUser);
        DIALOG.setResult(true);
    }

}