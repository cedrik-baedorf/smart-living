package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Controller to view 'login_page.fxml'
 * @author I551381
 * @version 1.0
 */
public class UserManagementController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "user_management.fxml";

    private SmartLivingApplication application;

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
        this.application = application;
    }

    public void initialize() {
        loadUsers();
        initializeButtons();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void loadUsers() {
        EntityManager entityManager = application.getEntityManager();
        TypedQuery<User> userQuery = entityManager.createNamedQuery(User.FIND_ALL, User.class);
        List<User> userList = userQuery.getResultList();
        userTable.setItems(FXCollections.observableList(userList));
    }

    private void initializeButtons() {
        deleteButton.setDisable(true);
        modifyButton.setDisable(true);
        createButton.setDisable(false);
    }

    public void _userTable_onMouseClicked(MouseEvent mouseEvent) {
        System.out.println("hi");
    }
}

