package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Task;
import smart.housing.entities.User;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.BudgetManagementServiceImplementation;
import smart.housing.ui.BackgroundStackPane;
import smart.housing.ui.StyledComboBox;
import smart.housing.ui.StyledTableView;

import java.util.List;

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

    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/budget_book_background.jpg";

    private final SmartLivingApplication APPLICATION;

    private final BudgetManagementService BUDGET_SERVICE;

    @FXML
    public BackgroundStackPane budgetBackgroundPane;

    @FXML
    public StyledComboBox<User> creditors;

    @FXML
    public StyledComboBox<User> debitors;

    @FXML
    public StyledTableView<Task> expenseTable;



    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public BudgetManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
        this.BUDGET_SERVICE = new BudgetManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    public void initialize() {
        setBackgroundImage();
        update();
    }

    private void setBackgroundImage() {
        budgetBackgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    @Override
    public void update() {
        BudgetManagementService service = new BudgetManagementServiceImplementation(APPLICATION.getDatabaseConnector());
        creditors.setItems(FXCollections.observableList(service.getCurrentUsers()));

        debitors.setItems(FXCollections.observableList(service.getCurrentUsers()));

        expenseTable.setItems(FXCollections.observableList(BUDGET_SERVICE.getAllExpenses()));
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}

