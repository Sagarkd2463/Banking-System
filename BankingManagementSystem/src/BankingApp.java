import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.Class.forName;

public class BankingApp {
    //default parameters to connect to the databse
    private static final String url = "";
    private static final String username = "";
    private static final String password = "";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //loading driver for the database
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully!");
        }  catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            //establishing connection with the databse
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner =  new Scanner(System.in); //for user input
            User user = new User(connection, scanner); //a separate class for user details
            Accounts accounts = new Accounts(connection, scanner); //a separate class for account details
            AccountManager accountManager = new AccountManager(connection, scanner); //a separate class for doing transactions

            //variables required for invoking some methods for banking system
            String email;
            long account_number;

            while(true){ //looping until user exits
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();  //calling register method if user is new
                        break;
                    case 2:
                        email = user.login(); //if any user already exists
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exists(email)){ //checking availability of account in account class through email
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }
                            }

                            account_number = accounts.getAccount_number(email); //if account is present then invoke related functions
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) { //perform any transaction by choosing any one method from account manager class
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        }
                        else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3: //exiting from the banking system
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
