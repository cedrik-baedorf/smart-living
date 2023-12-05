package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.services.UserManagementService;
import smart.housing.entities.User;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledPasswordField;

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
    private static final String DELETE_USER = "Confirm deletion of user %s";

    private final UserManagementService SERVICE;
    private final Dialog<Boolean> DIALOG;

    private final User USER;

    @FXML DialogPane dialogPane;
    @FXML ErrorMessage usernameLabel, errorMessage;
    @FXML StyledPasswordField passwordField;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public DeleteDialogController(UserManagementService service, Dialog<Boolean> dialog, User user) {
        this.SERVICE = service;
        this.DIALOG = dialog;
        this.USER = user;

    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        usernameLabel.displayInfo(String.format(DELETE_USER, USER.getUsername()));
        errorMessage.clear();
        initializeKeyMappings();
    }

    public void initializeKeyMappings() {
        //TODO
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _deleteUser(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        try {
            deleteUser();
        } catch (EmptyFieldException | IncorrectCredentialsException exception) {
            errorMessage.displayError(exception.getMessage(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            usernameLabel.setText("");
            passwordField.clear();
        }
    }

    public void deleteUser() {
        checkForEmptyInput(passwordField.getText(), "password");

        SERVICE.delete(USER.getUsername(), passwordField.getText());
        DIALOG.setResult(true);
    }

}