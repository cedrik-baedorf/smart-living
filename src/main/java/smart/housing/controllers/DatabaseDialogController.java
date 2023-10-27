package smart.housing.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.hibernate.service.spi.ServiceException;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;

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
    DialogPane dialogPane;
    @FXML
    TextField usernameField, jdbcDriverField, urlField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public DatabaseDialogController(Dialog<DatabaseConnector> dialog) {
        this.DIALOG = dialog;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        clearErrorMessage();
    }

    private void clearErrorMessage() {
        errorMessage.setTextFill(Color.BLACK);
        errorMessage.setText("");
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _confirmDatabaseConfiguration(ActionEvent event) {
        event.consume();
        Map<String, String> dbProperties = new HashMap<>();
        dbProperties.put(DatabaseConnector.USER_PROPERTY, usernameField.getText());
        dbProperties.put(DatabaseConnector.PASSWORD_PROPERTY, passwordField.getText());
        dbProperties.put(DatabaseConnector.DRIVER_PROPERTY, jdbcDriverField.getText());
        dbProperties.put(DatabaseConnector.URL_PROPERTY, urlField.getText());
        try {
            DIALOG.setResult(new DatabaseConnectorImplementation(dbProperties));
            DIALOG.close();
        } catch (ServiceException serviceException) {
            errorMessage.setText("Unable to connect to database");
            errorMessage.setTextFill(Color.RED);
        } finally {
            usernameField.clear();
            passwordField.clear();
            jdbcDriverField.clear();
            urlField.clear();
        }
    }



}