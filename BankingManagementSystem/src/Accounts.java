import java.sql.*;
import java.util.Scanner;

public class Accounts {
    //instances of connection interface and scanner used for connecting to database
    private Connection connection;
    private Scanner scanner;

    //constructor for accounts class
    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email){
        if(!account_exists(email)){ //if email is not present into database previously then open a new account
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            scanner.nextLine(); //enter required details
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();

            try{ //setting all the details for opening a new bank account
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);

                //checking the rows after inserting required info into table
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows > 0){
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation Failed");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Account Already Exist!");
        }
        return 0;
    }

    //getting account no through email if its present into database
    public long getAccount_number(String email){
        //retrieving account number by passing email to get account info
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber(){
        try{
            //Using SQL statement for getting new account no or setting the default if there is no account into database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()){ //generating account no by iterating from top to get last account number
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1; //increment it by one for new account
            } else {
                return 10000100;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }
    public boolean account_exists(String email){
        //getting data through email to check it's existing or not
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){ //checking if it is available or not into database
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
