package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;
import smart.housing.SmartLivingApplication;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.database.LoginManager;
import smart.housing.database.LoginManagerImplementation;

import javax.persistence.EntityManager;
import java.util.Optional;

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

    private SmartLivingApplication application;

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the contructor
     */
    public LoginPageController(SmartLivingApplication application) {
        this.application = application;
    }

    public void initialize() {
        try {
            application.setDatabaseConnector(new DatabaseConnectorImplementation());
        } catch (ServiceException | PropertyNotFoundException exception) {
            configureDatabaseProperties();
        }
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

    public void _loginButton_onAction(ActionEvent event) {
        event.consume();
        attemptLogin(usernameField.getText(), passwordField.getText());
    }

    private void attemptLogin(String username, String password) {
        DatabaseConnector connector = application.getDatabaseConnector();
        LoginManager loginManager = new LoginManagerImplementation(connector);
        EntityManager entityManager = loginManager.login(username, password);
        passwordField.clear();
        usernameField.clear();
        if (entityManager != null) {
            application.setDatabaseConnector(connector);
            application.setRoot(HomePageController.VIEW_NAME, new HomePageController(application));
        }
    }

    public void _confDBase_onAction(ActionEvent actionEvent) {
        configureDatabaseProperties();
    }

    private void configureDatabaseProperties() {
        Dialog<DatabaseConnector> dialog = new Dialog<>();
        dialog.setDialogPane(application.loadFXML(DatabaseDialogController.VIEW_NAME, new DatabaseDialogController(dialog)));

        dialog.showAndWait().ifPresent(databaseConnector -> application.setDatabaseConnector(databaseConnector));
    }
}

