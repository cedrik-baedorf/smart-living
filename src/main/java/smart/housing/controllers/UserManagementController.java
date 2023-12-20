package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.services.UserManagementService;
import smart.housing.ui.*;

import java.util.List;

/**
 * Controller to view 'user_management.fxml'
 * @author I551381
 * @version 1.0
 */
public class UserManagementController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "user_management.fxml";

    /**
     * Name of the background image file
     */
    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/user_management_background.jpg";

    private final SmartLivingApplication APPLICATION;

    private final UserManagementService SERVICE;

    @FXML public BackgroundStackPane backgroundPane;
    @FXML public StyledTableView<User> userTable;
    @FXML public Button deleteButton, modifyButton, createButton, logoutButton;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public UserManagementController(SmartLivingApplication application, UserManagementService service) {
        this.APPLICATION = application;
        this.SERVICE = service;
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        setBackgroundImage();
        loadUsers();
        initializeButtons(false);
        bindSizes();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void setBackgroundImage() {
        backgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    private void loadUsers() {
        List<User> userList = SERVICE.getUsers();
        userTable.setItems(FXCollections.observableList(userList));
        userTable.refresh();
    }

    private void initializeButtons(boolean itemSelected) {
        deleteButton.setDisable(! itemSelected);
        modifyButton.setDisable(! itemSelected);
        createButton.setDisable(false);
    }
    private void bindSizes() {
        userTable.bindColumnWidth();
    }

    @Override
    public void update() {
        loadUsers();
    }

    public void _logoutButton_onAction(ActionEvent event) {
        event.consume();
        APPLICATION.setRoot(LoginPageController.VIEW_NAME, new LoginPageController(APPLICATION));
    }

    public void _userTable_onMouseClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        initializeButtons(selectedUser != null && SERVICE.getServiceUser().getRole().outranks(selectedUser.getRole()));
    }

    public void _deleteButton_onAction(ActionEvent event) {
        event.consume();
        deleteSelectedUser();
    }

    private void deleteSelectedUser() {
        User userToBeDeleted = userTable.getSelectionModel().getSelectedItem();
        try {
            new ConfirmPasswordDialog("Confirm password to delete", "Yes, delete", "No, keep",
                SERVICE.getServiceUser(), SERVICE.getDatabaseConnector()
            ).showAndWait().ifPresent(aBoolean -> {
                if(aBoolean) {
                    SERVICE.delete(userToBeDeleted);
                    loadUsers();
                }
            });
        } catch (UserManagementServiceException exception) {
            new ErrorDialog(exception.getMessage()).show();
        }
    }

    public void _modifyButton_onAction(ActionEvent event) {
        event.consume();
        modifySelectedUser();
    }

    private void modifySelectedUser() {
        User userToBeModified = userTable.getSelectionModel().getSelectedItem();
        try {
            new UserDataDialog("Modify User", SERVICE.getServiceUser(), userToBeModified).showAndWait().ifPresent(user ->
                    new ConfirmPasswordDialog("Confirm password to modify", "Yes, commit", "No, undo",
                            SERVICE.getServiceUser(), SERVICE.getDatabaseConnector()
                    ).showAndWait().ifPresent(aBoolean -> {
                        if (aBoolean)
                            SERVICE.modify(userToBeModified, user);
                        loadUsers();
                    })
            );
        } catch (UserManagementServiceException exception) {
            new ErrorDialog(exception.getMessage()).show();
        }
    }

    public void _createButton_onAction(ActionEvent event) {
        event.consume();
        createUser();
    }

    private void createUser() {
        try {
            new UserDataDialog("Create User", SERVICE.getServiceUser()).showAndWait().ifPresent(user -> {
                SERVICE.create(user);
                loadUsers();
            });
        } catch (UserManagementServiceException exception) {
            new ErrorDialog(exception.getMessage()).show();
        }
    }

}