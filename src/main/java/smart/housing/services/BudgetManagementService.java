package smart.housing.services;

import smart.housing.entities.Expense;
import smart.housing.entities.User;

import java.util.List;

public interface BudgetManagementService {

    List<User> getCurrentUsers ();

    List<Expense> getAllExpenses();

    void create(Expense expense);


}
