package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.User;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.BudgetManagementServiceImplementation;

/**
 * Controller to view 'budget_management.fxml'
 * @author I551381
 * @version 1.0
 */
public class BudgetManagementController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "budget_management.fxml";

    private final SmartLivingApplication APPLICATION;

    @FXML
    ChoiceBox<User> creditors;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public BudgetManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
    }

    public void initialize() {
        update();
    }

    @Override
    public void update() {
        BudgetManagementService service = new BudgetManagementServiceImplementation(APPLICATION.getDatabaseConnector());
        creditors.setItems(FXCollections.observableList(service.getCurrentUsers()));
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}

