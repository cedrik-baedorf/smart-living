package smart.housing.services;

import smart.housing.entities.DebtOverview;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.exceptions.BudgetManagementServiceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public interface BudgetManagementService {

    List<Expense> getAllExpenses();

    /**
     * This method returns all {@link DebtOverview} objects where an amount is owed to or by the user
     * @param user {@link User} used for this calculation
     * @return {@link List} of all expenses matching that criterion
     */
    List<DebtOverview> getUserDebt(User user);

    /**
     * This method takes an object of class <code>{@link Expense}</code> and persists it in the database, if
     *  it is not equal to <code>null</code>,
     *  and its <i>cost</i> is not equal to zero
     * Otherwise, this method will throw a {@link IllegalArgumentException}
     * @param expense expense to be persisted in the database
     */
    void create(Expense expense);

    /**
     * This method deletes an object of class <code>{@link Expense}</code> from the database.
     * The <code>{@link Expense}</code> to be deleted is determined by the <code>expense</code>
     * parameter provided in the method signature.
     * If not present, this method will throw a {@link IllegalArgumentException}
     * @param expense expense that shall be deleted
     */
    void delete(Expense expense);

    /**
     * This method modifies an object of class <code>{@link Expense}</code> in the database.
     * The <code>{@link Expense}</code> to be modified is determined by the <code>expense</code>
     * parameter provided in the method signature. The updated expense data is taken from the
     * <code>modifiedExpense</code> parameter. Every attribute from <code>modifiedExpense</code> will be
     * transferred to <code>expense</code> and persisted to the database except the <code>id</code>.
     * Otherwise, this method will throw a {@link IllegalArgumentException}.
     * @param expense expense that shall be modfieid
     * @param modifiedExpense expense containing the updated user data
     */
    void modify(Expense expense, Expense modifiedExpense);

    void sendReminderEmail(User sender, DebtOverview expense) throws IOException, URISyntaxException;

    void validateAndCreateExpense(String product, String costInput, User creditor, Set<User> debtors) throws BudgetManagementServiceException;



}
