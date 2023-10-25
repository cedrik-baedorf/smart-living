package smart.housing.budget.service;

import smart.housing.budget.model.ExpenseType;
import smart.housing.budget.model.expense.ExpenseData;
import smart.housing.budget.model.split.Split;
import smart.housing.budget.repository.ExpenseRepository;

import java.util.List;

public class SplitWiseService {
    ExpenseRepository expenseRepository;

    public SplitWiseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(ExpenseType expenseType, double amount,
                           String expensePaidBy, List<Split > splits, ExpenseData expenseData) {
        expenseRepository.addExpense(expenseType,amount,expensePaidBy,splits,expenseData);
    }

    public void showBalance(String userName) {
        List<String> balances = expenseRepository.getBalance(userName);
        if (balances.isEmpty()) {
            System.out.println("No balances");
        } else {
            for(String balance: balances){
                System.out.println(balance);
            }
        }
    }

    public void showBalances(){
        List<String> balances = expenseRepository.getBalances();
        if (balances.isEmpty()) {
            System.out.println("No balances");
        } else {
            for(String balance: balances){
                System.out.println(balance);
            }
        }
    }
}
