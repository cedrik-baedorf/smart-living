package smart.housing.services;

import smart.housing.controllers.BudgetManagementController;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.DebtOverview;
import smart.housing.entities.Expense;
import smart.housing.entities.User;
import smart.housing.exceptions.BudgetManagementServiceException;

import javax.persistence.EntityManager;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
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
            expense -> expense.getCreditor().equals(user) || expense.getDebtors().contains(user)
        ).toList();

        List<DebtOverview> debtOverviews = convertExpensesToDebtOverviews(expenses).stream()
            .filter(debtOverview -> debtOverview.creditor().equals(user) || debtOverview.debtor().equals(user)).toList();

        List<DebtOverview> balancedDebts = balanceDebtOverviews(debtOverviews);

        for(int i = 0; i < balancedDebts.size(); i++) {
            DebtOverview debtOverview = balancedDebts.get(i);
            if(debtOverview.debtor().equals(user))
                balancedDebts.set(i, debtOverview.inverseDebtOverview());
        }

        return balancedDebts;
    }

    private List<DebtOverview> convertExpensesToDebtOverviews(List<Expense> expenses) {
        List<DebtOverview> debtOverviews = new LinkedList<>();

        for(Expense expense : expenses) {
            User creditor = expense.getCreditor();
            double cost = expense.getCost();
            double share = new BigDecimal(cost / expense.getDebtors().size())
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();
            for (User debtor : expense.getDebtors().stream().filter(debtor -> ! debtor.equals(creditor)).toList()) {
                debtOverviews.add(new DebtOverview(creditor, debtor, share));
            }
        }

        return debtOverviews;
    }



    private List<DebtOverview> balanceDebtOverviews(List<DebtOverview> unbalancedDebtOverview){
        Map<UserPair, Double> debtsMap = new HashMap<>();
        for(DebtOverview debt : unbalancedDebtOverview){
            UserPair userPair = new UserPair(debt.creditor(), debt.debtor());
            if(! debtsMap.containsKey(userPair.inversePair()))
                debtsMap.put(userPair, debtsMap.getOrDefault(userPair, 0.0) + debt.amount());
            else
                debtsMap.put(userPair.inversePair(), debtsMap.getOrDefault(userPair.inversePair(), 0.0) - debt.amount());

        }
        return debtsMap.entrySet().stream()
            .filter(entry -> entry.getValue() != 0.0)
            .map(entry -> {
                DebtOverview debtOverview = new DebtOverview(entry.getKey().creditor(), entry.getKey().debtor(), entry.getValue());
                return entry.getValue() > 0 ? debtOverview : debtOverview.inverseDebtOverview();
            })
            .collect(Collectors.toList());
    }

    @Override
    public void create (Expense expense){
        if (expense == null || expense.getCost() == 0) {
            throw new IllegalArgumentException("Expense is null");
        }
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(expense);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete (Expense expense) {

        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        if (entityManager.find(Expense.class, expense.getExpenseId()) == null) {
            throw new IllegalArgumentException("Expense not found");
        }

        Expense expenseToBeRemoved = entityManager.find(Expense.class, expense.getExpenseId());
        entityManager.getTransaction().begin();
        entityManager.remove(expenseToBeRemoved);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void modify(Expense oldExpense, Expense updateExpense) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        if (oldExpense == null || updateExpense == null || updateExpense.getCost()==0) {
            throw new IllegalArgumentException("Modify Parameteres are not valid");
        }

        try {
            entityManager.getTransaction().begin();
            oldExpense = entityManager.merge(oldExpense);
            oldExpense.setProduct(updateExpense.getProduct());
            oldExpense.setCreditor(updateExpense.getCreditor());
            oldExpense.setCost(updateExpense.getCost());
            oldExpense.setDebtors(updateExpense.getDebtors());

            entityManager.getTransaction().commit();
        } catch (Exception e){
            e.getStackTrace();
        }
        entityManager.close();
    }

    @Override
    public void sendReminderMail(DebtOverview selectedDebt, String senderFirstName, String senderLastName){

        try {
            String debtorFirstName = selectedDebt.debtor().getFirstName();
            String debtorLastName = selectedDebt.debtor().getLastName();
            String creditorFirstName = selectedDebt.creditor().getFirstName();
            String debtorEmail = selectedDebt.debtor().getEmail();
            String subject = "You owe money!";

            // Include debt amount in the email body
            double debtAmount = selectedDebt.getAmount();
            String body = "Dear " + debtorFirstName + " " + debtorLastName +
                    ",\n\nYou owe €" + String.format("%.2f", Math.abs(debtAmount)) +
                    " to " + creditorFirstName +
                    ". Please settle the amount at your earliest convenience.\n\nSincerely,\nYour Budget Management System\n"
                    + "on behalf of: " + senderFirstName + " " + senderLastName;

            // Encode the subject and body parameters
            String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8).replace("+", "%20");
            String encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8).replace("+", "%20");

            // Create a mailto URI with properly encoded subject and body
            URI mailtoUri = new URI("mailto:" + debtorEmail + "?subject=" + encodedSubject + "&body=" + encodedBody);

            // Open the default email client
            Desktop desktop = Desktop.getDesktop();
            desktop.mail(mailtoUri);

        } catch (IOException | URISyntaxException e) {
            // Display error message
            e.printStackTrace();
        }
    }

    @Override
    public void validateAndCreateExpense(String product, String costInput, User creditor, Set<User> debtors) throws BudgetManagementServiceException {
        if (product == null || product.trim().isEmpty()) {
            throw new BudgetManagementServiceException("Please enter a product name.");
        }

        if (!costInput.matches("\\d+(\\.\\d{1,2})?")) {
            throw new BudgetManagementServiceException("Invalid cost format. Please enter a valid amount.");
        }

        double cost;
        try {
            cost = Double.parseDouble(costInput);
        } catch (NumberFormatException e) {
            throw new BudgetManagementServiceException("Invalid cost. Please enter a numeric value.");
        }

        if (creditor == null) {
            throw new BudgetManagementServiceException("Creditor is not selected.");
        }

        if (debtors.isEmpty()) {
            throw new BudgetManagementServiceException("Please select at least one debtor.");
        }

        if (cost <= 0) {
            throw new BudgetManagementServiceException("Please provide a positive amount.");
        }

        create(new Expense(debtors, creditor, product, cost));
    }

}
