package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import smart.housing.MyApp;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.database.LoginManager;
import smart.housing.database.LoginManagerImplementation;

import javax.persistence.EntityManager;

/**
 * Controller to view 'login_page.fxml'
 * @author I551381
 * @version 1.0
 */
public class LoginPageController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "login_page.fxml";

    private MyApp application;

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;

    /**
     * Default constructor for this controller
     */
    public LoginPageController() {
        this(null);
    }

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the contructor
     */
    public LoginPageController(MyApp application) {
        this.application = application;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _onPasswordFieldAction(ActionEvent event) {
        event.consume();
        attemptLogin(usernameField.getText(), passwordField.getText());
    }

    public void _onUsernameFieldAction(ActionEvent event) {
        event.consume();
        attemptLogin(usernameField.getText(), passwordField.getText());
    }

    private void attemptLogin(String username, String password) {
        DatabaseConnector connector = new DatabaseConnectorImplementation();
        LoginManager loginManager = new LoginManagerImplementation(connector);
        EntityManager entityManager = loginManager.login(username, password);
        passwordField.clear();
        usernameField.clear();
        if(entityManager != null)
            application.setRoot(HomePageController.VIEW_NAME, new HomePageController(application));
    }

}

