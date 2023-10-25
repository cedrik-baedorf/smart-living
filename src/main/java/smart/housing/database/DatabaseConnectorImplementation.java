package smart.housing.database;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConnectorImplementation implements DatabaseConnector {

    /**
     * private <code>EntityManagerFactory</code> used to create <code>EntityManager</code> objects.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * This constructor creates a <code>DatabaseConnectionImplementation</code> object which is connected to the
     * persistence unit specified in the <code>persistence-unit.properties</code> file using the database access
     * properties specified in the <code>db-access.properties</code> file.
     */
    public DatabaseConnectorImplementation() {
        Map<String, String> loginProperties = new HashMap<>();
        Properties dbAccessProperties = getProperties(DB_ACCESS_PROPERTIES);

        //load database username
        if(dbAccessProperties.containsKey(USER_PROPERTY))
            loginProperties.put(USER_PROPERTY, dbAccessProperties.getProperty(USER_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(USER_PROPERTY, "loginProperties"));

        //load database uesr password
        if(dbAccessProperties.containsKey(PASSWORD_PROPERTY))
            loginProperties.put(PASSWORD_PROPERTY, dbAccessProperties.getProperty(PASSWORD_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(PASSWORD_PROPERTY, "loginProperties"));

        //load jdbc driver
        if(dbAccessProperties.containsKey(DRIVER_PROPERTY))
            loginProperties.put(DRIVER_PROPERTY, dbAccessProperties.getProperty(DRIVER_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(DRIVER_PROPERTY, "loginProperties"));

        //load jdbc url
        if(dbAccessProperties.containsKey(URL_PROPERTY))
            loginProperties.put(URL_PROPERTY, dbAccessProperties.getProperty(URL_PROPERTY));
        else
            throw new PropertyNotFoundException(missingProperty(URL_PROPERTY, "loginProperties"));

        //try creating EntityManagerFactory
        entityManagerFactory = createEntityManagerFactory(loginProperties);
    }

    public DatabaseConnectorImplementation(Map<String, String> loginProperties) throws ServiceException {
        try {
            //try creating EntityManagerFactory
            entityManagerFactory = createEntityManagerFactory(loginProperties);

            File propertiesFile = new File("src/main/resources/" + DB_ACCESS_PROPERTIES);
            if(! propertiesFile.exists())
                propertiesFile.createNewFile();

            //write login properties into the file after successfull completion
            FileWriter propertiesFileWriter = new FileWriter(propertiesFile);
            propertiesFileWriter.write(USER_PROPERTY + '=' + loginProperties.get(USER_PROPERTY) + '\n');
            propertiesFileWriter.write(PASSWORD_PROPERTY + '=' + loginProperties.get(PASSWORD_PROPERTY) + '\n');
            propertiesFileWriter.write(URL_PROPERTY + '=' + loginProperties.get(URL_PROPERTY) + '\n');
            propertiesFileWriter.write(DRIVER_PROPERTY + '=' + loginProperties.get(DRIVER_PROPERTY));

            propertiesFileWriter.close();
        } catch (IOException | PropertyNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    private EntityManagerFactory createEntityManagerFactory(Map<String, String> accessProperties) {
        //check if user property is set
        if(! accessProperties.containsKey(USER_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(USER_PROPERTY, "loginProperties"));

        //check if password property is set
        if(! accessProperties.containsKey(PASSWORD_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(PASSWORD_PROPERTY, "loginProperties"));

        //check if url property is set
        if(! accessProperties.containsKey(URL_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(URL_PROPERTY, "loginProperties"));

        //check if driver property is set
        if(! accessProperties.containsKey(DRIVER_PROPERTY))
            throw new PropertyNotFoundException(missingProperty(DRIVER_PROPERTY, "loginProperties"));

        String persistenceUnit = null;
        try {
            Properties persistenceUnitProperties = getProperties(PERSISTENCE_UNIT_PROPERTIES);
            persistenceUnit = persistenceUnitProperties.getProperty("persistence_unit");
            if (persistenceUnit != null)
                return Persistence.createEntityManagerFactory(persistenceUnit, accessProperties);
            else {
                final String MSG = "'persistence_unit' property not found in database properties";
                throw new RuntimeException(MSG);
            }
        } catch (Exception e) {
            final String MSG = "Unable to create connection using " + accessProperties + " and " + persistenceUnit;
            throw new ServiceException(MSG);
        }
    }

    @Override
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private Properties getProperties(String propertiesPath) {
        Properties properties;
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesPath)) {
            properties = new Properties();
            properties.load( is );
        } catch (Exception e) {
            final String msg = "Loading properties file " + propertiesPath + " failed";
            throw new ServiceException(msg);
        }
        return properties;
    }

    private String missingProperty(String property, String properties) {
        return "Property " + property + " not found in properties " + properties;
    }
}
