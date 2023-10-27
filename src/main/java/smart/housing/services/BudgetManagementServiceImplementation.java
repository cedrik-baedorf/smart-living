package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class BudgetManagementServiceImplementation implements BudgetManagementService{

    private DatabaseConnector databaseConnector;

    public BudgetManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public List<User> getCurrentUsers() {
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_ALL, User.class);
        return typedQuery.getResultList();
    }
}
