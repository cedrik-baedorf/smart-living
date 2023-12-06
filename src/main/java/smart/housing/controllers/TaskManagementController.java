package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Task;
import smart.housing.services.TaskManagementService;
import smart.housing.services.TaskManagementServiceImplementation;
import smart.housing.services.UserManagementService;
import smart.housing.ui.BackgroundStackPane;
import smart.housing.ui.ConfirmDialog;
import smart.housing.ui.StyledButton;
import smart.housing.ui.StyledTableView;


/**
 * Controller to view 'task_management.fxml'
 * @author Anna Gossner
 * @version 1.0
 */
public class TaskManagementController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "task_management.fxml";

    /**
     * Name of the background image file
     */
    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/task_management_background.jpg";

    private final SmartLivingApplication APPLICATION;

    private final UserManagementService USER_SERVICE;

    private final TaskManagementService TASK_SERVICE;

    @FXML public BackgroundStackPane backgroundPane;
    @FXML public StyledButton newTaskButton;
    @FXML public StyledButton modifyTaskButton;
    @FXML public StyledButton deleteTaskButton;
    @FXML public StyledTableView<Task> taskTable;
    @FXML public StyledTableView<Task> currentTasks;
    @FXML public StyledTableView<Task> overdueTasks;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public TaskManagementController(SmartLivingApplication application, UserManagementService userManagementService) {
        this.APPLICATION = application;
        this.USER_SERVICE = userManagementService;
        this.TASK_SERVICE = new TaskManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        setBackgroundImage();
        loadTasks();
        initializeButtons(false);
    }

    private void setBackgroundImage() {
        backgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    public void loadTasks(){
        taskTable.setItems(FXCollections.observableList(TASK_SERVICE.getAllTasks()));
        currentTasks.setItems(FXCollections.observableList(TASK_SERVICE.getCurrentTasks()));
        overdueTasks.setItems(FXCollections.observableList(TASK_SERVICE.getIncompleteTasks()));
    }

    private void initializeButtons(boolean itemSelected) {
        newTaskButton.setDisable(false);
        modifyTaskButton.setDisable(! itemSelected);
    }

    public void _newTaskButton_onAction(ActionEvent event) {
        event.consume();
        createTask();
    }

    public void createTask() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(
                NewTaskDialogController.VIEW_NAME,
                new NewTaskDialogController(USER_SERVICE, TASK_SERVICE, dialog)
        ));
        dialog.showAndWait().ifPresent(aBoolean -> loadTasks());
    }

    public void _taskTable_onMouseClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        initializeButtons(selectedTask != null);
    }

    public void _modifyTaskButton_onAction(ActionEvent event){
        event.consume();
        modifyTask();
    }

    public void modifyTask(){
        Task taskToBeModified = taskTable.getSelectionModel().getSelectedItem();
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setDialogPane(APPLICATION.loadFXML(
                ModifyTaskController.VIEW_NAME,
                new ModifyTaskController(USER_SERVICE, TASK_SERVICE, dialog, taskToBeModified)
        ));
        dialog.showAndWait().ifPresent(aBoolean -> loadTasks());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    public void _deleteButton_onAction(ActionEvent event) {
        event.consume();
        deleteSelectedTask();
    }

    private void deleteSelectedTask(){
        Task taskToBeDeleted = taskTable.getSelectionModel().getSelectedItem();
        ConfirmDialog dialog = new ConfirmDialog("Do you want to delete this task?", "Yes, delete", "No, keep");
        dialog.showAndWait().ifPresent(aBoolean -> {
            if(aBoolean) {
                TASK_SERVICE.delete(taskToBeDeleted);
                // loadTasks();
                taskTable.getItems().remove(taskToBeDeleted);
            }
        });
    }
}

