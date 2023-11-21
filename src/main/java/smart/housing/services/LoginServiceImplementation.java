package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;

public class LoginServiceImplementation implements LoginService {

    public static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.DEFAULT;

    private final DatabaseConnector DATABASE_CONNECTOR;

    public LoginServiceImplementation(DatabaseConnector databaseConnector) {
        this.DATABASE_CONNECTOR = databaseConnector;
    }

    @Override
    public User userLogin(String username, String password) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        if(username == null || username.isEmpty())
            throw new UserManagementServiceException(String.format(MSG_LOGIN_EMPTY, "username"));
        if(password == null || password.isEmpty())
            throw new UserManagementServiceException(String.format(MSG_LOGIN_EMPTY, "password"));
        if(username.length() > User.USERNAME_LENGTH)
            throw new IncorrectCredentialsException(String.format(MSG_LOGIN_LENGTH, "username", User.USERNAME_LENGTH));
        User user = entityManager.find(User.class, username);
        if(user == null || ! user.getPassword().equals(HASH_ALGORITHM.hash(password)))
            throw new IncorrectCredentialsException(String.format(MSG_LOGIN_FAILED, username));
        entityManager.close();
        return user;
    }

    @Override
    public UserManagementService serviceLogin(String username, String password) {
        return new UserManagementServiceImplementation(DATABASE_CONNECTOR, userLogin(username, password));
    }

}
