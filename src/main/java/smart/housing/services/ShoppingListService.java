package smart.housing.services;

import javafx.collections.ObservableList;
import smart.housing.entities.ShoppingListItem;

public interface ShoppingListService {

    /**
     * This method takes an object of class <code>ShoppingListItem</code> and persists it in the database, if
     *  its <i>item</i> is not equal to <code>null</code>,
     *  its <i>item</i> does not exist in the database
     *  its <i>amount</i> is not equal to <code>null</code>
     *  its <i>unit</i> is not equal to <code>null</code>
     * Otherwise, this method will throw a <code>Exception</code>
     * @param shoppingListItem to be persisted in the database
     */
    void create(ShoppingListItem shoppingListItem);

    /**
     * This method returns a List with all Items from the database table
     * @return <code>ObservableList<ShoppingListItem></code> the List containing all Items from the database table
     */
    ObservableList<ShoppingListItem> readList ();
}
