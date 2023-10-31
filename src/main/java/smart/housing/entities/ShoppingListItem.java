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
    private double anzahl;

    /**
     * unit of the amount
     */
    @Column(name = "unit")
    private String einheit;

    public ShoppingListItem(String item, double anzahl, String einheit) {
        this.item = item;
        this.anzahl = anzahl;
        this.einheit = einheit;
    }

    public ShoppingListItem(String item) {
        if(item == null)
            throw new NullPointerException("There is no item");
        this.item = item;
    }

    public ShoppingListItem() {
        this("default");
    }

    public boolean checkIfItemIsInList (String item) {
        return true;
    }

    public String getItem() {
        return item;
    }

    public double getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(double anzahl) {
        this.anzahl = anzahl;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass())) {
            return obj.toString().equals(this.toString());
        }
        return false;
    }
}
