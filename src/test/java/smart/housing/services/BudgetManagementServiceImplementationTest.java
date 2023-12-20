package smart.housing.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.entities.DebtOverview;
import smart.housing.exceptions.BudgetManagementServiceException;

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

        Mockito.when(entityManager.merge(Mockito.any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    @Test
    public void testCreateExpense() {
        DatabaseConnector databaseConnector = createMockDatabaseConnector();

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        Expense expense = new Expense(
                Set.of(new User("debtor 1")),
                new User("creditor"),
                "Hotel",
                250
        );

        assertDoesNotThrow(() -> service.create(expense));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(expense);
        inOrder.verify(entityTransaction).commit();
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

        BudgetManagementService service = new BudgetManagementServiceImplementation(databaseConnector);

        Expense expense = new Expense(
            Set.of(new User("debtor 1")),
            new User("creditor"),
            "Hotel",
            250
        );

        Expense modifiedExpense = new Expense(
                Set.of(new User("debtor 2"), new User("debtor 3")),
                new User("creditor"),
                "Hotel",
                250
        );

        service.modify(expense, modifiedExpense);

        assertEquals(expense, modifiedExpense);
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

            User user = new User();
            List<DebtOverview> debts = service.getUserDebt(user);

            assertTrue(debts.isEmpty());
        }

    }

    @Nested
    class ValidateAndCreateExpenseTests {
        private DatabaseConnector databaseConnector;
        private BudgetManagementServiceImplementation service;
        private User mockCreditor;
        private Set<User> mockDebtors;

        @BeforeEach
        public void setUp() {
            databaseConnector = createMockDatabaseConnector();
            service = new BudgetManagementServiceImplementation(databaseConnector);
            mockCreditor = Mockito.mock(User.class);
            mockDebtors = new HashSet<>(Arrays.asList(Mockito.mock(User.class)));
        }

        @Test
        public void testValidExpenseCreation() {
            assertDoesNotThrow(() ->
                    service.validateAndCreateExpense("Product", "100", mockCreditor, mockDebtors));
        }

        @Test
        public void testInvalidProductName() {
            assertThrows(BudgetManagementServiceException.class, () ->
                    service.validateAndCreateExpense("", "100", mockCreditor, mockDebtors));
        }
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
}