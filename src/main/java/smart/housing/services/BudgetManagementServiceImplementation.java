package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.entities.Task;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class BudgetManagementServiceImplementation implements BudgetManagementService{

    private final DatabaseConnector DATABASE_CONNECTOR;

    public BudgetManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.DATABASE_CONNECTOR = databaseConnector;
    }

    @Override
    public List<User> getCurrentUsers() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_ALL, User.class);
        return typedQuery.getResultList();
    }

    @Override
    public List<Expense> getAllExpenses(){
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        List<Expense> expenseList = entityManager.createNamedQuery(Expense.FIND_ALL, Expense.class).getResultList();
        entityManager.close();
        return expenseList;
    }
}
