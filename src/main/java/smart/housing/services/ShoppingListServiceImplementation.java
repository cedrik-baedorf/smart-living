package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.ShoppingListItem;
import javax.persistence.EntityManager;

public class ShoppingListServiceImplementation implements ShoppingListService{

    private final DatabaseConnector databaseConnector;
    private EntityManager em;

    public ShoppingListServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(ShoppingListItem shoppingListItem) {
        em = databaseConnector.createEntityManager();
        em.getTransaction().begin();
        ShoppingListItem existingItem = em.find(ShoppingListItem.class, shoppingListItem.getItem());

        if (existingItem != null) {
            double newAnzahl = existingItem.getAnzahl() + shoppingListItem.getAnzahl();
            existingItem.setAnzahl(newAnzahl);
            em.merge(existingItem);
        } else {
            System.out.println(shoppingListItem.getItem() + " " + shoppingListItem.getAnzahl());
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
}
