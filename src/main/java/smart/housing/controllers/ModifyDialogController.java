package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.services.UserManagementService;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledComboBox;
import smart.housing.ui.StyledPasswordField;
import smart.housing.ui.StyledTextField;
import java.util.Arrays;

/**
 * Controller to view 'modify_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class ModifyDialogController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "modify_dialog.fxml";

    private final UserManagementService SERVICE;

    private final Dialog<Boolean> DIALOG;

    private final User USER_TO_BE_MODIFIED;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField lastNameField, firstNameField, emailField;
    @FXML StyledComboBox<UserRole> roleComboBox;
    @FXML StyledPasswordField newPasswordField, confirmPasswordField, currentPasswordField;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public ModifyDialogController(UserManagementService service, Dialog<Boolean> dialog, User userToBeModified) {
        this.SERVICE = service;
        this.DIALOG = dialog;
        this.USER_TO_BE_MODIFIED = userToBeModified;
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        errorMessage.clear();
        loadUserData();
        initializeKeyMappings();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _modifyUser(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        try {
            modifyUser();
        } catch (EmptyFieldException | UserManagementServiceException exception) {
            errorMessage.displayError(exception.getMessage(), 5);
            loadUserData();
        }
    }

    private void loadUserData() {
        firstNameField.setText(USER_TO_BE_MODIFIED.getFirstName());
        lastNameField.setText(USER_TO_BE_MODIFIED.getLastName());
        roleComboBox.setItems(FXCollections.observableList(Arrays.stream(UserRole.values())
            .filter(userRole -> SERVICE.getServiceUser().getRole().outranks(userRole))
            .toList()
        ));
        roleComboBox.setValue(USER_TO_BE_MODIFIED.getRole());
        emailField.setText(USER_TO_BE_MODIFIED.getEmail());
        confirmPasswordField.clear();
        newPasswordField.clear();
        currentPasswordField.clear();
    }

    public void initializeKeyMappings() {
        firstNameField.switchFocusOnKeyPressed(KeyCode.DOWN, lastNameField);
        lastNameField.switchFocusOnKeyPressed(KeyCode.UP, firstNameField);

        lastNameField.switchFocusOnKeyPressed(KeyCode.DOWN, roleComboBox);

        emailField.switchFocusOnKeyPressed(KeyCode.UP, roleComboBox);

        emailField.switchFocusOnKeyPressed(KeyCode.DOWN, newPasswordField);
        newPasswordField.switchFocusOnKeyPressed(KeyCode.UP, emailField);

        newPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, confirmPasswordField);
        confirmPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, newPasswordField);

        confirmPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, currentPasswordField);
        currentPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, confirmPasswordField);
    }

    public void modifyUser() {
        checkForEmptyInput(firstNameField.getText(), "first name");
        checkForEmptyInput(lastNameField.getText(), "surname");
        checkForEmptyInput(emailField.getText(), "email");

        if(! newPasswordField.getText().equals(confirmPasswordField.getText()))
            throw new IncorrectCredentialsException("new passwords do not match");

        User updatedUser = new User(USER_TO_BE_MODIFIED.getUsername());
        updatedUser.setFirstName(firstNameField.getText());
        updatedUser.setLastName(lastNameField.getText());
        updatedUser.setRole(roleComboBox.getValue());
        updatedUser.setEmail(emailField.getText());
        if(newPasswordField.getText().length() != 0)
            updatedUser.setPassword(newPasswordField.getText(), SERVICE.getHashAlgorithm());

        SERVICE.modify(USER_TO_BE_MODIFIED.getUsername(), currentPasswordField.getText(), updatedUser);

        DIALOG.setResult(true);
    }

}