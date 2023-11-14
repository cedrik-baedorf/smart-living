package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Task;
import smart.housing.entities.User;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.services.TaskManagementService;
import smart.housing.services.TaskManagementServiceImplementation;
import smart.housing.services.UserManagementService;
import smart.housing.services.UserManagementServiceImplementation;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledComboBox;
import smart.housing.ui.StyledTextField;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller to view 'new_task_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class NewTaskDialogController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "new_task_dialog.fxml";

    private final SmartLivingApplication APPLICATION;

    private final Dialog<Boolean> DIALOG;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField taskNameField;
    @FXML DatePicker duedatePicker;
    @FXML StyledComboBox<User> assigneeComboBox;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public NewTaskDialogController(SmartLivingApplication application, Dialog<Boolean> dialog) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        loadAssignees();
        errorMessage.clear();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void loadAssignees() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(APPLICATION.getDatabaseConnector());
        List<User> userList = userManagementService.getUsers();
        assigneeComboBox.setItems(FXCollections.observableList(userList));
    }

    public void _createTask(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        try {
            createTask();
            taskNameField.clear();
            duedatePicker.setValue(null);
            assigneeComboBox.getSelectionModel().clearSelection();
        } catch (EmptyFieldException exception) {
            errorMessage.displayError(exception.getMessage(), 5);
        } catch (Exception e) {
            errorMessage.displayError("Error creating task. Please try again.");
        } finally {
            taskNameField.clear();
        }
    }

    public void createTask() {
        checkForEmptyInput(taskNameField.getText(), "taskName");

        Task newTask = new Task(taskNameField.getText());
        LocalDate dueDate = duedatePicker.getValue();
        newTask.setDueDate(dueDate);

        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(APPLICATION.getDatabaseConnector());

        taskManagementService.create(newTask);
        DIALOG.setResult(true);
    }

}