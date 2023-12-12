package smart.housing.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.UserManagementService;
import smart.housing.ui.*;

import java.util.List;

/**
 * Controller to view 'modify_dialog.fxml'
 * @author I551381
 * @version 1.0
 */
public class ModifyExpenseDialogController extends DialogController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "modify_expense_dialog.fxml";

    private final UserManagementService USER_MANAGEMENT_SERVICE;

    private final BudgetManagementService BUDGET_MANAGEMENT_SERVICE;

    private final Dialog<Boolean> DIALOG;

    private final Expense EXPENSE_TO_BE_MODIFIED;

    @FXML DialogPane dialogPane;
    @FXML public StyledTextField productNameField;
    @FXML public StyledTextField costField;
    @FXML public StyledComboBox<User> creditor;
    @FXML public StyledCheckComboBox<User> debtors;
    @FXML ErrorMessage errorMessage;

    /**
     * Constructor for this controller passing the <code>Application</code> object this
     * instance belongs to
     * @param dialog Dialog to this <code>DialogPane</code>
     */
    public ModifyExpenseDialogController(
            UserManagementService userManagementService,
            BudgetManagementService budgetManagementService,
            Dialog<Boolean> dialog,
            Expense expenseToBeModified) {
        this.USER_MANAGEMENT_SERVICE = userManagementService;
        this.BUDGET_MANAGEMENT_SERVICE = budgetManagementService;
        this.DIALOG = dialog;
        this.EXPENSE_TO_BE_MODIFIED = expenseToBeModified;
    }

    public void initialize() {
        super.setOnCloseRequest(DIALOG);
        loadCreditors();
        loadDebtors();
        loadExpense();
        errorMessage.clear();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void _modifyExpense(ActionEvent event) {
        System.out.println("_modifyExpense method");
        event.consume();
        errorMessage.clear();
        loadDebtors();
        modifyExpense();
        loadExpense();
        loadCreditors();
    }

    /*
    private void loadExpense(){
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();

        productNameField.initializeStyleSheets(); // Ensure the field is initialized
        productNameField.setText(EXPENSE_TO_BE_MODIFIED.getProduct());

        creditor.setItems(FXCollections.observableList(userList));
        costField.setText(Double.toString(EXPENSE_TO_BE_MODIFIED.getCost()));
        debtors.setItems(FXCollections.observableList(userList));
    }
*/

    private void loadExpense(){
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();

        productNameField.setText(EXPENSE_TO_BE_MODIFIED.getProduct());
        creditor.setItems(FXCollections.observableList(userList));
        costField.setText(Double.toString(EXPENSE_TO_BE_MODIFIED.getCost()));
        debtors.setItems(FXCollections.observableList(userList));
    }


    private void loadCreditors(){
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();
        creditor.setItems(FXCollections.observableList(userList));
    }

    private void loadDebtors() {
        List<User> userList = USER_MANAGEMENT_SERVICE.getUsers();
        debtors.setItems(FXCollections.observableList(userList));
    }

    public void modifyExpense() {
        System.out.println("modifyExpense method");
        checkForEmptyInput(productNameField.getText(), "taskName");

        Expense updateExpense = new Expense(EXPENSE_TO_BE_MODIFIED.getDebitors(),EXPENSE_TO_BE_MODIFIED.getCreditor(),EXPENSE_TO_BE_MODIFIED.getProduct(),EXPENSE_TO_BE_MODIFIED.getCost());
        updateExpense.setProduct(productNameField.getText());
        updateExpense.setCreditor(creditor.getValue());
        updateExpense.setCost(Double.parseDouble(costField.getText()));
        debtors.getCheckModel().getCheckedItems().forEach(updateExpense::addDebitor);

        BUDGET_MANAGEMENT_SERVICE.modify(EXPENSE_TO_BE_MODIFIED, updateExpense);

        DIALOG.setResult(true);
    }

}