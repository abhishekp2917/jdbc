package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main4 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String username = "root";
    private static String password = "password@123";
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Select record from student table");
            System.out.println("2. Insert into student table");
            System.out.println("3. Update records of student table");
            System.out.println("4. Delete records from student table");
            System.out.println("5. Exit");
            System.out.println("_________________________________________________\n");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 : {
                    select();
                    break;
                }
                case 2 : {
                    insert();
                    break;
                }
                case 3 : {
                    update();
                    break;
                }
                case 4 : {
                    delete();
                    break;
                }
                case 5 : {
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

    private static void select() {
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // dynamic query to select record into student table
            // '?' will be replaced by actual ID value that needs to be selected
            String sqlQuery = "SELECT * FROM student WHERE ID=?";
            // creating PreparedStatement object by passing dynamic sql query
            // this will compile the provided sql query
            // for compiling sql query, only syntax in sufficient. Not required to provide values
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // setting the query parameter and then executing it times
                // every time RDBMS will only execute the query but not compile it as this has already been taken care of
                while(true) {
                    // setting the parameter ID value dynamically
                    // setting parameter has no relation with column type, one can use 'setString' to set column of type
                    // int given that provided parameter must be a number in form of string
                    System.out.print("Enter ID value : ");
                    // 'setInt' will pass int value to parameter i.e. 'ID'
                    preparedStatement.setInt(1, sc.nextInt());
                    // calling 'executeQuery' to execute the above query
                    ResultSet result = preparedStatement.executeQuery();
                    displayResultSet(result);
                    System.out.print("Do you want to select another record?[YES/NO] : ");
                    if(sc.next().equalsIgnoreCase("NO")) break;
                }
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

    private static void insert() {
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // dynamic query to insert record into student table
            // '?' will be replaced by actual value that needs to be inserted
            String sqlQuery = "INSERT INTO student VALUES (?, ?, ?, ?)";
            // creating PreparedStatement object by passing dynamic sql query
            // this will compile the provided sql query
            // for compiling sql query, only syntax in sufficient. Not required to provide values
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // setting the query parameter and then executing it multiple times
                // every time RDBMS will only execute the query but not compile it as this has already been taken care of
                while(true) {
                    // setting the parameter (column) values dynamically
                    // setting parameter has no relation with column type, one can use 'setString' to set column of type
                    // int given that provided parameter must be a number in form of string
                    System.out.println("Enter column values : ");
                    System.out.print("ID : ");
                    // 'setInt' will pass int value to parameter
                    preparedStatement.setInt(1, sc.nextInt());
                    System.out.print("Firstname : ");
                    // 'setString' will pass String value to parameter
                    preparedStatement.setString(2, sc.next());
                    System.out.print("LastName : ");
                    preparedStatement.setString(3, sc.next());
                    System.out.print("Age : ");
                    preparedStatement.setInt(4, sc.nextInt());
                    // calling 'executeUpdate' to execute the above query
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println(String.format("Number of records inserted : %d", rowsEffected));
                    System.out.print("Do you want to insert another record?[YES/NO] : ");
                    if(sc.next().equalsIgnoreCase("NO")) break;
                }
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

    private static void update() {
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // dynamic query to update record of student table
            // '?' will be replaced by actual value
            String sqlQuery = "UPDATE student SET FirstName=? WHERE ID=?";
            // creating PreparedStatement object by passing dynamic sql query
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // setting the query parameter and then executing it multiple times
                // every time RDBMS will only execute the query but not compile it as this has already been taken care of
                while(true) {
                    // setting the parameter values dynamically
                    // setting parameter has no relation with column type, one can use 'setString' to set column of type
                    // int given that provided parameter must be a number in form of string
                    System.out.print("Enter ID whose FirstName you want to update : ");
                    // 'setInt' will pass int value to parameter
                    preparedStatement.setInt(2, sc.nextInt());
                    System.out.print("Enter Firstname : ");
                    // 'setString' will pass String value to parameter
                    preparedStatement.setString(1, sc.next());
                    // calling 'executeUpdate' to execute the above query
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println(String.format("Number of records updated : %d", rowsEffected));
                    System.out.print("Do you want to update another record?[YES/NO] : ");
                    if(sc.next().equalsIgnoreCase("NO")) break;
                }
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

    private static void delete() {
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // dynamic query to delete record from student table
            // '?' will be replaced by actual value that needs to be deleted
            String sqlQuery = "DELETE FROM student WHERE ID=?";
            // creating PreparedStatement object by passing dynamic sql query
            // this will compile the provided sql query
            // for compiling sql query, only syntax in sufficient. Not required to provide values
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // setting the query parameter and then executing it multiple times
                // every time RDBMS will only execute the query but not compile it as this has already been taken care of
                while(true) {
                    // setting the parameter ID value dynamically
                    // setting parameter has no relation with column type, one can use 'setString' to set column of type
                    // int given that provided parameter must be a number in form of string
                    System.out.print("Enter ID which you want to delete : ");
                    // 'setInt' will pass int value to parameter
                    preparedStatement.setInt(1, sc.nextInt());
                    // calling 'executeUpdate' to execute the above query
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println(String.format("Number of records deleted : %d", rowsEffected));
                    System.out.print("Do you want to delete another record?[YES/NO] : ");
                    if(sc.next().equalsIgnoreCase("NO")) break;
                }
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

    private static void displayResultSet(ResultSet result) {
        try{

            // ResultSetMetaData hold meta data i.e. data about the data (result set) such as
            // column count, column name of a specific index etc
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnMaxSize = new int[columnCount];
            for(int i=1; i<=columnCount; i++) {
                columnMaxSize[i-1] = Math.max(columnMaxSize[i-1], metaData.getColumnName(i).length());
            }
            ArrayList<String[]> records = new ArrayList<>();
            while(result.next()) {
                String[] record = new String[columnCount];
                for(int i=1; i<=columnCount; i++) {
                    record[i-1] = result.getString(i);
                    columnMaxSize[i-1] = Math.max(columnMaxSize[i-1], record[i-1].length());
                }
                records.add(record);
            }
            System.out.println("\n_________________________________________________");
            for(int i=1; i<=columnCount; i++) {
                int desiredWidth = columnMaxSize[i-1];
                String stringWithTrailingSpaces = String.format("%-" + desiredWidth + "s", metaData.getColumnName(i));
                System.out.print(String.format("\t%s\t", stringWithTrailingSpaces));
            }
            System.out.println("\n_________________________________________________");
            for(String[] record : records) {
                for(int i=0; i<record.length; i++) {
                    int desiredWidth = columnMaxSize[i];
                    String stringWithTrailingSpaces = String.format("%-" + desiredWidth + "s", record[i]);
                    System.out.print(String.format("\t%s\t", stringWithTrailingSpaces));
                }
                System.out.println();
            }
            System.out.println("_________________________________________________\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
