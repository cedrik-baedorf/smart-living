package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import smart.housing.entities.Task;
import smart.housing.entities.User;
import smart.housing.services.TaskManagementService;
import smart.housing.services.UserManagementService;
import smart.housing.ui.*;

import java.util.List;

/**
 * Controller to view 'modify_task_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class ModifyTaskController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "modify_task_dialog.fxml";

    private final UserManagementService USER_MANAGEMENT_SERVICE;

    private final TaskManagementService TASK_MANAGEMENT_SERVICE;

    private final Dialog<Boolean> DIALOG;

    private final Task TASK_TO_BE_MODIFIED;

    @FXML DialogPane dialogPane;
    @FXML StyledTextField taskNameField;
    @FXML DatePicker dueDatePicker;
    @FXML StyledCheckComboBox<User> assigneeComboBox;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public ModifyTaskController(
            UserManagementService userManagementService,
            TaskManagementService taskManagementService,
            Dialog<Boolean> dialog,
            Task taskToBeModified) {
        this.USER_MANAGEMENT_SERVICE = userManagementService;
        this.TASK_MANAGEMENT_SERVICE = taskManagementService;
        this.DIALOG = dialog;
        this.TASK_TO_BE_MODIFIED = taskToBeModified;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        loadTasks();
        loadAssignees();
        errorMessage.clear();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _modifyTask(ActionEvent event) {
        event.consume();
        errorMessage.clear();
        modifyTask();
        loadTasks();
        loadAssignees();
    }

    private void loadTasks(){
        taskNameField.setText(TASK_TO_BE_MODIFIED.getTaskName());
        dueDatePicker.setValue(TASK_TO_BE_MODIFIED.getDueDate());
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();
        assigneeComboBox.setItems(FXCollections.observableList(userList));
        TASK_TO_BE_MODIFIED.getAssignees().forEach(
            assignee -> assigneeComboBox.getCheckModel().check(assignee)
        );
    }

    private void loadAssignees() {
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();
        assigneeComboBox.setItems(FXCollections.observableList(userList));
    }

    public void modifyTask() {
        checkForEmptyInput(taskNameField.getText(), "taskName");

        Task updatetask = new Task(TASK_TO_BE_MODIFIED.getTaskName());
        updatetask.setTaskName(taskNameField.getText());
        updatetask.setDueDate(dueDatePicker.getValue());
        assigneeComboBox.getCheckModel().getCheckedItems().forEach(updatetask::addAssignee);

        TASK_MANAGEMENT_SERVICE.modify(TASK_TO_BE_MODIFIED, updatetask);

        DIALOG.setResult(true);
    }

}