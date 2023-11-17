package smart.housing.ui;

import javafx.scene.control.Dialog;
import javafx.scene.text.TextAlignment;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;

/**
 * This class extends {@link Dialog<Boolean>} and creates a simple popup to confirm an action.
 * When the <code>confirmButton</code> is clicked, the result of this dialog will be <code>true</code>.
 * When the <code>abortButton</code> is clicked, the result of this dialog will be <code>false</code>.
 * To set an action on confirm, please use the method <code>setOnConfirm(</code>
 */
public class ConfirmPasswordDialog extends ConfirmDialog {

    private final User USER;

    private final LoginService LOGIN_SERVICE;

    private StyledPasswordField passwordField;

    private ErrorMessage errorMessage;

    public ConfirmPasswordDialog(
        String messageText, String confirmText, String abortText,
        User user, DatabaseConnector databaseConnector
    ) {
        super(messageText, confirmText, abortText);
        this.USER = user;
        this.LOGIN_SERVICE = new LoginServiceImplementation(databaseConnector);

        initializePasswordField();
    }

    private void initializePasswordField() {
        passwordField = new StyledPasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(StyledNode.UI_ELEMENT_STYLE_SHEET);

        errorMessage = new ErrorMessage();
        errorMessage.setTextAlignment(TextAlignment.CENTER);

        super.getInfoPane().add(passwordField, 0, 1);

        super.getErrorPane().add(errorMessage, 0, 0);
    }

    @Override
    public void setConfirmClicked(boolean confirm) {
        if(! confirm) { //Do nothing if abortButton has been clicked
            this.setResult(false);
            return;
        }
        try {
            if (LOGIN_SERVICE.userLogin(USER.getUsername(), passwordField.getText()).equals(USER)) //test of a user with username and password exists in the database and if that user is the same one using the application
                this.setResult(true);
            else
                throw new IncorrectCredentialsException("this.USER is incompatible with the database");
        } catch (IncorrectCredentialsException exception) {
            errorMessage.displayError("Password incorrect", 5);
        }
    }

}