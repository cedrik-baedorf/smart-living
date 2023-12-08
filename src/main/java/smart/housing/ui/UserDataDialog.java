package smart.housing.ui;

import com.sun.glass.ui.GlassRobot;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import smart.housing.controllers.DialogController;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.EmptyFieldException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import java.util.Arrays;

public class UserDataDialog extends Dialog<User> {

    private User applicationUser, user;

    private StyledTextField username, firstName, lastName, email;
    private StyledPasswordField password, confirmPassword;
    private StyledComboBox<UserRole> role;
    private StyledButton confirmButton;

    /**
     * This constructor takes two arguments of class {@link User}
     * @param applicationUser user of the application
     */
    public UserDataDialog(String title, User applicationUser) {
        this(title, applicationUser, null);
    }

    /**
     * This constructor takes two arguments of class {@link User}
     * @param applicationUser user of the application
     * @param user user object who's properties will be loaded into the input fields
     */
    public UserDataDialog(String title, User applicationUser, User user) {
        this.applicationUser = applicationUser;
        this.user = user;
        this.setTitle(title);

        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            this.setResult(null);
            this.hide();
        });

        this.getDialogPane().setContent(this.createFields());

        clearFields();
        if(user != null)
            this.loadUserData();

        confirmButton.setOnAction(this::createUser);
    }

    private GridPane createFields() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        username = new StyledTextField();
        username.setPromptText("username");
        gridPane.add(username, 0, 0);

        firstName = new StyledTextField();
        firstName.setPromptText("first name");
        gridPane.add(firstName, 0, 1);

        lastName = new StyledTextField();
        lastName.setPromptText("last name");
        gridPane.add(lastName, 0, 2);

        role = new StyledComboBox<>();
        role.setItems(FXCollections.observableList(Arrays.stream(UserRole.values())
            .filter(userRole -> applicationUser.getRole().outranks(userRole))
            .toList()
        ));
        gridPane.add(role, 0, 3);

        email = new StyledTextField();
        email.setPromptText("email");
        gridPane.add(email, 0, 4);

        password = new StyledPasswordField();
        password.setPromptText("password");
        gridPane.add(password, 0, 5);

        confirmPassword = new StyledPasswordField();
        confirmPassword.setPromptText("confirm password");
        gridPane.add(confirmPassword, 0, 6);

        confirmButton = new StyledButton("Confirm Data");
        gridPane.add(confirmButton, 0, 7);

        return gridPane;
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