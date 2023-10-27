package smart.housing.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;
import smart.housing.SmartLivingApplication;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;
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
    StackPane mainPane;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    @FXML
    public Label errorMessage;
    @FXML
    public Image backgroundImage;

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
        try {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image("smart/housing/views/images/login_background.jpg"),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            Background background = new Background(backgroundImage);
            mainPane.setBackground(background);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        clearErrorMessage();
    }

    private void clearErrorMessage() {
        errorMessage.setTextFill(Color.BLACK);
        errorMessage.setText("");
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
        clearErrorMessage();
        errorMessage.setText("Attempting login...");

        DatabaseConnector connector = APPLICATION.getDatabaseConnector();
        LoginService loginService = new LoginServiceImplementation(connector);
        try {
            APPLICATION.setUser(loginService.login(username, password));
            APPLICATION.setDatabaseConnector(connector);
            APPLICATION.setRoot(HomePageController.VIEW_NAME, new HomePageController(APPLICATION));
        } catch (IncorrectCredentialsException exception) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Invalid Credentials");
        } catch (LoginServiceException exception) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Missing Credentials");
        } finally {
            passwordField.clear();
            usernameField.clear();
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

