package smart.housing.database;

import smart.housing.entities.User;
import smart.housing.security.SimpleHashAlgorithm;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoginManagerImplementation implements LoginManager {

    @Override
    public EntityManager login(String username, String password) {
        Properties accessProperties = getAccessProperties("smart.housing/access.properties");
        Map<String, String> map = new HashMap<>();
        String databaseUsername = accessProperties.getProperty(DatabaseConnector.USER_PROPERTY);
        String databasePassword = accessProperties.getProperty(DatabaseConnector.PASSWORD_PROPERTY);
        map.put(DatabaseConnector.USER_PROPERTY, databaseUsername);
        map.put(DatabaseConnector.PASSWORD_PROPERTY, databasePassword);
        DatabaseConnector connector = new DatabaseConnectorImplementation(map);
        EntityManager em = connector.createEntityManager();
        User user = em.find(User.class, username);
        return user.getPassword().equals(new SimpleHashAlgorithm().hash(password)) ? em : null;
    }

    private Properties getAccessProperties(String dbPropertiesPath) {
        Properties accessProperties;
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(dbPropertiesPath)) {
            accessProperties = new Properties();
            accessProperties.load( is );
        } catch (Exception e) {
            final String msg = "Loading database connection properties failed";
            throw new RuntimeException(msg);
        }
        return accessProperties;
    }
}
