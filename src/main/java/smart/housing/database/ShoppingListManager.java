package smart.housing.database;

import javafx.collections.ObservableList;
import smart.housing.entities.ShoppingListItem;


public interface ShoppingListManager {

    /**
     * This method returns a list with all the items from the table Shopping List Items
     * @return Observable list containing the Items from the Shopping List Item table
     */
    public ObservableList<ShoppingListItem> getItems ();


}
