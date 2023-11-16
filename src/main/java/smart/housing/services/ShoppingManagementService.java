package smart.housing.services;

import smart.housing.entities.ShoppingListItem;

import java.util.List;

public interface ShoppingManagementService {

    /**
     * This method takes an object of class <code>ShoppingListItem</code> and persists it in the database, if
     * its <i>item</i> is not equal to <code>null</code>,
     * its <i>item</i> does not exist in the database
     * its <i>amount</i> is not equal to <code>null</code>
     * its <i>unit</i> is not equal to <code>null</code>
     * Otherwise, this method will throw a <code>Exception</code>
     *
     * @param shoppingListItem to be persisted in the database
     */
    void create(ShoppingListItem shoppingListItem);

    /**
     * This method takes an object of class <code>ShoppingListItem</code> and removes it from the database, if
     *
     * @param shoppingListItem to be removed from the database
     */
    void delete(ShoppingListItem shoppingListItem);

    /**
     * This method takes an object of class <code>ShoppingListItem</code> and removes it from the database, if
     *
     * @return List<ShoppingListItem>
     */
    List<ShoppingListItem> getItemList();


    /**
     * This method takes an object of class <code>ShoppingListItem</code> and modifies the amount, if
     * the amount != null
     *
     * @param shoppingListItem to be modified in the database
     * @param newAmount that is addded to the current amount
     */
    void modifyItem(ShoppingListItem shoppingListItem, Double newAmount);

}