package smart.housing.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;
import smart.housing.SmartLivingApplication;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.services.UserManagementService;
import smart.housing.services.UserManagementServiceImplementation;
import smart.housing.ui.BackgroundStackPane;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledPasswordField;
import smart.housing.ui.StyledTextField;

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

    /**
     * Name of the background image file
     */
    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/login_page_background.jpg";

    private final SmartLivingApplication APPLICATION;

    @FXML BackgroundStackPane backgroundPane;
    @FXML GridPane gridPane;
    @FXML Label welcomeLabel;
    @FXML StyledPasswordField passwordField;
    @FXML StyledTextField usernameField;
    @FXML ErrorMessage errorMessage;

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
        setBackgroundImage();
        bindGridPaneProperties();
        errorMessage.clear();
        initializeKeyMappings();
    }

    private void setBackgroundImage() {
        backgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    private void bindGridPaneProperties() {
        ColumnConstraints columnConstraints;
        try {
            columnConstraints = gridPane.getColumnConstraints().get(0);
        } catch (RuntimeException exception) {
            return;
        }
        DoubleBinding fontSizeBinding = Bindings.createDoubleBinding(() -> {
            double calculatedWidth = (columnConstraints.getPercentWidth() / 100) * backgroundPane.getWidth();
            double actualWidth = Math.min(Math.max(calculatedWidth, columnConstraints.getMinWidth()), columnConstraints.getMaxWidth());
            return actualWidth * (0.1);
        }, backgroundPane.widthProperty());

        welcomeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), "px;"));
    }

    public void initializeKeyMappings() {
        usernameField.initializeKeyMappings();
        passwordField.initializeKeyMappings();
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
        errorMessage.clear();

        DatabaseConnector connector = APPLICATION.getDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(connector);
        try {
            APPLICATION.setUser(userManagementService.login(username, password));
            APPLICATION.setDatabaseConnector(connector);
            APPLICATION.setRoot(HomePageController.VIEW_NAME, new HomePageController(APPLICATION));
        } catch (IncorrectCredentialsException exception) {
            errorMessage.displayError("Invalid Credentials", 5);
        } catch (UserManagementServiceException exception) {
            errorMessage.displayError("Missing Credentials", 5);
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
        dialog.initOwner(APPLICATION.getPrimaryStage());
        dialog.setDialogPane(APPLICATION.loadFXML(DatabaseDialogController.VIEW_NAME, new DatabaseDialogController(dialog)));

        Optional<DatabaseConnector> result = dialog.showAndWait();
        return result.orElse(null);
    }
}

