package smart.housing.services;

import smart.housing.entities.User;
import smart.housing.exceptions.UserManagementServiceException;

import java.util.List;

public interface UserManagementService extends SmartLivingService {

    String MSG_LOWER_RANK = "Role %s of current user is not able to %s user with role %s";
    String MSG_CREATE_USERNAME_EXISTS = "Attempted to persist User but username %s already exists";
    String MSG_NULL_VALUE = "cannot %s %s when %s is null";

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
     * The <code>{@link User}</code> to be deleted is determined by the <code>user</code>
     * parameter provided in the method signature. The user shall only be deleted if it is not
     * assigned to any task or expense in the other services.
     * Otherwise, this method will throw a {@link UserManagementServiceException}
     * @param user user that shall be deleted
     */
    void delete(User user) throws UserManagementServiceException;

    /**
     * This method modifies an object of class <code>{@link User}</code> in the database.
     * The <code>{@link User}</code> to be modified is determined by the <code>user</code>
     * parameter provided in the method signature. The update user data is taken from the
     * <code>modifiedUser</code> parameter. Every attribute from <code>modified</code> will be
     * transferred to <code>user</code> and persisted to the database except the <code>username</code>.
     * Otherwise, this method will throw a {@link UserManagementServiceException}
     * @param user user that shall be deleted
     */
    void modify(User user, User modifiedUser) throws UserManagementServiceException;

    /**
     * This method shall return all <code>{@link User}</code> objects from the database
     * as a <code>{@link java.util.List}</code>.
     * If no <code>{@link User}</code> is found, this method shall return an empty
     * <code>{@link List}</code> object.
     * @return <code>{@link List}</code> object of type <code>{@link User}</code>
     */
    List<User> getUsers();

    /**
     * This method shall return the <code>{@link User}</code> object from the database
     * with the username provided.
     * If no <code>{@link User}</code> is found, this method shall return null.
     * @return {@link User} object
     */
    User getUser(String username);

    /**
     * Getter method for this.USER
     * @return {@link User} object related to this service
     */
    User getServiceUser();

}