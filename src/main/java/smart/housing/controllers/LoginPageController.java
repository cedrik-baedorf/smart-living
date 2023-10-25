package smart.housing.controllers;

import javafx.application.Platform;
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
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;

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

    private final SmartLivingApplication APPLICATION;

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
        this.APPLICATION = application;
    }

    public void initialize() {
        try {
            APPLICATION.setDatabaseConnector(new DatabaseConnectorImplementation());
        } catch (ServiceException | PropertyNotFoundException exception) {
            DatabaseConnector connector = createDatabaseConnector();
            if(connector != null)
                APPLICATION.setDatabaseConnector(connector);
            else
                Platform.exit();
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
        DatabaseConnector connector = APPLICATION.getDatabaseConnector();
        LoginService loginService = new LoginServiceImplementation(connector);
        EntityManager entityManager = loginService.login(username, password);
        passwordField.clear();
        usernameField.clear();
        if (entityManager != null) {
            APPLICATION.setDatabaseConnector(connector);
            APPLICATION.setRoot(HomePageController.VIEW_NAME, new HomePageController(APPLICATION));
        }
    }

    public void _confDBase_onAction(ActionEvent event) {
        event.consume();
        DatabaseConnector connector = createDatabaseConnector();
        if(connector != null)
            APPLICATION.setDatabaseConnector(connector);
    }

    private DatabaseConnector createDatabaseConnector() {
        Dialog<DatabaseConnector> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(DatabaseDialogController.VIEW_NAME, new DatabaseDialogController(dialog)));

        Optional<DatabaseConnector> result = dialog.showAndWait();
        return result.orElse(null);
    }
}

