package smart.housing.database;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.jdbc.user", "DEMO_USER");
        props.put("javax.persistence.jdbc.password", "PASSWORD");
        SmartLivingEntityManagerInterface emf = new SmartLivingEntityManagerFactory(props);
        EntityManager em = emf.createEntityManager();
        System.out.println("Hello World");
    }

}
