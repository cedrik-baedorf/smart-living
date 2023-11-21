package smart.housing.entities;

public class DebtOverview {
    private final User creditor;
    private final User debtor;
    private final double amount;

    public DebtOverview(User creditor, User debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

    public User getCreditor() {
        return creditor;
    }

    public User getDebtor() {
        return debtor;
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return String.format("%.2f €", amount);
    }
}

