package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.hibernate.service.spi.ServiceException;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.ui.BackgroundDialogPane;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledPasswordField;
import smart.housing.ui.StyledTextField;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to view 'database_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class DatabaseDialogController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "database_dialog.fxml";

    private final Dialog<DatabaseConnector> DIALOG;

    @FXML
    BackgroundDialogPane dialogPane;
    @FXML
    StyledTextField usernameField, jdbcDriverField, urlField;
    @FXML
    StyledPasswordField passwordField;
    @FXML
    ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public DatabaseDialogController(Dialog<DatabaseConnector> dialog) {
        this.DIALOG = dialog;
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        dialogPane.setBackgroundImage("smart/housing/ui/images/database_dialog_background.jpg");
        errorMessage.clear();
        initializeKeyMappings();
    }

    public void initializeKeyMappings() {
        usernameField.switchFocusOnKeyPressed(KeyCode.DOWN, passwordField);
        passwordField.switchFocusOnKeyPressed(KeyCode.UP, usernameField);
        passwordField.switchFocusOnKeyPressed(KeyCode.DOWN, jdbcDriverField);
        jdbcDriverField.switchFocusOnKeyPressed(KeyCode.UP, passwordField);
        jdbcDriverField.switchFocusOnKeyPressed(KeyCode.DOWN, urlField);
        urlField.switchFocusOnKeyPressed(KeyCode.UP, jdbcDriverField);
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _confirmDatabaseConfiguration(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        Map<String, String> dbProperties = new HashMap<>();
        dbProperties.put(DatabaseConnector.USER_PROPERTY, usernameField.getText());
        dbProperties.put(DatabaseConnector.PASSWORD_PROPERTY, passwordField.getText());
        dbProperties.put(DatabaseConnector.DRIVER_PROPERTY, jdbcDriverField.getText());
        dbProperties.put(DatabaseConnector.URL_PROPERTY, urlField.getText());
        try {
            DIALOG.setResult(new DatabaseConnectorImplementation(dbProperties));
            DIALOG.close();
        } catch (ServiceException serviceException) {
            errorMessage.displayError("Unable to connect to the database", 5);
        } finally {
            usernameField.clear();
            passwordField.clear();
            jdbcDriverField.clear();
            urlField.clear();
        }
    }



}