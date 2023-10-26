package smart.housing.services;

import smart.housing.entities.User;
import smart.housing.exceptions.LoginServiceException;

import javax.persistence.EntityManager;

public interface LoginService {

    String MSG_LOGIN_LENGTH = "Length of %s cannot be longer that %s characters";
    String MSG_LOGIN_FAILED = "Login failed for username '%s'";
    String MSG_CREATE_NULL = "Attempted to persist User but %s was null";
    String MSG_CREATE_USERNAME_EXISTS = "Attempted to persist User but username %s already exists";
    String MSG_DELETE_UNSUCCESSFUL = "Attempt to delete user '%s' was unsuccessful";

    /**
     * This method takes a username and a password and checks whether they match.
     * If they match, the method shall return the <code>User</code> object from the database.
     * If they do not match, this method shall throw a <code>IncorrectCredentialsException</code>.
     * If the username does not exist in the database, this method shall throw a <code>LoginServiceException</code>.
     * @param username username determines the user that is attempted to log in
     * @param password password must match the password stored for the username
     * @return <code>User</code> object related to the username and password provided
     */
    User login(String username, String password) throws LoginServiceException;

    /**
     * This method takes an object of class <code>User</code> and persists it in the database, if
     *  its <i>user</i> is not equal to <code>null</code>,
     *  its <i>username</i> does not exist in the database
     *  its <i>password</i> is not equal to <code>null</code>
     * Otherwise, this method will throw a <code>LoginServiceException</code>
     * @param user user to be persisted in the database
     */
    void create(User user) throws LoginServiceException;

    void delete(String username, String password) throws LoginServiceException;

}