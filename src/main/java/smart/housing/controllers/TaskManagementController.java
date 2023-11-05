package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Task;
import smart.housing.services.TaskManagementService;
import smart.housing.services.TaskManagementServiceImplementation;

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

    private final SmartLivingApplication APPLICATION;

    private final TaskManagementService TASK_SERVICE;

    @FXML
    public TableView<Task> taskTable;

    @FXML
    public TableView<Task> currentTasks;

    @FXML
    public TableView<Task> overdueTasks;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public TaskManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
        this.TASK_SERVICE = new TaskManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    public void initialize() {
        loadTasks();
    }

    public void loadTasks(){
        taskTable.setItems(FXCollections.observableList(TASK_SERVICE.getAllTasks()));
        currentTasks.setItems(FXCollections.observableList(TASK_SERVICE.getCurrentTasks()));
        overdueTasks.setItems(FXCollections.observableList(TASK_SERVICE.getIncomleteTasks()));
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}

