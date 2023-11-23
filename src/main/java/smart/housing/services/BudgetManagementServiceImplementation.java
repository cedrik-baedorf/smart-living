package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.DebtOverview;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.entities.UserPair;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManagementServiceImplementation implements BudgetManagementService{

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
            .filter(debtOverview -> debtOverview.getCreditor().equals(user) || debtOverview.getDebtor().equals(user)).toList();

        List<DebtOverview> balancedDebts = balanceDebts(debtOverviews);

        List<DebtOverview> result = new LinkedList<>();

        for(DebtOverview debtOverview : balancedDebts.stream().filter(debt -> debt.getCreditor().equals(user)).toList()){
            result.add (new DebtOverview(debtOverview.getCreditor(),debtOverview.getDebtor(),
                    debtOverview.getAmount()-balancedDebts.stream().filter(debt -> debt.getCreditor().equals(debtOverview.getDebtor())).toList().get(0).getAmount()));
        }

        return result;
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

        /*
        Map<UserPair, Double> debtsMap = new HashMap<>();

        // Iterate through expenses and update debtsMap
        for (Expense expense : expenses) {
            User creditor = expense.getCreditor();
            double cost = expense.getCost();
            double share = cost / (expense.getDebitors().size());

            // Update debtor balances
            for (User debtor : expense.getDebitors()) {
                // Skip if the debtor is the same as the creditor
                if (!debtor.equals(creditor)) {
                    UserPair userPair = new UserPair(creditor, debtor);
                    UserPair reversedPair = new UserPair(debtor, creditor);

                    // Update the debt for the pair
                    debtsMap.put(userPair, debtsMap.getOrDefault(userPair, 0.0) + share);
                    debtsMap.put(reversedPair, debtsMap.getOrDefault(reversedPair, 0.0) - share);
                }
            }

            // Update creditor balance (they owe money)
            UserPair selfPair = new UserPair(creditor, creditor);
            debtsMap.put(selfPair, debtsMap.getOrDefault(selfPair, 0.0) - cost);
        }

        // Convert the debtsMap to DebtOverview objects
        List<DebtOverview> debtOverviews = debtsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != 0.0 && !entry.getKey().getFirstUser().equals(entry.getKey().getSecondUser())) // Skip zero amounts and same user as creditor and debtor
                .map(entry -> new DebtOverview(entry.getKey().getFirstUser(), entry.getKey().getSecondUser(), entry.getValue()))
                .collect(Collectors.toList());

        return debtOverviews;*/
    }

    private List<DebtOverview> balanceDebts (List<DebtOverview> unbalancedDebtOverview){
        Map<UserPair, Double> debtsMap = new HashMap<>();
        for(DebtOverview debt : unbalancedDebtOverview){
            UserPair userPair = new UserPair(debt.getCreditor(), debt.getDebtor());
            debtsMap.put(userPair, debtsMap.getOrDefault(userPair, 0.0) + debt.getAmount());

        }
        List<DebtOverview> balancedDebts = debtsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != 0.0 && !entry.getKey().getFirstUser().equals(entry.getKey().getSecondUser())) // Skip zero amounts and same user as creditor and debtor
                .map(entry -> new DebtOverview(entry.getKey().getFirstUser(), entry.getKey().getSecondUser(), entry.getValue()))
                .collect(Collectors.toList());

        return balancedDebts;
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
