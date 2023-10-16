package smart.housing.database;

import smart.housing.entities.User;
import smart.housing.security.SimpleHashAlgorithm;

import javax.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {
        LoginManager login = new LoginManagerImplementation();
        EntityManager em = login.login("cbaedorf", "password");
        User user = em.find(User.class, "cbaedorf");
        System.out.println(user.toString());

        User anna = new User("nrg", "software", new SimpleHashAlgorithm());
        anna.setFirstName("Anna");
        anna.setLastName("Gossner");
        login.create(anna, em);


        System.out.println(em.find(User.class, "nrg"));
    }

}
