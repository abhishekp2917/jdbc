/*
   Code to demonstrate how to execute sql queries in batches using batch updates with different Statement object
*/

package org.example;

import org.example.utils.DisplayResultSet;

import java.sql.*;
import java.util.Scanner;

public class Main7 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String USERNAME = "root";
    private static String PASSWORD = "password@123";
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Execute multiple Non-Select query using batch updates and simple Statement");
            System.out.println("2. Execute multiple Non-Select query using batch updates and Prepared Statement");
            System.out.println("3. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    ExecuteBatchWithStatement();
                    break;
                }
                case 2 : {
                    ExecuteBatchWithPreparedStatement();
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

    private static void ExecuteBatchWithStatement() {
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // using simple Statement object for batch updates
            try(Statement statement = connection.createStatement()) {
                while(true) {
                    System.out.println("Enter Non-select SQL query that you want to execute :");
                    sc.nextLine();
                    String sqlQuery = sc.nextLine();
                    // adding the entered SQL query into batch
                    statement.addBatch(sqlQuery);
                    System.out.print("Do you want to append another Non-select query?[Yes/No] :");
                    if(sc.next().equalsIgnoreCase("No")) break;
                }
                // calling 'executeBatch' which will return the int array that represents the number of rows effected
                // of each query added in batch
                int[] result = statement.executeBatch();
                int rowsEffected = 0;
                for (int rowEffected : result) {
                    rowsEffected += rowEffected;
                }
                System.out.println(String.format("Number of rows effected : %d", rowsEffected));
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

    private static void ExecuteBatchWithPreparedStatement() {
        String sqlQuery = "INSERT INTO student VALUES (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // using PreparedStatement for batch updates
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                while(true) {
                    System.out.println("Enter student details :");
                    System.out.print("ID :");
                    int id = sc.nextInt();
                    System.out.print("FirstName :");
                    String firstName = sc.next();
                    System.out.print("LastName :");
                    String lastName = sc.next();
                    System.out.print("Age :");
                    int age = sc.nextInt();
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, firstName);
                    preparedStatement.setString(3, lastName);
                    preparedStatement.setInt(4, age);

                    // adding SQL query to the batch
                    // for PreparedStatement, there is no need of passing SQL query while adding query to the batch
                    preparedStatement.addBatch();
                    System.out.print("Do you want to insert another record?[Yes/No] :");
                    if(sc.next().equalsIgnoreCase("No")) break;
                }
                int[] result = preparedStatement.executeBatch();
                int rowsEffected = 0;
                for (int rowEffected : result) {
                    rowsEffected += rowEffected;
                }
                System.out.println(String.format("Number of rows effected : %d", rowsEffected));
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
