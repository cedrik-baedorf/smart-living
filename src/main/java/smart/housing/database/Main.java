package smart.housing.database;

import smart.housing.entities.User;

import javax.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {
        LoginManager login = new LoginManagerImplementation();
        EntityManager em = login.login("cbaedorf", "password");
        User user = em.find(User.class, "cbaedorf");
        System.out.println(user.toString());
    }

}
