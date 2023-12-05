package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import smart.housing.entities.Task;
import smart.housing.entities.User;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.services.TaskManagementService;
import smart.housing.services.UserManagementService;
import smart.housing.ui.ErrorMessage;
import smart.housing.ui.StyledCheckComboBox;
import smart.housing.ui.StyledDatePicker;
import smart.housing.ui.StyledTextField;

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

    private final UserManagementService USER_MANAGEMENT_SERVICE;

    private final TaskManagementService TASK_MANAGEMENT_SERVICE;

    private final Dialog<Boolean> DIALOG;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField taskNameField;
    @FXML StyledDatePicker dueDatePicker;
    @FXML StyledCheckComboBox<User> assigneeComboBox;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public NewTaskDialogController(
        UserManagementService userManagementService,
        TaskManagementService taskManagementService,
        Dialog<Boolean> dialog
    ) {
        this.USER_MANAGEMENT_SERVICE = userManagementService;
        this.TASK_MANAGEMENT_SERVICE = taskManagementService;
        this.DIALOG = dialog;
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        loadAssignees();
        errorMessage.clear();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void loadAssignees() {
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();
        assigneeComboBox.setItems(FXCollections.observableList(userList));
    }

    public void _createTask(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        try {
            createTask();
            taskNameField.clear();
            dueDatePicker.setValue(null);
            assigneeComboBox.getCheckModel().clearChecks();
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
        newTask.setDueDate(dueDatePicker.getValue());
        assigneeComboBox.getCheckModel().getCheckedItems().forEach(newTask::addAssignee);

        TASK_MANAGEMENT_SERVICE.create(newTask);
        DIALOG.setResult(true);
    }

}