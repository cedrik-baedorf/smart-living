package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.hibernate.dialect.Database;
import smart.housing.SmartLivingApplication;
import smart.housing.database.DatabaseConnector;
import smart.housing.database.LoginManager;
import smart.housing.database.LoginManagerImplementation;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Controller to view 'delete_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class DeleteDialogController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "delete_dialog.fxml";

    private SmartLivingApplication application;

    private User userAtDeletion;

    @FXML
    DialogPane dialogPane;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public DeleteDialogController(SmartLivingApplication application, User userAtDeletion) {
        this.application = application;
        this.userAtDeletion = userAtDeletion;
    }

    public void initialize() {

    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _confirmDeletion(ActionEvent actionEvent) {
        if(usernameField.getText().equals(userAtDeletion.getUsername())) {
            LoginManager loginManager = new LoginManagerImplementation(application.getDatabaseConnector());
            loginManager.delete(usernameField.getText(), passwordField.getText());
        }
        usernameField.clear();
        passwordField.clear();
    }



}