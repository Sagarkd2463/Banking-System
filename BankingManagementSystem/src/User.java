import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    //getting instances of the connection interface and scanner when connection was established
    private Connection connection;
    private Scanner scanner;

    //constructor for the user class
    public User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){ //fill required details for registering
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if(user_exists(email)){ //checking user if it is already present into database
            System.out.println("User Already Exists for this Email Address!!");
            return;
        } else {
            //inserting a new user
            String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";
            try { //parameterized statement with placeholder "?" for hiding some details
                PreparedStatement preparedStatement = connection.prepareStatement(register_query);
                preparedStatement.setString(1, full_name); //setting all the required values
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                int affectedRows = preparedStatement.executeUpdate();

                if(affectedRows > 0){ //checking rows affected into table
                    System.out.println("Registration Successfull!");
                } else {
                    System.out.println("Registration Failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String login(){ //taking email & password for logging
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";

        //retrieving required data from the database
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email); //passing email
            preparedStatement.setString(2, password); //passing password
            ResultSet resultSet = preparedStatement.executeQuery(); //a pointer for iterating into table

            if(resultSet.next()){ //sending email back if it encounters or otherwise null i.e. not registered as of now
                return email;
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exists(String email){ //returning true or false if user is already there in database
        String query = "SELECT * FROM user WHERE email = ?"; //reading table for data required
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery(); //executing select query by checking email if it's present or not
            if(resultSet.next()){
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
