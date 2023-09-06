/*
   Code to demonstrate how to execute stored procedure and functions using CallableStatement
*/

package org.example;

import org.example.utils.DisplayResultSet;
import java.sql.*;
import java.util.Scanner;

public class Main6 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String USERNAME = "root";
    private static String PASSWORD = "password@123";
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Update Employee Salary");
            System.out.println("2. Select Employees whose salary is within given range");
            System.out.println("3. Get average salary of Employees");
            System.out.println("4. Get maximum salary of 2 Employees");
            System.out.println("5. Remove employee above specified age");
            System.out.println("6. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    updateSalary();
                    break;
                }
                case 2 : {
                    selectEmployees();
                    break;
                }
                case 3 : {
                    getAverageSalary();
                    break;
                }
                case 4 : {
                    getMaximumSalary();
                    break;
                }
                case 5 : {
                    removeEmployee();
                    break;
                }
                case 6 : {
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

    private static void updateSalary() {
        System.out.print("Enter Employee ID whose salary you want to update : ");
        int employeeId = sc.nextInt();
        System.out.print("Enter new salary : ");
        float newSalary = sc.nextFloat();
        // preparing SQL query to call 'UpdateSalary' stored procedure which takes 2 argument
        String sqlQuery = "{call UpdateSalary(?, ?)}";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // creating 'CallableStatement' object to execute stored procedure which will update salary of employee of
            // provided employee ID
            try(CallableStatement callableStatement = connection.prepareCall(sqlQuery)) {
                // passing parameter values to sql query
                callableStatement.setInt(1, employeeId);
                callableStatement.setFloat(2, newSalary);
                // since this SP will run update query, we are using 'executeUpdate' method which will return the number
                // of rows effected
                // we can use 'execute' and 'executeUpdate' method also to execute the SP.
                // using 'executeUpdate' will return the records selected by SP and
                // using 'execute' will return true if SP contains any select query else false
                // Using any of the execute method will call the SP and execute it
                // use 'executeQuery' if you are expecting records to be selected by SP
                // use 'executeUpdate' if you are interested in number of rows effected by SP
                int rowsEffected = callableStatement.executeUpdate();
                System.out.print(String.format("Number of rows effected : %d", rowsEffected));
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

    private static void selectEmployees() {
        System.out.print("Enter minimum salary : ");
        float minSalary = sc.nextFloat();
        System.out.print("Enter maximum salary : ");
        float maxSalary = sc.nextFloat();
        // preparing SQL query to call 'GetEmployeeInfo' stored procedure which takes 2 argument
        String sqlQuery = "{call GetEmployeeInfo(?, ?)}";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // creating 'CallableStatement' object to execute stored procedure which will list out the employee whose
            // salary is within given range
            try(CallableStatement callableStatement = connection.prepareCall(sqlQuery)) {
                // passing parameter values to sql query
                callableStatement.setFloat(1, minSalary);
                callableStatement.setFloat(2, maxSalary);
                // since this SP will run select query, we are using 'executeQuery' method which will return the
                // ResultSet. If we will use 'executeUpdate', it will return number of rows selected and effected by SP
                ResultSet result = callableStatement.executeQuery();
                DisplayResultSet.display(result);
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

    private static void getAverageSalary() {
        // preparing SQL query to call 'GetAvgSalary' stored procedure which takes one out parameter
        String sqlQuery = "{call GetAvgSalary(?)}";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // creating 'CallableStatement' object to execute stored procedure which will find average of employees
            // salary and will assign it to out parameter
            try(CallableStatement callableStatement = connection.prepareCall(sqlQuery)) {
                // mapping SP out parameter with Java data type using JDBC types
                callableStatement.registerOutParameter(1, Types.FLOAT);
                // since this SP will not update or select any record, executing this SP with normal execute method will
                // work
                callableStatement.execute();
                // getting the value of out parameter which will hold the average of all the employees salary
                float avgSalary = callableStatement.getFloat(1);
                System.out.println(String.format("Average salary of employees is : %f", avgSalary));
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

    private static void getMaximumSalary() {
        System.out.print("Enter first EmployeeID : ");
        int employeeID1 = sc.nextInt();
        System.out.print("Enter second EmployeeID : ");
        int employeeID2 = sc.nextInt();
        // preparing SQL query to call 'GetMaxSalary' function that takes 2 arguments and returns maximum salary
        // between two employees
        // by adding '?=' before 'call' will differentiate query between SPs and functions
        String sqlQuery = "{?=call GetMaxSalary(?, ?)}";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // creating 'CallableStatement' object to execute function
            try(CallableStatement callableStatement = connection.prepareCall(sqlQuery)) {
                // mapping function return type with Java data type using JDBC types
                callableStatement.registerOutParameter(1, Types.FLOAT);
                // setting the input parameters of the function
                callableStatement.setInt(2, employeeID1);
                callableStatement.setInt(3, employeeID2);
                // executing query to call the function
                callableStatement.execute();
                // getting the value of out parameter which will hold the maximum salary
                float maxSalary = callableStatement.getFloat(1);
                System.out.println(String.format("Maximum salary among 2 employees is : %f", maxSalary));
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

    private static void removeEmployee() {
        System.out.print("Enter age : ");
        int age = sc.nextInt();
        // preparing SQL query to call 'RemoveEmployeeAboveSpecifiedAge' function that takes 1 argument and returns
        // count of employee removed
        String sqlQuery = "{?=call RemoveEmployeeAboveSpecifiedAge(?)}";
        try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
            // creating 'CallableStatement' object to execute function
            try(CallableStatement callableStatement = connection.prepareCall(sqlQuery)) {
                // mapping function return type with Java data type using JDBC types
                callableStatement.registerOutParameter(1, Types.INTEGER);
                // setting the input parameter of the function
                callableStatement.setInt(2, age);
                // executing query to call the function
                callableStatement.execute();
                // getting the value of out parameter which will hold the maximum salary
                int noOfEmployeesRemoved = callableStatement.getInt(1);
                System.out.println(String.format("Number of employees removed : %d", noOfEmployeesRemoved));
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
