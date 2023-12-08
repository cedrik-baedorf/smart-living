package smart.housing.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Dialog;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

public class UserDataDialog extends Dialog<User> {

    private User applicationUser, user;

    private StyledTextField username, firstName, lastName, email;
    private StyledPasswordField password, confirmPassword;
    private StyledComboBox<UserRole> role;
    private StyledButton confirmButton;

    /**
     * This constructor takes two arguments of class {@link User}
     * @param applicationUser user of the application
     * @param user user object who's properties will be loaded into the input fields
     */
    public UserDataDialog(User applicationUser, User user) {
        this.applicationUser = applicationUser;
        this.user = user;

        this.createFields();

        clearFields();
        if(user != null)
            this.loadUserData();

        confirmButton.setOnAction(this::createUser);
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

        confirmButton = new StyledButton();
    }

    private void clearFields() {
        this.username.clear();
        this.firstName.clear();
        this.lastName.clear();
        this.role.setValue(UserRole.DEFAULT_ROLE);
        this.email.clear();
    }

    private void loadUserData() {
        this.username.setText(user.getUsername());
        this.firstName.setText(user.getFirstName());
        this.lastName.setText(user.getLastName());
        this.role.setValue(user.getRole());
        this.email.setText(user.getEmail());

    }

    private void createUser(ActionEvent event) {
        event.consume();
        try {
            this.setResult(createUser());
        } catch (EmptyFieldException exception) {
            new ErrorDialog(exception.getMessage());
        } catch (UserManagementServiceException exception) {
            new ErrorDialog(exception.getMessage()).show();
        } finally {
            clearFields();
            if(this.user != null)
                this.loadUserData();
        }
    }

    private User createUser() {
        if(! password.getText().equals(confirmPassword.getText()))
            throw new UserManagementServiceException("Passwords do not match");

        User user = new User(username.getText());

        if(password.getText().isEmpty()) {
            if(this.user == null)
                throw new EmptyFieldException("Please enter a password", "password");
            else
                user.setPasswordHash(this.user.getPassword());
        } else
            user.setPassword(password.getText(), HashAlgorithm.DEFAULT);

        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setRole(role.getValue());
        user.setEmail(email.getText());
        return user;
    }

}