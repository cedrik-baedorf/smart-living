package smart.housing.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.DialogPane;
import smart.housing.SmartLivingApplication;
import smart.housing.entities.ShoppingListItem;
import smart.housing.services.ShoppingManagementService;
import smart.housing.services.ShoppingManagementServiceImplementation;
import smart.housing.ui.*;

public class ModifyShoppingManagementDialogController extends DialogController{
    /**
     * Name of the corresponding <code>.fxml</code> file
     */
    public static final String VIEW_NAME = "shopping_management_modify_item_dialog.fxml";

    private final SmartLivingApplication APPLICATION;

    private final Dialog<Boolean> DIALOG;

    @FXML DialogPane dialogPane;
    @FXML Label itemField;
    @FXML Label unitField;
    @FXML StyledTextField amountTextField;
    @FXML ErrorMessage errorMessage;
    @FXML StyledButton modifyButton;

    private final ShoppingManagementService service;

    private ShoppingListItem selectedItem;

    public ModifyShoppingManagementDialogController(SmartLivingApplication application, Dialog<Boolean> dialog, ShoppingListItem item) {
        this.APPLICATION = application;
        this.DIALOG = dialog;
        this.service = new ShoppingManagementServiceImplementation(APPLICATION.getDatabaseConnector());
        this.selectedItem = item;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * this method is automatically called at loading time
     */
    public void initialize() {
        super.setOnCloseRequest(DIALOG);

        itemField.setText("New Amount for Item: "+ selectedItem.getItem());
        unitField.setText(selectedItem.getUnit());
    }

    public void modifyItem() {
        try {
            if (amountTextField.getText().isEmpty()) {
                ShoppingManagementController.buttonDisplay(false, modifyButton);
            } else {
                service.modifyItem(selectedItem, Double.parseDouble(amountTextField.getText()));
                ShoppingManagementController.buttonDisplay(true, modifyButton);
                DIALOG.setResult(true);
            }
        } catch (Exception e) {ShoppingManagementController.buttonDisplay(false, modifyButton);}
    }
}
