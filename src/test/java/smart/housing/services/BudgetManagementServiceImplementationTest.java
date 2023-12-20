package smart.housing.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.entities.DebtOverview;

import javax.persistence.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetManagementServiceImplementationTest {

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
    public void testCreateExpense() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        Expense newExpense = new Expense();
        service.create(newExpense);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).persist(newExpense);
        inOrder.verify(entityManager.getTransaction()).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void testGetAllExpenses() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<Expense> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.createNamedQuery(Expense.FIND_ALL, Expense.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(Collections.singletonList(new Expense()));

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        List<Expense> expenses = service.getAllExpenses();

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
        Mockito.verify(entityManager).close();
    }

    @Test
    public void testDeleteExpense() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        Expense expense = new Expense();
        Mockito.when(entityManager.find(Expense.class, expense.getExpenseId())).thenReturn(expense);

        service.delete(expense);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).remove(expense);
        inOrder.verify(entityManager.getTransaction()).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void testModifyExpense() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();
        EntityManager entityManager = databaseConnector.createEntityManager();

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        Expense existingExpense = new Expense();
        Expense updatedExpense = new Expense();

        Mockito.when(entityManager.find(Expense.class, existingExpense.getExpenseId())).thenReturn(existingExpense);

        service.modify(existingExpense, updatedExpense);

        InOrder inOrder = Mockito.inOrder(entityManager, entityManager.getTransaction());
        inOrder.verify(entityManager.getTransaction()).begin();
        inOrder.verify(entityManager).merge(existingExpense);
        inOrder.verify(entityManager.getTransaction()).commit();
        inOrder.verify(entityManager).close();
    }

    @Nested
    class UserDebtTests {

        @Test
        public void testGetUserDebtWithNoExpenses() {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            EntityManager entityManager = databaseConnector.createEntityManager();

            Mockito.when(entityManager.createNamedQuery(Expense.FIND_ALL, Expense.class))
                    .thenReturn(Mockito.mock(TypedQuery.class));

            BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

            User user = new User(); // Assuming a default User object exists
            List<DebtOverview> debts = service.getUserDebt(user);

            assertTrue(debts.isEmpty());
        }

        // Additional tests for different scenarios in getUserDebt
    }

    @Nested
    class EdgeCaseTests {
        @Test
        public void testCreateExpenseWithNull() {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

            assertThrows(IllegalArgumentException.class, () -> service.create(null));
        }

        @Test
        public void testDeleteNonExistentExpense() {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            EntityManager entityManager = databaseConnector.createEntityManager();

            BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

            Expense nonExistentExpense = new Expense();
            Mockito.when(entityManager.find(Expense.class, nonExistentExpense.getExpenseId())).thenReturn(null);

            assertThrows(RuntimeException.class, () -> service.delete(nonExistentExpense));
        }

        @Test
        public void testModifyExpenseWithNull() {
            DatabaseConnector databaseConnector = createMockDatabaseConnector();
            BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

            Expense existingExpense = new Expense();

            assertThrows(IllegalArgumentException.class, () -> service.modify(existingExpense, null));
        }
    }

    // Additional tests for other methods and edge cases
}