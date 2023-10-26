package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;

public class LoginServiceImplementation implements LoginService {

    public static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.DEFAULT;

    private final DatabaseConnector databaseConnector;

    public LoginServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public User login(String username, String password) {
        EntityManager entityManager = databaseConnector.createEntityManager();
        if(username == null || username.length() == 0)
            throw new LoginServiceException(String.format(MSG_LOGIN_EMPTY, "username"));
        if(password == null || password.length() == 0)
            throw new LoginServiceException(String.format(MSG_LOGIN_EMPTY, "password"));
        if(username.length() > User.USERNAME_LENGTH)
            throw new IncorrectCredentialsException(String.format(MSG_LOGIN_LENGTH, "username", User.USERNAME_LENGTH));
        User user = entityManager.find(User.class, username);
        if(user == null || ! user.getPassword().equals(HASH_ALGORITHM.hash(password)))
            throw new IncorrectCredentialsException(String.format(MSG_LOGIN_FAILED, username));
        entityManager.close();
        return user;
    }

    @Override
    public void create(User user) {
        if(user == null)
            throw new LoginServiceException(String.format(MSG_CREATE_NULL, "User.class"));
        if(user.getPassword() == null)
            throw new LoginServiceException(String.format(MSG_CREATE_NULL, "user.getPassword()"));
        EntityManager entityManager = databaseConnector.createEntityManager();
        if(entityManager.find(User.class, user.getUsername()) != null)
            throw new LoginServiceException(String.format(MSG_CREATE_USERNAME_EXISTS, user.getUsername()));
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(String username, String password) {
        User userToBeDeleted = login(username, password);
        if(HASH_ALGORITHM.hash(password).equals(userToBeDeleted.getPassword())) {
            EntityManager entityManager = databaseConnector.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(userToBeDeleted);
            entityManager.getTransaction().commit();
            entityManager.close();
        } else {
            throw new IncorrectCredentialsException(String.format(MSG_DELETE_UNSUCCESSFUL, username));
        }
    }

    @Override
    public HashAlgorithm getHashAlgorithm() {
        return HASH_ALGORITHM;
    }
}
