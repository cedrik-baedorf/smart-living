package smart.housing.entities;

public record DebtOverview(User creditor, User debtor, double amount) {

    public DebtOverview(User creditor, User debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

    public DebtOverview inverseDebtOverview() {
        return new DebtOverview(debtor, creditor, - amount);
    }

    /**
     * Getter for <code>creditor</code> necessary for javafx {@link javafx.scene.control.TableView}
     * @return creditor of this debt
     */
    public User getCreditor() {
        return creditor;
    }

    /**
     * Getter for <code>creditor</code> necessary for javafx {@link javafx.scene.control.TableView}
     * @return debtor of this debt
     */
    public User getDebtor() {
        return debtor;
    }

    /**
     * Getter for <code>creditor</code> necessary for javafx {@link javafx.scene.control.TableView}
     * @return amount of this debt
     */
    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return String.format("%.2f €", amount);
    }
}

