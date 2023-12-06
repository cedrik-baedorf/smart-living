package smart.housing.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;

public class UserDataDialog extends Dialog<User> {

    private StyledTextField username, firstName, lastName, email;
    private StyledPasswordField password, confirmPassword;
    private StyledComboBox<UserRole> role;

    /**
     * This constructor takes two arguments of class {@link User}
     * @param applicationUser user of the application
     * @param user user object who's properties will be loaded into the input fields
     */
    public UserDataDialog(User applicationUser, User user) {
        this.createFields();
    }

    private void createFields() {
        username = new StyledTextField();
        username.setPromptText("username");

        firstName = new StyledTextField();
        username.setPromptText("first name");

        lastName = new StyledTextField();
        username.setPromptText("last name");

        role = new StyledComboBox<>();

        email = new StyledTextField();
        username.setPromptText("email");

        password = new StyledPasswordField();
        username.setPromptText("password");

        confirmPassword = new StyledPasswordField();
        username.setPromptText("confirm password");
    }

}