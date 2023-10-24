package smart.housing.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import smart.housing.SmartLivingApplication;

/**
 * Controller to view 'home_page.fxml'
 * @author I551381
 * @version 1.0
 */
public class HomePageController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "home_page.fxml";

    private SmartLivingApplication application;

    @FXML
    public TabPane tabPane;

    @FXML
    public Pane userManagement;

    @FXML
    public Pane taskManagement;

    @FXML
    public Pane accounting;

    @FXML
    public Pane shopping;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public HomePageController(SmartLivingApplication application) {
        this.application = application;
    }

    public void initialize() {
        loadUserManagement();
        loadTaskManagement();
        loadAccounting();
        loadShopping();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void loadUserManagement() {
        userManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        loadSubPane(userManagement, application.loadFXML(UserManagementController.VIEW_NAME, new UserManagementController(application)));
    }

    public void loadTaskManagement() {
        taskManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        loadSubPane(taskManagement, application.loadFXML(TaskManagementController.VIEW_NAME, new TaskManagementController(application)));
    }

    public void loadAccounting() {
        accounting.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());

    }

    public void loadShopping() {
        shopping.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());

    }

    private void loadSubPane(Pane parent, Pane child) {
        child.setPrefSize(parent.getPrefWidth(), parent.getPrefHeight());
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
        parent.getChildren().add(child);
    }

}

