package smart.housing.database;

import smart.housing.entities.User;
import smart.housing.security.SimpleHashAlgorithm;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        LoginManager login = new LoginManagerImplementation();
        EntityManager em = login.login("cbaedorf", "password");
        User user = em.find(User.class, "cbaedorf");
        System.out.println(user.toString());
    }

}
