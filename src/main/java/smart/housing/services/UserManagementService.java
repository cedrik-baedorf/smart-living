package smart.housing.services;

import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import java.util.List;

public interface UserManagementService {

    String MSG_CREATE_LOWER_RANK = "Role %s of current user is not able to create user with role %s";
    String MSG_CREATE_NULL = "Attempted to persist User but %s was null";
    String MSG_CREATE_USERNAME_EXISTS = "Attempted to persist User but username %s already exists";
    String MSG_UNSUCCESSFUL = "Attempt to %s user '%s' was unsuccessful";

    /**
     * This method takes an object of class <code>User</code> and persists it in the database, if
     *  its <i>user</i> is not equal to <code>null</code>,
     *  its <i>username</i> does not exist in the database
     *  its <i>password</i> is not equal to <code>null</code>
     *  its <i>role</i> is not ranked higher than role of <code>this.USER</code>
     * Otherwise, this method will throw a {@link UserManagementServiceException}
     * @param user user to be persisted in the database
     */
    void create(User user) throws UserManagementServiceException;

    /**
     * This method deletes an object of class <code>{@link User}</code> from the database.
     * The <code>{@link User}</code> to be deleted is determined by the <code>username</code>
     * parameter provided in the method signature. The user shall only be deleted if the
     * <code>password</code> provided matches the password in the database.
     * Otherwise, this method will throw a {@link UserManagementServiceException}
     * @param username username of the user that shall be deleted
     * @param password un-hashed password belonging to the username
     */
    void delete(String username, String password) throws UserManagementServiceException;

    void delete(User user) throws UserManagementServiceException;

    void modify(String username, String password, User updateUser) throws UserManagementServiceException;

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

    /**
     * Getter method for this.USER
     * @return {@link User} object related to this service
     */
    User getServiceUser();

}