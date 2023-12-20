package smart.housing.entities;

import javax.persistence.*;

/**
 * This entity represents one row of table <i>Shopping List</i>
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
     * Unique item
     */
    @Id
    @Column(name = "item")
    private final String item;

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

    public ShoppingListItem(String item, double anzahl, String einheit) {
        if (item == null || anzahl == 0.0 || einheit == null) {
            throw new NullPointerException("Item, amount or einheit cannot be null");
        }

        this.item = item;
        this.amount = anzahl;
        this.unit = einheit;
    }

    public ShoppingListItem(String item) {
        if(item == null)
            throw new NullPointerException("There is no item");
        this.item = item;
    }

    public ShoppingListItem() {
        this("default");
    }

    public String getItem() {
        return item;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String einheit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ShoppingListItem other = (ShoppingListItem) obj;
        return item.equals(other.item);
    }
}
