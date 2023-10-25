package smart.housing.controllers;

import smart.housing.SmartLivingApplication;

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

    private SmartLivingApplication application;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public TaskManagementController(SmartLivingApplication application) {
        this.application = application;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }
}

