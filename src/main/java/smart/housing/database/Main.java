package smart.housing.database;

import smart.housing.entities.User;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put(DatabaseConnector.USER_PROPERTY, "DEMO_USER");
        loginData.put(DatabaseConnector.PASSWORD_PROPERTY, "PASSWORD");
        loginData.put(DatabaseConnector.DRIVER_PROPERTY, "org.mariadb.jdbc.Driver");
        loginData.put(DatabaseConnector.URL_PROPERTY, "jdbc:mariadb://localhost:3306/smart-living");

        DatabaseConnector connector =
            new File("src/main/resources/" + DatabaseConnector.DB_ACCESS_PROPERTIES).exists() ?
                    new DatabaseConnectorImplementation() : new DatabaseConnectorImplementation(loginData);

        LoginManager login = new LoginManagerImplementation(connector);
        EntityManager em = login.login("cbaedorf", "password");
        User user = em.find(User.class, "cbaedorf");
        System.out.println(user.toString());
    }

}
