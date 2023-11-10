package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.services.UserManagementService;
import smart.housing.services.UserManagementServiceImplementation;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledComboBox;
import smart.housing.ui.StyledPasswordField;
import smart.housing.ui.StyledTextField;

import javax.persistence.EntityManager;
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

    private final SmartLivingApplication APPLICATION;

    private final Dialog<Boolean> DIALOG;

    private final User USER_TO_BE_MODIFIED;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField lastNameField, firstNameField;
    @FXML StyledComboBox<UserRole> roleComboBox;
    @FXML StyledPasswordField newPasswordField, confirmPasswordField, currentPasswordField;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public ModifyDialogController(SmartLivingApplication application, Dialog<Boolean> dialog, User userToBeModified) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
        this.USER_TO_BE_MODIFIED = userToBeModified;
    }

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
            .filter(userRole -> APPLICATION.getUser().getRole().outranks(userRole))
            .toList()
        ));
        roleComboBox.setValue(USER_TO_BE_MODIFIED.getRole());
        confirmPasswordField.clear();
        newPasswordField.clear();
        currentPasswordField.clear();
    }

    public void initializeKeyMappings() {
        firstNameField.switchFocusOnKeyPressed(KeyCode.DOWN, lastNameField);
        lastNameField.switchFocusOnKeyPressed(KeyCode.UP, firstNameField);

        lastNameField.switchFocusOnKeyPressed(KeyCode.DOWN, roleComboBox);

        newPasswordField.switchFocusOnKeyPressed(KeyCode.UP, roleComboBox);

        newPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, confirmPasswordField);
        confirmPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, newPasswordField);

        confirmPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, currentPasswordField);
        currentPasswordField.switchFocusOnKeyPressed(KeyCode.DOWN, confirmPasswordField);
    }

    public void modifyUser() {
        checkForEmptyInput(firstNameField.getText(), "first name");
        checkForEmptyInput(lastNameField.getText(), "surname");

        if(! newPasswordField.getText().equals(confirmPasswordField.getText()))
            throw new IncorrectCredentialsException("new passwords do not match");

        UserManagementService userManagementService = new UserManagementServiceImplementation(APPLICATION.getDatabaseConnector());

        User user = userManagementService.login(USER_TO_BE_MODIFIED.getUsername(), currentPasswordField.getText());

        EntityManager entityManager = APPLICATION.getDatabaseConnector().createEntityManager();

        entityManager.getTransaction().begin();
        user = entityManager.merge(user);
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setRole(roleComboBox.getValue());
        if(newPasswordField.getText().length() != 0)
            user.setPassword(newPasswordField.getText(), userManagementService.getHashAlgorithm());
        entityManager.getTransaction().commit();
        entityManager.close();
        DIALOG.setResult(true);
    }

}