package smart.housing.database;

import javax.persistence.EntityManager;

public interface LoginManager {

    /**
     * This method takes a username and a password and checks whether they match.
     * If they match, the method shall return an <code>EntityManager</code> object connected to the database.
     * If they do not match, this method shall return <code>null</code>.
     * If the username does not exist in the database, this method shall return <code>null</code>.
     * @param username username determines the user that is attempted to log in
     * @param password password must match the password stored for the username
     * @return entity manager connected to the database
     */
    public EntityManager login(String username, String password);

}