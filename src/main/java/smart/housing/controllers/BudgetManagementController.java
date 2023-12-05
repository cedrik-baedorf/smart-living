package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.exceptions.BudgetManagementServiceException;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.BudgetManagementServiceImplementation;
import smart.housing.ui.*;
import javafx.collections.ListChangeListener;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @FXML public BackgroundStackPane budgetBackgroundPane;
    @FXML public StyledTextField productNameField;
    @FXML public StyledTextField costField;
    @FXML public StyledComboBox<User> creditors;
    @FXML public StyledCheckComboBox<User> debitors;
    @FXML public StyledButton addExpenseButton;
    @FXML public StyledTableView<Expense> expenseTable;



    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public BudgetManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
        this.BUDGET_SERVICE = new BudgetManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        setBackgroundImage();
        update();
        // Set up listener for multiple selections
        debitors.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super User>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println("Selected Debitors: " + change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    System.out.println("Deselected Debitors: " + change.getRemoved());
                }
            }
        });
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

    public void _addExpenseButton_onAction(ActionEvent event) {
        event.consume();
        addExpenseButtonClicked();
    }

    private void addExpenseButtonClicked() {
        try {
            String product = productNameField.getText();
            double cost = Double.parseDouble(costField.getText());
            User selectedCreditor = creditors.getValue();
            Set<User> selectedDebitors = debitors.getCheckModel().getCheckedItems().stream().collect(Collectors.toSet());
            if(product == null)
                throw new BudgetManagementServiceException("Please enter a product");
            if(selectedDebitors == null || selectedDebitors.isEmpty())
                throw new BudgetManagementServiceException("Please select at least one debitor");
            if(cost <= 0)
                throw new BudgetManagementServiceException("Please provide a positive amount");

            BUDGET_SERVICE.create(new Expense(selectedDebitors, selectedCreditor, product, cost));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearExpenses();
            loadExpenseList();
        }
    }

    public void loadExpenseList() {
        try {
            List<Expense> expenses = BUDGET_SERVICE.getAllExpenses();
            expenseTable.setItems(FXCollections.observableList(expenses));
        } catch (Exception e) {
            System.err.println("Error loading expense list: " + e.getMessage());
        }
    }


    private void clearExpenses () {
        productNameField.clear();
        costField.clear();
        creditors.getSelectionModel().clearSelection();
        debitors.getCheckModel().clearChecks();
    }


    public String getViewName() {
        return VIEW_NAME;
    }
}