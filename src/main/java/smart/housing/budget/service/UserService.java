package smart.housing.budget.service;

import smart.housing.budget.model.User;
import smart.housing.budget.repository.ExpenseRepository;

public class UserService {
    ExpenseRepository expenseRepository;

    public UserService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public void addUser(User user){
        expenseRepository.addUser(user);
    }
    public User getUser(String userName){
        return expenseRepository.getUser(userName);
    }
}
