package smart.housing.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.ShoppingListItem;
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingManagementServiceImplementationTest {

    private DatabaseConnector createMockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    @Test
    public void testCreateNewItem() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem newItem = new ShoppingListItem("Apple", 5.0, "kg");
        Mockito.when(entityManager.find(ShoppingListItem.class, "Apple")).thenReturn(null);

        service.create(newItem);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).persist(newItem);
        inOrder.verify(entityManager.getTransaction()).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void testGetItemList() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<ShoppingListItem> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.createNamedQuery(ShoppingListItem.FIND_ALL, ShoppingListItem.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(Arrays.asList(
                new ShoppingListItem("Apple", 5.0, "kg"),
                new ShoppingListItem("Banana", 2.0, "bunches")
        ));

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        List<ShoppingListItem> itemList = service.getItemList();

        assertNotNull(itemList);
        assertEquals(2, itemList.size());
        assertEquals("Apple",itemList.get(0).getItem());
        assertEquals("Banana",itemList.get(1).getItem());
        Mockito.verify(entityManager).close();
    }

    @Test
    public void testModifyItem() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem itemToBeModified = new ShoppingListItem("Apple", 5.0, "kg");
        Mockito.when(entityManager.find(ShoppingListItem.class, "Apple")).thenReturn(itemToBeModified);

        double newAmount = 10.0;
        service.modifyItem(itemToBeModified, newAmount);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).merge(itemToBeModified);
        inOrder.verify(entityManager.getTransaction()).commit();
        assertEquals(newAmount, itemToBeModified.getAmount());
        inOrder.verify(entityManager).close();
    }

    @Nested
    class TestConstructor {
        @Test
        public void testShoppingListItemConstructorWithParameters() {
            String itemName = "Apple";
            double amount = 5.0;
            String unit = "kg";
            ShoppingListItem item = new ShoppingListItem(itemName, amount, unit);

            assertEquals(itemName, item.getItem());
            assertEquals(amount, item.getAmount());
            assertEquals(unit, item.getUnit());
        }

        @Test
        public void testShoppingListItemConstructorWithNullItem() {
            assertThrows(NullPointerException.class, () -> new ShoppingListItem(null, 5.0, "kg"));
        }

        @Test
        public void testDefaultConstructor() {
            ShoppingListItem item = new ShoppingListItem();
            assertEquals("default", item.getItem());
        }
    }


    @Test
    public void testDeleteExistingItem() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem itemToBeDeleted = new ShoppingListItem("Apple", 5.0, "kg");
        Mockito.when(entityManager.find(ShoppingListItem.class, "Apple")).thenReturn(itemToBeDeleted);

        service.delete(itemToBeDeleted);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).remove(itemToBeDeleted);
        inOrder.verify(entityManager.getTransaction()).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void testDeleteItemNotFound() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem itemToBeDeleted = new ShoppingListItem("NonExistentItem", 5.0, "kg");
        Mockito.when(entityManager.find(ShoppingListItem.class, "NonExistentItem")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.delete(itemToBeDeleted)); // Assuming a RuntimeException is thrown when the item is not found.
    }

    @Test
    public void testCreateItemWithEmptyName() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem newItem = new ShoppingListItem("", 5.0, "kg");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(newItem),
                "Expected create() to throw IllegalArgumentException for empty item name, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Item name cannot be empty"));
    }

    @Test
    public void testGetItemListWhenEmpty() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<ShoppingListItem> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.createNamedQuery(ShoppingListItem.FIND_ALL, ShoppingListItem.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(Arrays.asList());

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        List<ShoppingListItem> itemList = service.getItemList();

        assertTrue(itemList.isEmpty());
        Mockito.verify(entityManager).close();
    }

    @Test
    public void testModifyItemUnavailable() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        ShoppingListItem itemToBeModified = new ShoppingListItem("NonExistentItem", 5.0, "kg");
        Mockito.when(entityManager.find(ShoppingListItem.class, "NonExistentItem")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.modifyItem(itemToBeModified, 10.0)); // Assuming a RuntimeException when item not found.
    }

    @Test
    public void testConcurrentModificationOnCreate() throws InterruptedException {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        Runnable task = () -> {
            ShoppingListItem item = new ShoppingListItem("Bread", 10.0, "loaves");
            service.create(item);
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(task);
        executor.submit(task);

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

        // Verify that the item "Bread" exists and its amount is correctly updated
        // This may require additional setup in your mock databaseConnector
    }

    @Test
    public void testCreateAndThenUpdateItem() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction transaction = Mockito.mock(EntityTransaction.class);

        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(entityManager.find(ShoppingListItem.class, "Milk")).thenReturn(null).thenReturn(new ShoppingListItem("Milk", 1.0, "liters"));

        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        // First, create the item
        service.create(new ShoppingListItem("Milk", 1.0, "liters"));
        // Then, update the same item
        service.create(new ShoppingListItem("Milk", 2.0, "liters"));

        // Verify the interactions and the final state of the item
        Mockito.verify(entityManager, Mockito.times(2)).find(ShoppingListItem.class, "Milk");
        Mockito.verify(entityManager, Mockito.times(1)).persist(Mockito.any(ShoppingListItem.class));
        Mockito.verify(entityManager, Mockito.times(1)).merge(Mockito.any(ShoppingListItem.class));
    }

    @Nested
    class testEdgeAmounts {
        @ParameterizedTest
        @ValueSource(doubles = { 1.0, 50.0, 99999.9 }) // Example set of valid amounts
        void testCreateItemWithVariousValidAmounts(double amount) {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

            ShoppingListItem item = new ShoppingListItem("Milk", amount, "liters");

            // Assert that no exception is thrown for valid amounts
            assertDoesNotThrow(() -> service.create(item));
        }

        @ParameterizedTest
        @ValueSource(doubles = { -1.0, -10000000, 100001.0 }) // Example set of valid amounts
        void testCreateItemWithVariousInvalidAmounts(double amount2) {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

            ShoppingListItem item = new ShoppingListItem("Milk", amount2, "l");

            // Assert that no exception is thrown for valid amounts
            assertThrows(IllegalArgumentException.class, () -> service.create(item));
        }

        @ParameterizedTest
        @ValueSource(doubles = { -1.0, 100000.0 }) // Example set of valid amounts
        void testModifyItemWithVariousInvalidAmounts(double newAmount) {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

            ShoppingListItem item = new ShoppingListItem("Milk", 10.0, "liters");

            // Assert that no exception is thrown for valid amounts
            assertThrows(IllegalArgumentException.class, () -> service.modifyItem(item, newAmount));
        }

    }

    @Test
    void testAddItemWithDifferentUnit() {
        // Mock setup
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        // Mocking existing item
        ShoppingListItem existingItem = new ShoppingListItem("Milk", 10.0, "liters");
        Mockito.when(entityManager.find(ShoppingListItem.class, "Milk")).thenReturn(existingItem);

        // Service setup
        ShoppingManagementService service = new ShoppingManagementServiceImplementation(databaseConnector);

        // First, add the item
        service.create(existingItem);

        // Then, try to add the same item with a different unit
        ShoppingListItem newItemWithDifferentUnit = new ShoppingListItem("Milk", 5.0, "gallons");

        // Assert that an exception is thrown for adding an item with a different unit
        assertThrows(IllegalArgumentException.class, () -> service.create(newItemWithDifferentUnit));
    }

}