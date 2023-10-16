package smart.housing.database;

import smart.housing.entities.User;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.jdbc.user", "DEMO_USER");
        props.put("javax.persistence.jdbc.password", "PASSWORD");
        DatabaseConnector emf = new DatabaseConnectorImplementation(props);
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, "cbaedorf");
        System.out.println(user.toString());
    }

}
