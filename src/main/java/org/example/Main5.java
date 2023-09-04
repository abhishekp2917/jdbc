/*
   Code to demonstrate SQL injection and how PreparedStatement is secured from it
*/

package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main5 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String USERNAME = "root";
    private static String PASSWORD = "password@123";
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Login using simple Statement");
            System.out.println("2. Login using PreparedStatement");
            System.out.println("3. Exit");
            System.out.println("_________________________________________________\n");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 : {
                    loginUsingStatement();
                    break;
                }
                case 2 : {
                    loginUsingPreparedStatement();
                    break;
                }
                case 3 : {
                    exit = true;
                    break;
                }
                default: {
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
                }
            }
        }
        sc.close();
    }

    private static void loginUsingStatement() {
        // taking username and password as an input from user
        System.out.print("Enter Username : ");
        sc.nextLine();
        String username = sc.nextLine();
        System.out.print("Enter Password : ");
        String password = sc.nextLine();
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // using simple 'Statement' object to execute the SQL query which is prone to SQL Injection attack
            // since the query is compiled at the time of execution, if the user injects any SQL specific character
            // the SQL query structure might change which will eventually make query behaving differently on execution
            try(Statement statement = connection.createStatement()) {
                String sqlQuery = String.format("SELECT * FROM users WHERE Username='%s' AND Password='%s'", username, password);
                System.out.println(String.format("SQL query : %s", sqlQuery));
                // compiling the query once user enter the values, which makes it vulnerable for SQL injection
                ResultSet result = statement.executeQuery(sqlQuery);
                if(result.next()) System.out.print("Login successfully!");
                else System.out.print("Invalid Username or Password");
            }
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    private static void loginUsingPreparedStatement() {
        System.out.print("Enter Username : ");
        sc.nextLine();
        String username = sc.nextLine();
        System.out.print("Enter Password : ");
        String password = sc.nextLine();
        String sqlQuery = String.format("SELECT * FROM users WHERE Username=? AND Password=?", username, password);
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // since in 'PreparedStatement', the query gets compiled before injecting the parameters value due to
            // which the structure of query will remain intact
            // when the user enters any SQL specific values, since the query structure is predefined, those values
            // will be taken as parameter values and will not alter the query. This way it is secure form SQL injection
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // entering parameters value once the query has been compiled
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet result = preparedStatement.executeQuery();
                if(result.next()) System.out.print("Login successfully!");
                else System.out.print("Invalid Username or Password");
            }
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }
}
