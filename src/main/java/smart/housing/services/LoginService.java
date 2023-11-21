package smart.housing.services;

import smart.housing.entities.User;
import smart.housing.exceptions.UserManagementServiceException;

public interface LoginService {

    String MSG_LOGIN_EMPTY = "Login failed using empty %s property";
    String MSG_LOGIN_LENGTH = "Length of %s cannot be longer that %s characters";
    String MSG_LOGIN_FAILED = "Login failed for username '%s'";

    /**
     * This method takes a username and a password and checks whether they match.
     * If they match, the method shall return the <code>User</code> object from the database.
     * If they do not match, this method shall throw a <code>IncorrectCredentialsException</code>.
     * If the username does not exist in the database, this method shall throw a <code>LoginServiceException</code>.
     * @param username username determines the user that is attempted to log in
     * @param password password must match the password stored for the username
     * @return <code>User</code> object related to the username and password provided
     */
    User userLogin(String username, String password);

    /**
     * This method takes a username and a password and checks whether they match.
     * If they match, the method shall return a {@link UserManagementService} with the corresponding
     * {@link User} as the user of the service.
     * If they do not match, this method shall throw a <code>IncorrectCredentialsException</code>.
     * If the username does not exist in the database, this method shall throw a <code>LoginServiceException</code>.
     * @param username username determines the user that is attempted to log in
     * @param password password must match the password stored for the username
     * @return <code>User</code> object related to the username and password provided
     */
    UserManagementService serviceLogin(String username, String password) throws UserManagementServiceException;

}
