package smart.housing.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingListItemTest {

    @Test
    void testEqualsWithEqualObjects() {
        ShoppingListItem item1 = new ShoppingListItem("Milk", 1.0, "Liters");
        ShoppingListItem item2 = new ShoppingListItem("Milk", 1.0, "Liters");

        assertEquals(item1, item2, "Items should be equal");
    }

    @Test
    void testEqualsWithDifferentObjects() {
        ShoppingListItem item1 = new ShoppingListItem("Milk", 1.0, "Liters");
        ShoppingListItem item2 = new ShoppingListItem("Bread", 2.0, "Pieces");

        assertNotEquals(item1, item2, "Items should not be equal");
    }

    @Test
    void testEqualsWithNull() {
        ShoppingListItem item = new ShoppingListItem("Milk", 1.0, "Liters");

        assertNotEquals(item, null, "Item should not be equal to null");
    }
}