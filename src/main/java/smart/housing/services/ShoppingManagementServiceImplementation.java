package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.ShoppingListItem;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ShoppingManagementServiceImplementation implements ShoppingManagementService {

    private final DatabaseConnector databaseConnector;
    private EntityManager em;

    public ShoppingManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(ShoppingListItem shoppingListItem) {
        em = databaseConnector.createEntityManager();
        em.getTransaction().begin();
        ShoppingListItem existingItem = em.find(ShoppingListItem.class, shoppingListItem.getItem());

        if (existingItem != null) {
            double newAnzahl = existingItem.getAmount() + shoppingListItem.getAmount();
            existingItem.setAmount(newAnzahl);
            em.merge(existingItem);
        } else {
            System.out.println(shoppingListItem.getItem() + " " + shoppingListItem.getAmount());
            em.persist(shoppingListItem);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete (ShoppingListItem shoppingListItem) {
        em = databaseConnector.createEntityManager();
        ShoppingListItem itemToBeRemoved = em.find(ShoppingListItem.class, shoppingListItem.getItem());
        em.getTransaction().begin();
        em.remove(itemToBeRemoved);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<ShoppingListItem> getItemList() {
        em = databaseConnector.createEntityManager();
        TypedQuery<ShoppingListItem> itemQuery = em.createNamedQuery(ShoppingListItem.FIND_ALL, ShoppingListItem.class);
        List<ShoppingListItem> itemList = itemQuery.getResultList();

        em.close();

        return itemList;
    }

    @Override
    public void modifyItem(ShoppingListItem shoppingListItem, Double newAmount) {
        em = databaseConnector.createEntityManager();
        em.getTransaction().begin();

        ShoppingListItem modifiedItem = em.find(ShoppingListItem.class,shoppingListItem.getItem());

        if(modifiedItem != null)
            modifiedItem.setAmount(newAmount);

        em.getTransaction().commit();
        em.close();
    }
}
