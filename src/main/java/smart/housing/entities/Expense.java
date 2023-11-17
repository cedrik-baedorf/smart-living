package smart.housing.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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
            name = "debitor_mapping",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "debitor_name")
    )
    private Set<User> debitors;

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

    public Expense(Set<User> debitors, User creditor, String product, double cost) {
        this.debitors = debitors;
        this.creditor = creditor;
        // this.creditorUser = creditorUser;
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

    public Set<User> getDebitors() {
        return debitors;
    }

    public void setDebitors(Set<User> debitors) {
        this.debitors = debitors;
    }

    public void addDebitor(User roommate){
        debitors.add(roommate);
    }

    public void removeDebitor(User roommate) {
        debitors.remove(roommate);
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    /*
    public User getCreditorUser() {
        return creditorUser;
    }

    public void setCreditorUser(User creditorUser) {
        this.creditorUser = creditorUser;
    }
    */

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
