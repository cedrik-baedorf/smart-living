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
    private static final String BACKGROUND_IMAGE = "smart/housing/ui/images/shopping_management–background2.jpg";

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

    @FXML
    private StyledButton modifyButton;


    private final ShoppingManagementService service;

    public ShoppingManagementController(SmartLivingApplication application) {

        this.APPLICATION = application;
        this.service = new ShoppingManagementServiceImplementation(APPLICATION.getDatabaseConnector());
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    public void initialize() {
        setBackgroundImage();
        System.out.println("Initialisieren");
        ObservableList<String> itemsList = FXCollections.observableArrayList(
                "kg",
                "l",
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
            tableView.setItems(FXCollections.observableList(service.getItemList()));
        } catch (Exception e) {System.out.println("Catched Exception");}
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

            service.create(new ShoppingListItem(artikel,anzahl,einheit));

            clearFields();
            loadShoppingList();
            buttonDisplay(true,hinzufuegenButton);

        } else {
            System.out.println("Bitte füllen Sie alle Felder aus.");
        }
        } catch (Exception e) {
            buttonDisplay(false,hinzufuegenButton);
        };
    }

    private void removeItemFromList(ShoppingListItem shoppingListItem) {
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
            buttonDisplay(true,loeschenButton);
        } else {
            buttonDisplay(false,loeschenButton);
        }
    }

    public void _modifyButton_onAction() {
        if(tableView.getSelectionModel().getSelectedItem() == null) {
            buttonDisplay(false,modifyButton);
        } else {
            ShoppingListItem selectedItem = tableView.getSelectionModel().getSelectedItem();
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setDialogPane(APPLICATION.loadFXML(
                    ModifyShoppingManagementDialogController.VIEW_NAME,
                    new ModifyShoppingManagementDialogController(APPLICATION, dialog, selectedItem)
            ));
            dialog.showAndWait().ifPresent(aBoolean -> loadShoppingList());
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
}
