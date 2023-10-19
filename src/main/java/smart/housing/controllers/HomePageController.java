package smart.housing.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import smart.housing.MyApp;

import java.io.IOException;

/**
 * Controller to view 'login_page.fxml'
 * @author I551381
 * @version 1.0
 */
public class HomePageController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "home_page.fxml";

    private MyApp application;

    @FXML
    public Pane userManagement;

    @FXML
    public Pane tasks;

    @FXML
    public Pane accounting;

    @FXML
    public Pane shopping;

    /**
     * Default constructor for this controller
     */
    public HomePageController() {
        this(null);
    }

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public HomePageController(MyApp application) {
        this.application = application;
    }

    public void initialize() {
        loadUserManagement();
        loadTasks();
        loadAccounting();
        loadShopping();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void loadUserManagement() {
        loadSubPane(userManagement, application.loadFXML("user_management.fxml"));
    }

    public void loadTasks() {

    }

    public void loadAccounting() {

    }

    public void loadShopping() {

    }

    private void loadSubPane(Pane parent, Pane child) {
        child.setPrefSize(parent.getPrefWidth(), parent.getPrefHeight());
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
        parent.getChildren().add(child);
    }

}

