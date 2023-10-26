package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;

import javax.persistence.EntityManager;

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

    private User userToBeModified;

    @FXML
    DialogPane dialogPane;
    @FXML
    TextField lastNameField, firstNameField;
    @FXML
    PasswordField newPasswordField, confirmPasswordField, currentPasswordField;
    @FXML
    Label errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public ModifyDialogController(SmartLivingApplication application, Dialog<Boolean> dialog, User userToBeModified) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
        this.userToBeModified = userToBeModified;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        clearErrorMessage();
        loadUserData();
    }

    private void clearErrorMessage() {
        errorMessage.setTextFill(Color.BLACK);
        errorMessage.setText("");
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _modifyUser(ActionEvent event) {
        event.consume();
        clearErrorMessage();
        try {
            modifyUser();
        } catch (EmptyFieldException exception) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText(exception.getMessage());
        } catch (IncorrectCredentialsException exception) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText(exception.getMessage());
        } finally {
            loadUserData();
        }
    }

    private void loadUserData() {
        firstNameField.setText(userToBeModified.getFirstName());
        lastNameField.setText(userToBeModified.getLastName());
        confirmPasswordField.clear();
        newPasswordField.clear();
        currentPasswordField.clear();
    }

    public void modifyUser() {
        checkForEmptyInput(firstNameField.getText(), "first name");
        checkForEmptyInput(lastNameField.getText(), "surname");

        if(! newPasswordField.getText().equals(confirmPasswordField.getText()))
            throw new IncorrectCredentialsException("new passwords do not match");

        LoginService loginService = new LoginServiceImplementation(APPLICATION.getDatabaseConnector());

        User user = loginService.login(userToBeModified.getUsername(), currentPasswordField.getText());

        EntityManager entityManager = APPLICATION.getDatabaseConnector().createEntityManager();

        entityManager.getTransaction().begin();
        user = entityManager.merge(user);
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        if(newPasswordField.getText().length() != 0)
            user.setPassword(newPasswordField.getText(), loginService.getHashAlgorithm());
        entityManager.getTransaction().commit();
        DIALOG.setResult(true);
    }

}