package smart.housing.entities;

import javax.persistence.*;

/**
 * This entity represents one row of table <i>Shopping_List_Items</i>
 */
@Entity
@Table(name= "Shopping_List_Items")
@NamedQueries({
        @NamedQuery(
                name = ShoppingListItem.FIND_ALL,
                query = "SELECT shoppingListItem FROM ShoppingListItem shoppingListItem"
        )
})
public class ShoppingListItem {

    /**
     * Name of named query to return all Shopping List Items
     */
    public static final String FIND_ALL = "ShoppingListItem.findAll";

    /**
     * Unique itemName
     */
    @Id
    @Column(name = "item")
    private final String itemName;

    /**
     * Amount of the item
     */
    @Column(name = "amount")
    private double amount;

    /**
     * unit of the amount
     */
    @Column(name = "unit")
    private String unit;

    /**
     * Default constructor for an object of this class setting the item to 'default'.
     */
    public ShoppingListItem() {
        this("default");
    }

    /**
     * Overloaded constructor accepting a value for the <code>itemName</code> property.
     * @param itemName name of the item
     */
    public ShoppingListItem(String itemName) {
        if(itemName == null)
            throw new NullPointerException("There is no item");
        this.itemName = itemName;
    }

    /**
     * Overloaded constructor accepting a value for the
     * <code>itemName</code>, <code>anzahl</code>, and <code>einheit</code> property.
     * @param itemName name of the item
     * @param amount amount of the item
     * @param unit unit of the item
     */
    public ShoppingListItem(String itemName, double amount, String unit) {
        if (itemName == null || amount == 0.0 || unit == null) {
            throw new NullPointerException("Item, amount or einheit cannot be null");
        }

        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Getter for the <code>item</code> attribute.
     * @return name of the item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Getter for the <code>amount</code> attribute.
     * @return amount of the item
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter for the <code>item</code> attribute.
     * @param amount amount of the item
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Getter for the <code>unit</code> attribute.
     * @return unit of the item
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter for the <code>unit</code> attribute.
     * @param unit unit of the item
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return itemName + ": " + amount + " " + unit;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass())) {
            ShoppingListItem item = (ShoppingListItem) obj;
            return
                itemName != null && itemName.equals(item.getItemName()) &&
                amount == item.getAmount() &&
                unit != null && unit.equals(item.getUnit());
        }
        return false;
    }
}
