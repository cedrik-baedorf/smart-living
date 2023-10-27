package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    private final SmartLivingApplication APPLICATION;

    @FXML
    public TableView<User> userTable;

    @FXML
    public Button deleteButton, modifyButton, createButton;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public UserManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
    }

    public void initialize() {
        loadUsers();
        initializeButtons(false);
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void loadUsers() {
        EntityManager entityManager = APPLICATION.getDatabaseConnector().createEntityManager();
        TypedQuery<User> userQuery = entityManager.createNamedQuery(User.FIND_ALL, User.class);
        List<User> userList = userQuery.getResultList();
        userTable.setItems(FXCollections.observableList(userList));
    }

    private void initializeButtons(boolean itemSelected) {
        deleteButton.setDisable(! itemSelected);
        modifyButton.setDisable(! itemSelected);
        createButton.setDisable(false);
    }

    @Override
    public void update() {
        loadUsers();
    }

    public void _userTable_onMouseClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            initializeButtons(true);
        }
    }

    public void _deleteButton_onAction(ActionEvent event) {
        event.consume();
        deleteSelectedUser();
    }

    private void deleteSelectedUser() {
        User userToBeDeleted = userTable.getSelectionModel().getSelectedItem();
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(
                DeleteDialogController.VIEW_NAME,
                new DeleteDialogController(APPLICATION, dialog, userToBeDeleted)
        ));
        dialog.showAndWait().ifPresent(aBoolean -> loadUsers());
    }

    public void _modifyButton_onAction(ActionEvent event) {
        event.consume();
        modifyUser();
    }

    public void modifyUser() {
        User userToBeModified = userTable.getSelectionModel().getSelectedItem();
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(
                ModifyDialogController.VIEW_NAME,
                new ModifyDialogController(APPLICATION, dialog, userToBeModified)
        ));
        dialog.showAndWait().ifPresent(aBoolean -> loadUsers());
    }

    public void _createButton_onAction(ActionEvent event) {
        event.consume();
        createUser();
    }

    public void createUser() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(
                CreateDialogController.VIEW_NAME,
                new CreateDialogController(APPLICATION, dialog)
        ));
        dialog.showAndWait().ifPresent(aBoolean -> loadUsers());
    }

}