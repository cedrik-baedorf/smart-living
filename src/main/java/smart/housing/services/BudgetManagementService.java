package smart.housing.services;

import smart.housing.entities.DebtOverview;
import smart.housing.entities.Expense;
import smart.housing.entities.User;

import java.util.List;

public interface BudgetManagementService {

    List<Expense> getAllExpenses();

    /**
     * This method returns all {@link DebtOverview} objects where an amount is owed to or by the user
     * @param user {@link User} used for this calculation
     * @return {@link List} of all expenses matching that criterion
     */
    List<DebtOverview> getUserDebt(User user);

    void create(Expense expense);

    void delete(Expense expense);

}
