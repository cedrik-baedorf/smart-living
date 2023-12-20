package smart.housing.entities;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ExpenseTest {

    @Test
    public void testExpenseCreationWithNoArguments() {
        Expense expense = new Expense();
        assertNotNull(expense);
    }

    @Test
    public void testExpenseCreationWithArguments() {
        Set<User> debtors = new HashSet<>();
        User creditor = new User("creditor");
        String product = "Product";
        double cost = 100.0;

        debtors.add(new User("debtor1"));
        debtors.add(new User("debtor2"));

        Expense expense = new Expense(debtors, creditor, product, cost);

        assertEquals(debtors, expense.getDebtors());
        assertEquals(creditor, expense.getCreditor());
        assertEquals(product, expense.getProduct());
        assertEquals(cost, expense.getCost());
    }

    @Test
    public void testGetDebtorShareWithDebtors() {
        Set<User> debtors = new HashSet<>();
        User creditor = new User("creditor");
        String product = "Product";
        double cost = 100.0;

        User debtor1 = new User("debtor1");
        User debtor2 = new User("debtor2");
        debtors.add(debtor1);
        debtors.add(debtor2);

        Expense expense = new Expense(debtors, creditor, product, cost);

        double expectedShare = cost / debtors.size();
        assertEquals(expectedShare, expense.getDebtorShare(debtor1));
        assertEquals(expectedShare, expense.getDebtorShare(debtor2));
    }

    @Test
    public void testGetDebtorShareWithoutDebtors() {
        Set<User> debtors = new HashSet<>();
        User creditor = new User("creditor");
        String product = "Product";
        double cost = 100.0;

        Expense expense = new Expense(debtors, creditor, product, cost);

        assertEquals(0.0, expense.getDebtorShare(new User("someDebtor")));
    }

    @Test
    public void testAddAndRemoveDebtor() {
        Set<User> debtors = new HashSet<>();
        User creditor = new User("creditor");
        String product = "Product";
        double cost = 100.0;

        Expense expense = new Expense(debtors, creditor, product, cost);
        User newDebtor = new User("newDebtor");

        expense.addDebtor(newDebtor);
        assertTrue(expense.getDebtors().contains(newDebtor));

        expense.removeDebtor(newDebtor);
        assertFalse(expense.getDebtors().contains(newDebtor));
    }

}
