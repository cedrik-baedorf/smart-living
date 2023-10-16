package smart.housing.database;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class SmartLivingEntityManagerFactory implements SmartLivingEntityManagerInterface {

    /**
     * private <code>EntityManagerFactory</code> used to create <code>EntityManager</code> objects.
     */
    private final EntityManagerFactory entityManagerFactory;

    /**
     * This constructor creates a <code>TravelAgencyEntityManagerFactory</code> object which is connected to the
     * persistence unit specified in the <code>db.properties</code> file using the login properties provided and
     * driver and url properties defined in the <code>db.properties</code> file.
     * @param loginProperties <code>Map</code> object with persistence unit properties
     */
    public SmartLivingEntityManagerFactory(Map<String, String> loginProperties) {
        //check if user property is set
        if(! loginProperties.containsKey(USER_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(USER_PROPERTY, "loginProperties"));

        //check if password property is set
        if(! loginProperties.containsKey(PASSWORD_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(PASSWORD_PROPERTY, "loginProperties"));

        //load db properties
        String dbPropertiesPath = "smart.housing/db.properties";
        String persistenceUnit = null;
        Properties p = getDBAccessProperties(dbPropertiesPath);

        //load jdbc driver
        if(p.containsKey(DRIVER_PROPERTY))
            loginProperties.put(DRIVER_PROPERTY, p.getProperty(DRIVER_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(DRIVER_PROPERTY, "loginProperties"));

        //load jdbc url
        if(p.containsKey(URL_PROPERTY))
            loginProperties.put(URL_PROPERTY, p.getProperty(URL_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(URL_PROPERTY, "loginProperties"));

        //try creating EntityManagerFactory
        try {
            persistenceUnit = p.getProperty("persistence_unit");
            if (persistenceUnit != null)
                entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, loginProperties);
            else {
                final String MSG = "'persistence_unit' property not found in database properties";
                throw new RuntimeException(MSG);
            }
        } catch (Exception e) {
            final String MSG = "Unable to create connection using " + loginProperties + " and " + persistenceUnit;
            throw new ServiceException(MSG);
        }
    }

    @Override
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private Properties getDBAccessProperties(String dbPropertiesPath) {
        Properties dbAccessProperties;
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(dbPropertiesPath)) {
            dbAccessProperties = new Properties();
            dbAccessProperties.load( is );
        } catch (Exception e) {
            final String msg = "Loading database connection properties failed";
            throw new RuntimeException(msg);
        }
        return dbAccessProperties;
    }

    private String missingProperty(String property, String properties) {
        return "Property " + property + " not found in properties " + properties;
    }
}
