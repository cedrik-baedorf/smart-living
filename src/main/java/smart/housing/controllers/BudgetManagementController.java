package smart.housing.controllers;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.*;
import smart.housing.exceptions.BudgetManagementServiceException;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.BudgetManagementServiceImplementation;
import smart.housing.services.UserManagementService;
import smart.housing.ui.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

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

    private final UserManagementService USER_SERVICE;

    @FXML public BackgroundStackPane budgetBackgroundPane;
    @FXML public StyledTextField productNameField;
    @FXML public StyledTextField costField;
    @FXML public StyledComboBox<User> creditors;
    @FXML public StyledCheckComboBox<User> debtors;
    @FXML public StyledButton addExpenseButton;
    @FXML public StyledTableView<Expense> expenseTable;
    @FXML public StyledTableView<DebtOverview> debtsOverview;
    @FXML public StyledButton deleteButton;
    @FXML public StyledButton modifyButton;
    @FXML public StyledButton emailButton;
    @FXML public StyledButton settleDebtButton;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param application Application calling the constructor
     */
    public BudgetManagementController(SmartLivingApplication application, UserManagementService userManagementService) {
        this.APPLICATION = application;
        this.USER_SERVICE = userManagementService;
        this.BUDGET_SERVICE = new BudgetManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        setBackgroundImage();

        // Set up listener for selection in debtsOverview
        debtsOverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                emailButton.setVisible(true);
                settleDebtButton.setVisible(true);  // Settle Debt button is always visible when a row is selected
            } else {
                emailButton.setVisible(true);
                settleDebtButton.setVisible(true);  // Settle Debt button is not visible when no row is selected
            }
        });

        // Bind the selection model of the debtsOverview table to a property
        SimpleObjectProperty<DebtOverview> selectedDebtProperty = new SimpleObjectProperty<>();
        selectedDebtProperty.bind(debtsOverview.getSelectionModel().selectedItemProperty());

        // Bind the visibility and clickability of the emailButton based on the debt amount
        emailButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedDebtProperty.get() != null && selectedDebtProperty.get().getAmount() < 0,
                selectedDebtProperty));

        // Assuming you have a TableColumn for "Amount" like this:
        TableColumn<DebtOverview, Double> amountColumn = (TableColumn<DebtOverview, Double>) debtsOverview.getColumns().get(0);

        // Add a cell factory to format the double value as a string with the desired color
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");  // Clear style for empty cells
                } else {
                    setText(String.format("%.2f €", item)); // Include the currency symbol
                    setStyle(item < 0 ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
                }
            }
        });

        // Also, update the cell value factory for the "Amount" column
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        update();
    }

    private void setBackgroundImage() {
        budgetBackgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    @Override
    public void update() {
        creditors.setItems(FXCollections.observableList(USER_SERVICE.getUsers()));
        debtors.setItems(FXCollections.observableList(USER_SERVICE.getUsers()));
        expenseTable.setItems(FXCollections.observableList(BUDGET_SERVICE.getAllExpenses()));
        loadExpenseList();
        loadDebtsOverviewList();
    }

    public void _addExpenseButton_onAction(ActionEvent event) {
        event.consume();
        addExpenseButtonClicked();
    }

    private void addExpenseButtonClicked() {
        try {
            String product = productNameField.getText();
            String costInput = costField.getText().replace(',', '.');
            User selectedCreditor = creditors.getValue();
            Set<User> selectedDebtors = new HashSet<>(debtors.getCheckModel().getCheckedItems());

            BUDGET_SERVICE.validateAndCreateExpense(product, costInput, selectedCreditor, selectedDebtors);
            buttonDisplay(true, addExpenseButton); // Turn button green on success
        } catch (BudgetManagementServiceException e) {
            // Handle validation errors
            System.err.println(e.getMessage());
            buttonDisplay(false, addExpenseButton); // Turn button red on failure
        } catch (Exception e) {
            // Handle other errors
            e.printStackTrace();
            buttonDisplay(false, addExpenseButton); // Turn button red on failure
        } finally {
            clearExpenses();
            loadExpenseList();
            loadDebtsOverviewList();
        }
    }

    public void loadExpenseList() {
        try {
            List<Expense> expenses = BUDGET_SERVICE.getAllExpenses();
            // Sort the list in descending order based on creation date
            expenses.sort(Comparator.comparing(Expense::getExpenseId).reversed());
            expenseTable.setItems(FXCollections.observableList(expenses));
        } catch (Exception e) {
            System.err.println("Error loading expense list: " + e.getMessage());
        }
    }

    private void loadDebtsOverviewList() {
        User activeUser = USER_SERVICE.getServiceUser();
        List<DebtOverview> debtOverviews = BUDGET_SERVICE.getUserDebt(activeUser);
        debtsOverview.setItems(FXCollections.observableList(debtOverviews));
    }

    private void clearExpenses () {
        productNameField.clear();
        costField.clear();
        creditors.getSelectionModel().clearSelection();
        debtors.getCheckModel().clearChecks();
    }

    public void _deleteButton_onAction(ActionEvent event) {
        event.consume();
        Expense selectedItem = expenseTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            removeItemFromList(selectedItem);
            buttonDisplay(true,deleteButton);
        } else {
            buttonDisplay(false,deleteButton);
        }
    }

    private void removeItemFromList(Expense expense) {
        new ConfirmDialog(
            "Delete selected expense?", "Delete", "Keep"
        ).showAndWait().ifPresent(aBoolean -> {
            if(aBoolean)
                BUDGET_SERVICE.delete(expense);
        });
        loadExpenseList();
        loadDebtsOverviewList();
    }

    public void _modifyButton_onAction(ActionEvent event) {
        System.out.println("ModifyButton clicked!");
        event.consume();
        Expense selectedItem = expenseTable.getSelectionModel().getSelectedItem();

        if(selectedItem == null) {
            buttonDisplay(false,modifyButton);
        } else {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setDialogPane(APPLICATION.loadFXML(
                    ModifyExpenseDialogController.VIEW_NAME,
                    new ModifyExpenseDialogController(USER_SERVICE, BUDGET_SERVICE, dialog, selectedItem)
            ));
            dialog.showAndWait().ifPresent(aBoolean -> {
                if (aBoolean) {
                    // If modification was successful, update both tables
                    loadExpenseList();
                    loadDebtsOverviewList();
                }});
            buttonDisplay(true,modifyButton);
        }
    }

    public static void buttonDisplay(Boolean successful, StyledButton button) {
        String borderColor = successful ? "green" : "red";
        button.setStyle("-fx-border-color: " + borderColor + ";");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(g -> button.setStyle(""));
        pause.play();
    }

    public void _emailButton_onAction(ActionEvent event) {
        event.consume();
        sendEmail();
    }

    public void _settleDebtButton_onAction(ActionEvent event){
        event.consume();
        settleDebtButtonClicked();
    }

    private void settleDebtButtonClicked() {
        DebtOverview selectedDebt = debtsOverview.getSelectionModel().getSelectedItem();

        if (selectedDebt != null) {
            User contrary = selectedDebt.getCreditor();
            List<Expense> expenseList = BUDGET_SERVICE.getAllExpenses()
                .stream()
                .filter(expense ->
                    (expense.getCreditor().equals(contrary) && expense.getDebtors().contains(USER_SERVICE.getServiceUser())) ||
                    (expense.getCreditor().equals(USER_SERVICE.getServiceUser()) && expense.getDebtors().contains(contrary))
                ).toList();
            for(Expense expense : expenseList)
                BUDGET_SERVICE.delete(expense);
            update();
        }
    }

    private void sendEmail() {
        // Get the selected row from the debtsOverview table
        DebtOverview selectedDebt = debtsOverview.getSelectionModel().getSelectedItem();

        if (selectedDebt != null) {
            try {
                BUDGET_SERVICE.sendReminderEmail(USER_SERVICE.getServiceUser(), selectedDebt);
                buttonDisplay(true, emailButton);
            } catch (IOException | URISyntaxException e) {
                buttonDisplay(false, emailButton);
            }
        } else {
            buttonDisplay(false, emailButton);
        }
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}