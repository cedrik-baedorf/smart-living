package smart.housing.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.dialect.Database;
import smart.housing.entities.ShoppingListItem;

import javax.persistence.EntityManager;
import java.util.List;

public class ShoppingListManagerImplementation implements ShoppingListManager {

    private final DatabaseConnector databaseConnector;

    public ShoppingListManagerImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public ObservableList<ShoppingListItem> getItems() {
        EntityManager entityManager = databaseConnector.createEntityManager();
        List<ShoppingListItem> results = entityManager.createQuery("Select * from ShoppingListItem", ShoppingListItem.class).getResultList();

        ObservableList<ShoppingListItem> observableList = FXCollections.observableList(results);
        return observableList;
    }
}
