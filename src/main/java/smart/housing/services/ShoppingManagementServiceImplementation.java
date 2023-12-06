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

        if (shoppingListItem.getAmount() <= 0 || shoppingListItem.getAmount()>100000.0) {
            throw new IllegalArgumentException("Amount cannot be negative nor over 100000");
        } else if (shoppingListItem.getItem() == null || shoppingListItem.getItem().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }

        em = databaseConnector.createEntityManager();
        em.getTransaction().begin();
        ShoppingListItem existingItem = em.find(ShoppingListItem.class, shoppingListItem.getItem());

        if (existingItem != null) {
            if (existingItem.getUnit().compareTo(shoppingListItem.getUnit())!=0) {
                throw new IllegalArgumentException("Unit can not be different to the Item unit in the database");
            } else {
                double newAnzahl = existingItem.getAmount() + shoppingListItem.getAmount();
                existingItem.setAmount(newAnzahl);
                em.merge(existingItem);
            }
        } else {
            em.persist(shoppingListItem);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete (ShoppingListItem shoppingListItem) {
        em = databaseConnector.createEntityManager();
        ShoppingListItem itemToBeRemoved = em.find(ShoppingListItem.class, shoppingListItem.getItem());

        if (itemToBeRemoved == null) {
            throw new IllegalArgumentException("Item not found");
        }

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
        if (shoppingListItem == null || newAmount == null || newAmount <= 0 || newAmount>100000.0) {
            throw new IllegalArgumentException("ShoppingListItem or new amount cannot be null");
        }

        EntityManager em = databaseConnector.createEntityManager();
        ShoppingListItem modifiedItem = em.find(ShoppingListItem.class, shoppingListItem.getItem());

        if (modifiedItem == null) {
            throw new IllegalArgumentException("Item not found in the database");
        }

        em.getTransaction().begin();
        modifiedItem.setAmount(newAmount);  // Assuming setAmount method exists and updates the amount
        em.merge(modifiedItem);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public boolean isInList(String itemName) {
        EntityManager em = databaseConnector.createEntityManager();
        if (em.find(ShoppingListItem.class, itemName) != null) {
            return true;
        }
        return false;
    }
}
