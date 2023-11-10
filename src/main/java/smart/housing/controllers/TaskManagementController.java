package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Task;
import smart.housing.services.TaskManagementService;
import smart.housing.services.TaskManagementServiceImplementation;
import smart.housing.ui.BackgroundStackPane;
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

    private final TaskManagementService TASK_SERVICE;

    @FXML public BackgroundStackPane backgroundPane;
    @FXML
    public StyledTableView<Task> taskTable;
    @FXML
    public StyledTableView<Task> currentTasks;
    @FXML
    public StyledTableView<Task> overdueTasks;

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
        setBackgroundImage();
        loadTasks();
    }

    private void setBackgroundImage() {
        backgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    public void loadTasks(){
        taskTable.setItems(FXCollections.observableList(TASK_SERVICE.getAllTasks()));
        currentTasks.setItems(FXCollections.observableList(TASK_SERVICE.getCurrentTasks()));
        overdueTasks.setItems(FXCollections.observableList(TASK_SERVICE.getIncompleteTasks()));
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}

