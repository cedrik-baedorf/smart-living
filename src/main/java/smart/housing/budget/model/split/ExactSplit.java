package smart.housing.budget.model.split;

import smart.housing.budget.model.User;

public class ExactSplit extends Split {

    public ExactSplit(User user, double amount) {
        super(user);
        this.amount = amount;
    }
}