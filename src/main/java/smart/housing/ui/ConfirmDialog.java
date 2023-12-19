package smart.housing.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class extends {@link Dialog<Boolean>} and creates a simple popup to confirm an action.
 * When the <code>confirmButton</code> is clicked, the result of this dialog will be <code>true</code>.
 * When the <code>abortButton</code> is clicked, the result of this dialog will be <code>false</code>.
 * To set an action on confirm, please use the method <code>setOnConfirm(</code>
 */
public class ConfirmDialog extends StyledDialog<Boolean> {

    private static final int GAP = 10;

    private GridPane infoPane, buttonsPane, errorPane;

    private StyledMessage messageLabel;
    private StyledButton confirmButton, abortButton;

    public ConfirmDialog(String messageText, String confirmText, String abortText) {
        this.setTitle("Confirm Action");
        this.getDialogPane().setPrefSize(300, 100);

        ((Stage)this.getDialogPane().getScene().getWindow()).getIcons().add(
            new Image("smart/housing/ui/images/icon_info.png")
        );

        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            this.setResult(false);
            this.hide();
        });

        initializeObjects(messageText, confirmText, abortText);

        initializeActionListeners();

        this.getDialogPane().setContent(createBackgroundPane());
    }

    private void initializeObjects(String messageText, String confirmText, String abortText) {
        messageLabel = new StyledMessage();
        messageLabel.displayInfo(messageText);
        messageLabel.getStylesheets().add(StyledNode.UI_ELEMENT_STYLE_SHEET);
        confirmButton = new StyledButton(confirmText);
        confirmButton.setButtonStyle(StyledButton.ButtonStyle.CONFIRM);
        abortButton = new StyledButton(abortText);
        abortButton.setButtonStyle(StyledButton.ButtonStyle.CONFIRM);
    }

    private void initializeActionListeners() {
        confirmButton.setOnAction(event -> this.setConfirmClicked(true));
        abortButton.setOnAction(event -> this.setConfirmClicked(false));
    }

    private GridPane createBackgroundPane() {
        buttonsPane = createGridPane();
        buttonsPane.add(confirmButton, 0, 0);
        buttonsPane.add(abortButton, 1, 0);

        infoPane = createGridPane();
        infoPane.add(messageLabel, 0, 0);

        errorPane = createGridPane();

        GridPane backgroundPane = createGridPane();
        backgroundPane.add(infoPane, 0, 0);
        backgroundPane.add(buttonsPane, 0, 1);
        backgroundPane.add(errorPane, 0, 2);

        return backgroundPane;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    protected GridPane getInfoPane() {
        return infoPane;
    }

    protected GridPane getErrorPane() {
        return errorPane;
    }

    public void setConfirmClicked(boolean confirm) {
        this.setResult(confirm);
    }

}