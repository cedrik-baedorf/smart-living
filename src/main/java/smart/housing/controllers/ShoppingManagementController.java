package smart.housing.controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import smart.housing.entities.*;
import smart.housing.services.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smart.housing.SmartLivingApplication;
import smart.housing.ui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ShoppingManagementController extends SmartHousingController {

    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "shopping_management.fxml";

    /**
     * Name of the background image file
     */
    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/shopping_management_background.jpg";

    private final SmartLivingApplication APPLICATION;

    @FXML public BackgroundStackPane backgroundPane;
    @FXML
    private StyledTextField artikelTextField;

    @FXML
    private StyledButton hinzufuegenButton;

    @FXML
    private StyledButton loeschenButton;

    @FXML
    private StyledComboBox<String> einheitComboBox;

    @FXML
    private StyledButton aenderungButton;

    @FXML
    private StyledTableView<ShoppingListItem> tableView;

    @FXML
    private StyledTextField anzahlField;

    private ShoppingManagementServiceImplementation service;

    public ShoppingManagementController(SmartLivingApplication application) {
        this.APPLICATION = application;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    public void initialize() {
        setBackgroundImage();
        System.out.println("Initialisieren");
        ObservableList<String> itemsList = FXCollections.observableArrayList(
                "g",
                "kg",
                "l",
                "ml",
                " "
        );
        einheitComboBox.setItems(itemsList);

        clearFields();
        loadShoppingList();
    }

    private void setBackgroundImage() {
        backgroundPane.setBackgroundImage(BACKGROUND_IMAGE);
    }

    private void loadShoppingList () {
        try {
            EntityManager entityManager = APPLICATION.getDatabaseConnector().createEntityManager();
            TypedQuery<ShoppingListItem> itemQuery = entityManager.createNamedQuery(ShoppingListItem.FIND_ALL, ShoppingListItem.class);
            List<ShoppingListItem> itemList = itemQuery.getResultList();

            tableView.setItems(FXCollections.observableList(itemList));
        } catch (Exception e) {System.out.println("Catched Exception");}


        TableColumn<ShoppingListItem,String> itemCol = new TableColumn<ShoppingListItem,String>("Artikel");
        itemCol.setCellValueFactory(new PropertyValueFactory("item"));
        itemCol.setMinWidth(50);
        TableColumn<ShoppingListItem,Double> quantityCol = new TableColumn<ShoppingListItem,Double>("Anzahl");
        quantityCol.setCellValueFactory(new PropertyValueFactory("anzahl"));
        TableColumn<ShoppingListItem,String> unitCol = new TableColumn<ShoppingListItem,String>("Einheit");
        unitCol.setCellValueFactory(new PropertyValueFactory("einheit"));

        tableView.getColumns().setAll(itemCol, quantityCol, unitCol);

    }

    private void clearFields () {
        artikelTextField.clear();
        anzahlField.clear();
        einheitComboBox.getSelectionModel().clearSelection();
    }

    private void hinzufuegenButtonClicked() {
        try {
            String artikel = artikelTextField.getText();
            double anzahl = Double.parseDouble(anzahlField.getText());
            String einheit = einheitComboBox.getValue();

        if (artikel != null && !artikel.isEmpty() && anzahl != 0.0 && !einheit.isEmpty()
                && einheit != null && !einheit.isEmpty()) {

            service = new ShoppingManagementServiceImplementation(APPLICATION.getDatabaseConnector());
            service.create(new ShoppingListItem(artikel,anzahl,einheit));

            clearFields();
            loadShoppingList();

        } else {
            System.out.println("Bitte füllen Sie alle Felder aus.");
        }
        } catch (Exception e) {
            hinzufuegenButton.setStyle("-fx-border-color: red;");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(g -> hinzufuegenButton.setStyle("-fx-border-color: grey;"));
            pause.play();
        };
    }

    private void removeItemFromList(ShoppingListItem shoppingListItem) {
        service = new ShoppingManagementServiceImplementation(APPLICATION.getDatabaseConnector());
        service.delete(shoppingListItem);
        loadShoppingList();
    }

    public void _hinzufügenButton_onAction(ActionEvent event) {
        event.consume();
        hinzufuegenButtonClicked();
    }

    public void _deleteButton_onAction(ActionEvent event) {
        event.consume();
        ShoppingListItem selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            removeItemFromList(selectedItem);
            loeschenButton.setStyle("-fx-border-color: green;");
        } else {
            loeschenButton.setStyle("-fx-border-color: red;");
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> loeschenButton.setStyle("-fx-border-color: grey;"));
        pause.play();
    }
}
