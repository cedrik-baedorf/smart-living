package smart.housing.services;

import org.hibernate.exception.ConstraintViolationException;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.util.List;

public class UserManagementServiceImplementation implements UserManagementService {

    public static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.DEFAULT;

    private final DatabaseConnector DATABASE_CONNECTOR;

    private final User USER;

    /**
     * Constructor takes two arguments for this service to function properly.
     * @param databaseConnector connector to the database to be used
     * @param user user that is using this service
     */
    public UserManagementServiceImplementation(DatabaseConnector databaseConnector, User user) {
        this.DATABASE_CONNECTOR = databaseConnector;
        this.USER = user;
    }

    @Override
    public DatabaseConnector getDatabaseConnector() {
        return DATABASE_CONNECTOR;
    }

    @Override
    public void create(User user) {
        if(user == null)
            throw new UserManagementServiceException(String.format(MSG_NULL_VALUE, "create", "user", "user"));
        if(user.getPassword() == null)
            throw new UserManagementServiceException(String.format(MSG_NULL_VALUE, "create", "user", "user.getPassword()"));
        if(USER == null)
            throw new UserManagementServiceException("service must have a service user for this service");
        if(! USER.getRole().outranks(user.getRole()))
            throw new UserManagementServiceException(String.format(MSG_LOWER_RANK, USER.getRole(), "create", user.getRole()));
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        if(entityManager.find(User.class, user.getUsername()) != null)
            throw new UserManagementServiceException(String.format(MSG_CREATE_USERNAME_EXISTS, user.getUsername()));
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void modify(User user, User modifiedUser) {
        if(user == null)
            throw new UserManagementServiceException(String.format(MSG_NULL_VALUE, "modify", "user", "user"));
        if(modifiedUser == null)
            throw new UserManagementServiceException(String.format(MSG_NULL_VALUE, "modify", "user", "modifiedUser"));
        if(USER == null)
            throw new UserManagementServiceException(String.format(MSG_NULL_VALUE, "modify", "user", "this.USER"));
        if(! USER.getRole().outranks(user.getRole()))
            throw new UserManagementServiceException(String.format(MSG_LOWER_RANK, USER.getRole(), "modify", user.getRole()));
        if(! USER.getRole().outranks(modifiedUser.getRole()))
            throw new UserManagementServiceException(String.format(MSG_LOWER_RANK, USER.getRole(), "modify", modifiedUser.getRole()));

        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        entityManager.getTransaction().begin();

        user = entityManager.merge(user);
        user.setFirstName(modifiedUser.getFirstName());
        user.setLastName(modifiedUser.getLastName());
        if(modifiedUser.getPassword() != null && ! modifiedUser.getPassword().isEmpty())
            user.setPasswordHash(modifiedUser.getPassword());
        user.setRole(modifiedUser.getRole());
        user.setEmail(modifiedUser.getEmail());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(User user) {
        if(user == null)
            throw new UserManagementServiceException("cannot delete user when user = null");
        if(USER == null)
            throw new UserManagementServiceException("cannot delete user " + user.getUsername() + "when this.USER = null");
        if(! USER.getRole().outranks(user.getRole()))
            throw new UserManagementServiceException("cannot delete user of rank " + user.getRole().getRoleName() + " since this.USER.getRank() = " + USER.getRole().getRoleName());
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        entityManager.getTransaction().begin();
        user = entityManager.merge(user);
        entityManager.remove(user);
        try {
            entityManager.getTransaction().commit();
        } catch (RollbackException exception) {
            if(exception.getCause().getCause().getClass().equals(ConstraintViolationException.class))
                throw new UserManagementServiceException("User cannot be deleted if it is still assigned to tasks or expenses", exception);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<User> getUsers() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        List<User> userList = entityManager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
        entityManager.close();
        return userList;
    }

    @Override
    public User getUser(String username) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        User user = entityManager.find(User.class, username);
        entityManager.close();
        return user;
    }

    @Override
    public User getServiceUser() {
        return this.USER;
    }

}
