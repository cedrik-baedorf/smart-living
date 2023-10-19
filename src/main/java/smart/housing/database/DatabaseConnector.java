package smart.housing.database;

import javax.persistence.EntityManager;

/**
 * This interface defines a service which can be used to create entities managers
 * for the persistence unit defined in the <code>persistence-unit.properties</code> file.
 * @author I551381
 * @version 1.0
 */
public interface DatabaseConnector {

    /**
     * Name of the <code>driver</code> property in the <code>persistence.xml</code> file
     */
    String DRIVER_PROPERTY = "javax.persistence.jdbc.driver";

    /**
     * Name of the <code>url</code> property in the <code>persistence.xml</code> file
     */
    String URL_PROPERTY = "javax.persistence.jdbc.url";

    /**
     * Name of the <code>user</code> property in the <code>persistence.xml</code> file
     */
    String USER_PROPERTY = "javax.persistence.jdbc.user";

    /**
     * Name of the <code>password</code> property in the <code>persistence.xml</code> file
     */
    String PASSWORD_PROPERTY = "javax.persistence.jdbc.password";

    /**
     * File path for database access properties
     */
    String DB_ACCESS_PROPERTIES = "smart/housing/db-access.properties";

    /**
     * File path for the persistence unit properties
     */
    String PERSISTENCE_UNIT_PROPERTIES = "smart/housing/persistence-unit.properties";

    /**
     * This public method shall return an <code>EntityManager</code> connected to the persistence unit
     * specified in the <code>persistence-unit.properties</code> file
     * @return <code>EntityManager</code> object to be used to find and persist entities
     */
    EntityManager createEntityManager();

}