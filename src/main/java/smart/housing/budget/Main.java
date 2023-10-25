package smart.housing.budget;

import smart.housing.budget.model.ExpenseType;
import smart.housing.budget.model.Type;
import smart.housing.budget.model.User;
import smart.housing.budget.model.expense.ExpenseData;
import smart.housing.budget.model.split.EqualSplit;
import smart.housing.budget.model.split.ExactSplit;
import smart.housing.budget.model.split.PercentSplit;
import smart.housing.budget.model.split.Split;
import smart.housing.budget.repository.ExpenseRepository;
import smart.housing.budget.service.SplitWiseService;
import smart.housing.budget.service.UserService;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Input Sample Users
        User user1 = new User(1, "ivan","ivan@luzie.de","9890098900");
        User user2 = new User(2, "tom","u2@gmail.com","9999999999");
        User user3 = new User(3, "anna","u3@gmail.com","9898989899");
        User user4 = new User(4, "cedrik","u4@gmail.com","8976478292");

        // Adding Expenses
        ExpenseRepository expenseRepository = new ExpenseRepository();
        UserService userService = new UserService(expenseRepository);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);
        SplitWiseService service = new SplitWiseService(expenseRepository);

        while (true) {
            Scanner scan = new Scanner(System.in);
            String[] commands = scan.nextLine().split(" ");
            Type type = Type.of(commands[0]);
            switch (type){
                case EXPENSE:
                    String userName = commands[1];
                    double amountSpend = Double.parseDouble(commands[2]);
                    int totalMembers = Integer.parseInt(commands[3]);
                    List<Split> splits = new ArrayList<>();
                    int expenseIndex = 3 + totalMembers + 1;
                    ExpenseType expense = ExpenseType.of(commands[expenseIndex]);
                    switch (expense){
                        case EQUAL:
                            for (int i = 0; i < totalMembers; i++) {
                                splits.add(new EqualSplit(userService.getUser(commands[4+i])));
                            }
                            service.addExpense(
                                    ExpenseType.EQUAL, amountSpend, userName, splits, new ExpenseData("GoaFlight")
                            );
                            break;
                        case EXACT:
                            for (int i = 0; i < totalMembers; i++) {
                                splits.add(
                                        new ExactSplit(
                                                userService.getUser(commands[4+i]),
                                                Double.parseDouble(commands[expenseIndex+i+1]))
                                );
                            }
                            service.addExpense(
                                    ExpenseType.EXACT, amountSpend, userName, splits, new ExpenseData("CabTickets")
                            );

                            break;
                        case PERCENT:
                            for (int i = 0; i < totalMembers; i++) {
                                splits.add(
                                        new PercentSplit(
                                                userService.getUser(commands[4+i]),
                                                Double.parseDouble(commands[expenseIndex+i+1]))
                                );
                            }
                            service.addExpense(
                                    ExpenseType.PERCENT, amountSpend, userName, splits, new ExpenseData("Dinner")
                            );
                            break;
                    }
                    break;
                case SHOW:
                    if(commands.length == 1)
                        service.showBalances();
                    else
                        service.showBalance(commands[1]);
                    break;
                case QUIT:
                     System.out.println("Quiting...");
                     return;
                case REMIND:
                    if (commands.length >= 2) {
                        String username = commands[1];

                        // Check if the user with the specified username exists and has an email
                        User user = userService.getUser(username);
                        if (user != null) {
                            String email = user.getEmail();

                            // Define the email subject and message
                            String subject = "YOU ARE IN TROUBLE!!";
                            String message = "Hello " + username + ", \n\nplease check youre balance in the budget book as soon as possible!\n\nUnfriendly Greetings!";

                            try {
                                // URL encode the subject and message with space represented as %20
                                String encodedSubject = URLEncoder.encode(subject, "UTF-8").replace("+", "%20");
                                String encodedMessage = URLEncoder.encode(message, "UTF-8").replace("+", "%20");

                                // Create a mailto URI with the encoded subject and message
                                String mailtoURI = "mailto:" + email + "?subject=" + encodedSubject + "&body=" + encodedMessage;

                                // Create a URI object from the mailto URI string
                                URI uri = new URI(mailtoURI);

                                // Use the Desktop class to open the default email program with the mailto URI
                                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                                    Desktop.getDesktop().mail(uri);
                                    System.out.println("Email program opened with the reminder.");
                                } else {
                                    System.out.println("Desktop mail action is not supported.");
                                }
                            } catch (Exception e) {
                                System.err.println("Error opening the email program: " + e.getMessage());
                            }
                        } else {
                            System.out.println("User with username " + username + " not found or has no email.");
                        }
                    } else {
                        System.out.println("Invalid REMIND command. Usage: REMIND <username>");
                    }
                    break;
                default:
                    System.out.println("No Expected Argument Found");
                    break;
            }

        }
    }
}
