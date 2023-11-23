package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.DebtOverview;
import smart.housing.entities.Expense;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManagementServiceImplementation implements BudgetManagementService{

    private record UserPair(User creditor, User debtor) {

        private UserPair inversePair() { return new UserPair(debtor, creditor); }

    }

    private final DatabaseConnector DATABASE_CONNECTOR;

    public BudgetManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.DATABASE_CONNECTOR = databaseConnector;
    }

    @Override
    public List<Expense> getAllExpenses(){
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        List<Expense> expenseList = entityManager.createNamedQuery(Expense.FIND_ALL, Expense.class).getResultList();
        entityManager.close();
        return expenseList;
    }

    @Override
    public List<DebtOverview> getUserDebt(User user){
        List<Expense> expenses = getAllExpenses().stream().filter(
            expense -> expense.getCreditor().equals(user) || expense.getDebitors().contains(user)
        ).toList();

        List<DebtOverview> debtOverviews = convertExpensesToDebtOverview(expenses).stream()
            .filter(debtOverview -> debtOverview.creditor().equals(user) || debtOverview.debtor().equals(user)).toList();

        List<DebtOverview> balancedDebts = balanceDebts(debtOverviews);

        for(int i = 0; i < balancedDebts.size(); i++) {
            DebtOverview debtOverview = balancedDebts.get(i);
            if(debtOverview.debtor().equals(user))
                balancedDebts.set(i, debtOverview.inverseDebtOverview());
        }

        return balancedDebts;
    }


    private List<DebtOverview> convertExpensesToDebtOverview(List<Expense> expenses) {
        List<DebtOverview> debtOverviews = new LinkedList<>();

        for(Expense expense : expenses) {
            User creditor = expense.getCreditor();
            double cost = expense.getCost();
            double share = cost / (expense.getDebitors().size());
            for (User debtor : expense.getDebitors().stream().filter(debtor -> ! debtor.equals(creditor)).toList()) {
                debtOverviews.add(new DebtOverview(creditor, debtor, share));
            }
        }

        return debtOverviews;
    }

    private List<DebtOverview> balanceDebts (List<DebtOverview> unbalancedDebtOverview){
        Map<UserPair, Double> debtsMap = new HashMap<>();
        for(DebtOverview debt : unbalancedDebtOverview){
            UserPair userPair = new UserPair(debt.creditor(), debt.debtor());
            if(! debtsMap.containsKey(userPair.inversePair()))
                debtsMap.put(userPair, debtsMap.getOrDefault(userPair, 0.0) + debt.amount());
            else
                debtsMap.put(userPair.inversePair(), debtsMap.getOrDefault(userPair.inversePair(), 0.0) - debt.amount());

        }
        return debtsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != 0.0 && !entry.getKey().creditor().equals(entry.getKey().debtor())) // Skip zero amounts and same user as creditor and debtor
                .map(entry -> new DebtOverview(entry.getKey().creditor(), entry.getKey().debtor(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void create (Expense expense){
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(expense);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete (Expense expense) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        Expense expenseToBeRemoved = entityManager.find(Expense.class, expense.getExpenseId());
        entityManager.getTransaction().begin();
        entityManager.remove(expenseToBeRemoved);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
