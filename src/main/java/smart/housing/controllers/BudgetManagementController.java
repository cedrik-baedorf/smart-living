package smart.housing.controllers;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.*;
import smart.housing.exceptions.BudgetManagementServiceException;
import smart.housing.services.BudgetManagementService;
import smart.housing.services.BudgetManagementServiceImplementation;
import smart.housing.services.UserManagementService;
import smart.housing.ui.*;
import javafx.collections.ListChangeListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
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

    private final UserManagementService USER_SERVICE;

    @FXML
    public BackgroundStackPane budgetBackgroundPane;

    @FXML
    public StyledTextField productNameField;

    @FXML
    public StyledTextField costField;

    @FXML
    public StyledComboBox<User> creditors;

    @FXML
    public StyledCheckComboBox<User> debitors;

    @FXML
    public StyledButton addExpenseButton;

    @FXML
    public StyledTableView<Expense> expenseTable;

    @FXML
    public StyledTableView<DebtOverview> debtsOverview;

    @FXML
    public StyledButton deleteButton;

    @FXML
    public StyledButton emailButton;



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

    public void initialize() {
        setBackgroundImage();

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

        // Set up listener for selection in debtsOverview
        debtsOverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                emailButton.setVisible(debtsOverview.getItems().size() > 1);
            } else {
                emailButton.setVisible(false);
            }
        });

        // Bind the selection model of the debtsOverview table to a property
        SimpleObjectProperty<DebtOverview> selectedDebtProperty = new SimpleObjectProperty<>();
        selectedDebtProperty.bind(debtsOverview.getSelectionModel().selectedItemProperty());

        // Bind the visibility of the emailButton to the condition you want
        emailButton.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> selectedDebtProperty.get() != null && debtsOverview.getItems().size() > 1,
                selectedDebtProperty, debtsOverview.getItems()));

        update();
    }


    private void setBackgroundImage() {
        budgetBackgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    @Override
    public void update() {
        creditors.setItems(FXCollections.observableList(USER_SERVICE.getUsers()));
        debitors.setItems(FXCollections.observableList(USER_SERVICE.getUsers()));
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
            double cost = Double.parseDouble(costField.getText());
            User selectedCreditor = creditors.getValue();
            Set<User> selectedDebitors = new HashSet<>(debitors.getCheckModel().getCheckedItems());
            if(product == null)
                throw new BudgetManagementServiceException("Please enter a product");
            if(selectedDebitors.isEmpty())
                throw new BudgetManagementServiceException("Please select at least one debitor");
            if(cost <= 0)
                throw new BudgetManagementServiceException("Please provide a positive amount");

            BUDGET_SERVICE.create(new Expense(selectedDebitors, selectedCreditor, product, cost));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearExpenses();
            loadExpenseList();
            loadDebtsOverviewList();
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


    private void loadDebtsOverviewList() {
        User activeUser = USER_SERVICE.getServiceUser();
        List<DebtOverview> debtOverviews = BUDGET_SERVICE.getUserDebt(activeUser);
        debtsOverview.setItems(FXCollections.observableList(debtOverviews));
    }



    private void clearExpenses () {
        productNameField.clear();
        costField.clear();
        creditors.getSelectionModel().clearSelection();
        debitors.getCheckModel().clearChecks();
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
        BUDGET_SERVICE.delete(expense);
        loadExpenseList();
        loadDebtsOverviewList();
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

    private void sendEmail() {
        // Get the selected row from the debtsOverview table
        DebtOverview selectedDebt = debtsOverview.getSelectionModel().getSelectedItem();

        if (selectedDebt != null) {
            try {
                String debtorFirstName = selectedDebt.debtor().getFirstName();
                String debtorLastName = selectedDebt.debtor().getLastName();
                String creditorFirstName = selectedDebt.creditor().getFirstName();
                String debtorEmail = debtorFirstName + "." + debtorLastName + "@studmail.hwg-lu.de";
                String subject = "You owe money!";

                // Encode the body parameter
                String body = "Dear " + debtorFirstName + " " + debtorLastName +
                        ",\n\nYou owe money to " + creditorFirstName +
                        ". Please settle the amount at your earliest convenience.\n\nSincerely,\nYour Budget Management System";

                // Encode the subject and body parameters
                String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8).replace("+", "%20");
                String encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8).replace("+", "%20");

                // Create a mailto URI with properly encoded subject and body
                URI mailtoUri = new URI("mailto:" + debtorEmail + "?subject=" + encodedSubject + "&body=" + encodedBody);

                // Open the default email client
                Desktop desktop = Desktop.getDesktop();
                desktop.mail(mailtoUri);

                // Display success message
                buttonDisplay(true, emailButton);
            } catch (IOException | URISyntaxException e) {
                // Display error message
                buttonDisplay(false, emailButton);
                e.printStackTrace();
            }
        } else {
            // No row selected, display error message
            buttonDisplay(false, emailButton);
        }
    }







    public String getViewName() {
        return VIEW_NAME;
    }
}

