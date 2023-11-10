package smart.housing.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import smart.housing.SmartLivingApplication;
import smart.housing.ui.StyledTabPane;

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

    private final SmartLivingApplication APPLICATION;

    private SmartHousingController
        userManagementController, taskManagementController,
        budgetManagementController, ShoppingListController;

    @FXML
    public StyledTabPane tabPane;
    @FXML
    public Pane userManagement;
    @FXML
    public Pane taskManagement;
    @FXML
    public Pane budgetManagement;
    @FXML
    public Pane shoppingManagement;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public HomePageController(SmartLivingApplication application) {
        this.APPLICATION = application;
    }

    public void initialize() {
        loadUserManagement();
        loadTaskManagement();
        loadBudgetManagement();
        loadShoppingManagement();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void loadUserManagement() {
        userManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        userManagementController = new UserManagementController(APPLICATION);
        loadSubPane(userManagement, APPLICATION.loadFXML(UserManagementController.VIEW_NAME, userManagementController));
    }

    public void loadTaskManagement() {
        taskManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        taskManagementController = new TaskManagementController(APPLICATION);
        loadSubPane(taskManagement, APPLICATION.loadFXML(TaskManagementController.VIEW_NAME, taskManagementController));
    }

    public void loadBudgetManagement() {
        budgetManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        budgetManagementController = new BudgetManagementController(APPLICATION);
        loadSubPane(budgetManagement, APPLICATION.loadFXML(BudgetManagementController.VIEW_NAME, budgetManagementController));
    }

    public void loadShoppingManagement() {
        shoppingManagement.setPrefSize(tabPane.getPrefWidth(), tabPane.getPrefHeight());
        ShoppingListController  = new ShoppingManagementController(APPLICATION);
        loadSubPane(shoppingManagement, APPLICATION.loadFXML(ShoppingListController.getViewName(), ShoppingListController));

    }

    private void loadSubPane(Pane parent, Pane child) {
        child.setPrefSize(parent.getPrefWidth(), parent.getPrefHeight());
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
        parent.getChildren().add(child);
    }
    public void _updateTabs(Event event) {
        Object source = event.getSource();
        if(source.getClass().equals(Tab.class)) {
            Tab sourceTab = (Tab) source;
            if(sourceTab.getContent().equals(userManagement) && userManagementController != null)
                userManagementController.update();
            if(sourceTab.getContent().equals(taskManagement) && taskManagementController != null)
                taskManagementController.update();
            if(sourceTab.getContent().equals(budgetManagement) && budgetManagementController != null)
                budgetManagementController.update();
            if(sourceTab.getContent().equals(shoppingManagement) && ShoppingListController != null)
                ShoppingListController.update();
        }
        event.consume();
    }

}

