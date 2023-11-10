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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ShoppingListController extends SmartHousingController {

    public static final String VIEW_NAME = "shopping_list.fxml";

    private final SmartLivingApplication APPLICATION;

    @FXML
    private TextField artikelTextField;

    @FXML
    private Button hinzufuegenButton;

    @FXML
    private Button loeschenButton;

    @FXML
    private ComboBox<String> einheitComboBox;

    @FXML
    private Button aenderungButton;

    @FXML
    private TableView<ShoppingListItem> tableView;

    @FXML
    private TextField anzahlField;

    private ShoppingListServiceImplementation service;

    public ShoppingListController(SmartLivingApplication application) {
        this.APPLICATION = application;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    public void initialize() {
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

            service = new ShoppingListServiceImplementation(APPLICATION.getDatabaseConnector());
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
        service = new ShoppingListServiceImplementation(APPLICATION.getDatabaseConnector());
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
