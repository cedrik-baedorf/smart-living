package smart.housing.database;

import smart.housing.entities.User;
import smart.housing.security.HashAlgorithm;
import smart.housing.security.SimpleHashAlgorithm;

import javax.persistence.EntityManager;

public class LoginManagerImplementation implements LoginManager {

    private static final HashAlgorithm HASH_ALGORITHM = new SimpleHashAlgorithm();

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
    public void create(User user, EntityManager entityManager) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
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
            throw new RuntimeException("User " + username + " could not be deleted");
        }
    }
}
