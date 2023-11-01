package smart.housing.services;

import smart.housing.entities.Role;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import java.util.List;

public interface UserManagementService {

    String MSG_LOGIN_EMPTY = "Login failed using empty %s property";
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
    User login(String username, String password) throws UserManagementServiceException;

    /**
     * This method takes an object of class <code>User</code> and persists it in the database, if
     *  its <i>user</i> is not equal to <code>null</code>,
     *  its <i>username</i> does not exist in the database
     *  its <i>password</i> is not equal to <code>null</code>
     * Otherwise, this method will throw a <code>LoginServiceException</code>
     * @param user user to be persisted in the database
     */
    void create(User user) throws UserManagementServiceException;

    /**
     * This method deletes an object of class <code>{@link User}</code> from the database.
     * The <code>{@link User}</code> to be deleted is determined by the <code>username</code>
     * parameter provided in the method signature. The user shall only be deleted if the
     * <code>password</code> provided matches the password in the database.
     * Otherwise, this method will throw a <code>LoginServiceException</code>
     * @param username username of the user that shall be deleted
     * @param password un-hashed password belonging to the username
     */
    void delete(String username, String password) throws UserManagementServiceException;

    /**
     * This method shall return all <code>{@link User}</code> objects from the database
     * as a <code>{@link java.util.List}</code>.
     * If no <code>{@link User}</code> is found, this method shall return an empty
     * <code>{@link List}</code> object.
     * @return <code>{@link List}</code> object of type <code>{@link User}</code>
     */
    List<User> getUsers();

    /**
     * This method shall return all <code>{@link User}</code> objects from the database
     * as a <code>{@link java.util.List}</code> filtered by an array of <code>{@link UserRole}</code> objects.
     * If no corresponding <code>{@link User}</code> is found, this method shall return an empty
     * <code>{@link List}</code> object.
     * @return <code>{@link List}</code> object of type <code>{@link User}</code>
     */
    List<User> getUsers(UserRole... roles);

    HashAlgorithm getHashAlgorithm();

}