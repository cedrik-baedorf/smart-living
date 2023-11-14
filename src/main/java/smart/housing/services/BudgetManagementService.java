package smart.housing.services;

import smart.housing.budget.model.expense.Expense;
import smart.housing.entities.Task;
import smart.housing.entities.User;

import java.util.List;

public interface BudgetManagementService {

    List<User> getCurrentUsers ();

    List<Task> getAllExpenses();


}
