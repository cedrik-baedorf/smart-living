package smart.housing.services;

import org.hibernate.exception.ConstraintViolationException;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.util.Arrays;
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
            throw new UserManagementServiceException(String.format(MSG_CREATE_NULL, "User.class"));
        if(user.getPassword() == null)
            throw new UserManagementServiceException(String.format(MSG_CREATE_NULL, "user.getPassword()"));
        if(USER == null)
            throw new UserManagementServiceException("service must have a service user for this service");
        if(! USER.getRole().outranks(user.getRole()))
            throw new UserManagementServiceException(String.format(MSG_CREATE_LOWER_RANK, USER.getRole(), user.getRole()));
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        if(entityManager.find(User.class, user.getUsername()) != null)
            throw new UserManagementServiceException(String.format(MSG_CREATE_USERNAME_EXISTS, user.getUsername()));
        entityManager.getTransaction().begin();
        entityManager.persist(user);
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
    public void modify(User user, User modifiedUser) {
        if(user == null)
            throw new UserManagementServiceException("cannot modify user when user = null");
        if(USER == null)
            throw new UserManagementServiceException("cannot modify user " + user.getUsername() + "when this.USER = null");
        if(! USER.getRole().outranks(user.getRole()))
            throw new UserManagementServiceException("cannot modify user of rank " + user.getRole().getRoleName() + " since this.USER.getRank() = " + USER.getRole().getRoleName());
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
    public List<User> getUsers() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        List<User> userList = entityManager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
        entityManager.close();
        return userList;
    }

    @Override
    public List<User> getUsers(UserRole... roles) {
        List<User> userList = getUsers();
        if(userList == null || userList.isEmpty())
            return userList;
        return userList.stream().filter(user -> Arrays.asList(roles).contains(user.getRole())).toList();
    }

    @Override
    public HashAlgorithm getHashAlgorithm() {
        return HASH_ALGORITHM;
    }

    @Override
    public User getServiceUser() {
        return this.USER;
    }

}
