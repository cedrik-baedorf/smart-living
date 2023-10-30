package smart.housing.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.dialect.Database;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.ShoppingListItem;

import javax.persistence.EntityManager;
import java.util.List;

public class ShoppingListServiceImplementation implements ShoppingListService{

    private final DatabaseConnector databaseConnector;
    private EntityManager entityManager;

    public ShoppingListServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(ShoppingListItem shoppingListItem) {
        if (shoppingListItem != null) {
            entityManager.getTransaction().begin();
            entityManager.persist(shoppingListItem);
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @Override
    public ObservableList<ShoppingListItem> readList() {
        entityManager = databaseConnector.createEntityManager();
        List<ShoppingListItem> shoppingListItemList = entityManager.createNativeQuery("SELECT * FROM `smart-living`.shopping_list_items").getResultList();
        entityManager.close();
        ObservableList<ShoppingListItem> observableList = FXCollections.observableList(shoppingListItemList);

        return observableList;
    }
}
