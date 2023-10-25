package smart.housing.database;

import org.hibernate.service.spi.ServiceException;
import smart.housing.entities.User;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;

public class LoginManagerImplementation implements LoginManager {

    private static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.DEFAULT;

    private final DatabaseConnector databaseConnector;

    public LoginManagerImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public EntityManager login(String username, String password) {
        EntityManager em = databaseConnector.createEntityManager();
        if(username.length() > 8)
            return null;
        User user = em.find(User.class, username);
        return user != null && user.getPassword().equals(HASH_ALGORITHM.hash(password)) ? em : null;
    }

    @Override
    public void create(User user) {
        if(user == null)
            throw new RuntimeException(String.format(MSG_CREATE_USER_NULL, "User.class"));
        if(user.getUsername() == null)
            throw new RuntimeException(String.format(MSG_CREATE_USER_NULL, "user.getUsername()"));
        EntityManager entityManager = databaseConnector.createEntityManager();
        if(entityManager.find(User.class, user.getUsername()) != null)
            throw new RuntimeException(String.format(MSG_CREATE_USERNAME_EXISTS, user.getUsername()));
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(String username, String password) {
        EntityManager entityManager = login(username, password);
        User user = entityManager.find(User.class, username);
        if(HASH_ALGORITHM.hash(password).equals(user.getPassword())) {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
        } else {
            throw new ServiceException("User " + username + " could not be deleted");
        }
    }
}
