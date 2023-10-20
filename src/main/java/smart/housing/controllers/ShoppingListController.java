package smart.housing.controllers;

import javafx.collections.ObservableList;
import smart.housing.entities.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smart.housing.SmartLivingApplication;

public class ShoppingListController extends SmartHousingController {

    public static final String VIEW_NAME = "shopping_list.fxml";

    private SmartLivingApplication application;

    @FXML
    private TextField artikelTextField;

    @FXML
    private Button hinzufuegenButton;

    @FXML
    private Button loeschenButton;

    @FXML
    private ComboBox<String> anzahlComboBox;

    @FXML
    private ComboBox<String> einheitComboBox;

    @FXML
    private Button aenderungButton;

    @FXML
    private TableView<ShoppingListItem> tableView;

    public ShoppingListController(SmartLivingApplication application) {
        this.application = application;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    public void initialize () {
        clearFields();
        loadShoppingList();
    }

    private void loadShoppingList () {

        //tableView.setItems(ShoppingListItem.getList());

        TableColumn<ShoppingListItem,String> itemCol = new TableColumn<ShoppingListItem,String>("Artikel");
        itemCol.setCellValueFactory(new PropertyValueFactory("artikel"));
        TableColumn<ShoppingListItem,Double> quantityCol = new TableColumn<ShoppingListItem,Double>("Anzahl");
        quantityCol.setCellValueFactory(new PropertyValueFactory("anzahl"));
        TableColumn<ShoppingListItem,String> unitCol = new TableColumn<ShoppingListItem,String>("Einheit");
        unitCol.setCellValueFactory(new PropertyValueFactory("einheit"));

        //tableView.getColumns().setAll(itemCol, quantityCol, unitCol);
    }

    private void clearFields () {
        artikelTextField.clear();
        anzahlComboBox.getSelectionModel().clearSelection();
        einheitComboBox.getSelectionModel().clearSelection();
    }

    private void hinzufuegenButtonClicked() {
        String artikel = artikelTextField.getText();
        double anzahl = Double.parseDouble(anzahlComboBox.getValue());
        String einheit = einheitComboBox.getValue();

        // Überprüfen, ob alle benötigten Felder ausgefüllt sind
        if (artikel != null && !artikel.isEmpty() && anzahl != 0.0 && !einheit.isEmpty()
                && einheit != null && !einheit.isEmpty()) {
            hinzufuegenButton.setDisable(true);

            // Fügen Sie den neuen Eintrag in die TableView hinzu
            ShoppingListItem item = new ShoppingListItem(artikel,anzahl,einheit);

            // Optional: Löschen Sie die Eingaben nach dem Hinzufügen
            clearFields();

        } else {
            // Zeigen Sie eine Fehlermeldung an oder ergreifen Sie andere Maßnahmen, wenn Felder leer sind
            System.out.println("Bitte füllen Sie alle Felder aus.");
        }
    }


}
