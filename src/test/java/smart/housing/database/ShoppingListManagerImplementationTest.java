package smart.housing.database;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.entities.ShoppingListItem;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.services.ShoppingListServiceImplementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingListManagerImplementationTest {

    public ShoppingListServiceImplementation service;

    @Test
    public void test_readList() {

        ObservableList<ShoppingListItem> items = service.readList();

        assertEquals(2,items.size());
    }

}

