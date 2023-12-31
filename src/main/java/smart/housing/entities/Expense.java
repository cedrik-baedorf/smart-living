package smart.housing.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * This entity represents one row of table <i>Expenses</i>
 */
@Entity
@Table(name = "expenses")
@NamedQueries({
        @NamedQuery(
                name = Expense.FIND_ALL,
                query = """
                        SELECT expense FROM Expense expense
                        """
        )
})
public class Expense implements Serializable {

    /**
     * Name of named query to return all tasks
     */
    public static final String FIND_ALL = "Expense.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private int expenseId;

    /**
     * To which roommate is the task assigned
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "debtor_mapping",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "debtor_name")
    )
    private Set<User> debtors;

    @ManyToOne
    @JoinColumn(name = "creditor_name")
    private User creditor;

    @Column(name = "product", length = 32, nullable = false)
    private String product;

    @Column(name = "cost", nullable = false)
    private double cost;



    // Constructors, getters, and setters

    public Expense() {
        // Default constructor
    }

    public Expense(Set<User> debtors, User creditor, String product, double cost) {
        this.debtors = debtors;
        this.creditor = creditor;
        this.product = product;
        this.cost = cost;
    }

    // Getters and setters

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public Set<User> getDebtors() {
        return debtors;
    }

    public void setDebtors(Set<User> debtors) {
        this.debtors = debtors;
    }

    public void addDebtor(User roommate){
        debtors.add(roommate);
    }

    public void removeDebtor(User roommate) {

        debtors.remove(roommate);
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDebtorShare(User debtor) {
        int numberOfDebtors = debtors.size();
        if (numberOfDebtors > 0) {
            return cost / numberOfDebtors;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return
            creditor + " -> "
            + debtors.toString() + " for "
            + product + " at "
            + cost + "€";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass())) {
            Expense expense = (Expense) obj;
            return
                creditor != null && creditor.equals(expense.getCreditor()) &&
                debtors != null && debtors.equals(expense.getDebtors()) &&
                product != null && product.equals(expense.getProduct()) &&
                cost == expense.getCost();
        }
        return false;
    }

}
