/*
   Code to demonstrate how to deal with Dates in jdbc
*/

package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;

public class Main13 {

    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Insert record");
            System.out.println("2. Retrieve records");
            System.out.println("3. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    insertRecord();
                    break;
                }
                case 2 : {
                    retrieveRecord();
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

    private static void insertRecord() {
        String sqlQuery = "INSERT INTO policy (EffectiveDate, ExpirationDate) VALUES (?, ?)";
        try(Connection connection = getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // getting date values from user in 'dd-MM-yyyy' format
                System.out.print("Enter policy EffectiveDate in dd-MM-yyyy format :");
                String date1 = sc.next();
                System.out.print("Enter policy ExpirationDate in dd-MM-yyyy format :");
                String date2 = sc.next();
                // create SimpleDateFormat object to parse string to java.util.Date format
                // in constructor provide the required date format. In this case it is 'dd-MM-yyyy'
                // the string formatted date value should match the provided date format in constructor
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                // parsing string to java.util.Date format
                java.util.Date utilEffectiveDate = sdf.parse(date1);
                java.util.Date utilExpirationDate = sdf.parse(date2);
                // converting java.util.Date to java.sql.Date by passing time value of java.util.Date to java.sql.Date
                // constructor
                java.sql.Date effectiveDate = new Date(utilEffectiveDate.getTime());
                java.sql.Date expirationDate = new Date(utilExpirationDate.getTime());
                // use setDate method to pass the date value to positional parameter of PreparedStatement
                preparedStatement.setDate(1, effectiveDate);
                preparedStatement.setDate(2, expirationDate);
                preparedStatement.executeUpdate();
                System.out.print("Record inserted successfully");
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveRecord() {
        String sqlQuery = "SELECT * FROM policy WHERE policyNumber=?";
        try(Connection connection = getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                System.out.print("Enter policyNumber : ");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    int policyNumber = resultSet.getInt(1);
                    // getting the java.sql.Date from the result set which will be in the format that is supported by
                    // the database. In case of MySQL, it is 'yyyy-MM-dd'
                    java.sql.Date effDate = resultSet.getDate(2);
                    java.sql.Date expDate = resultSet.getDate(3);
                    // to get the date in end user specific format, use SimpleDateFormat to format the java.sql.Date to
                    // required Date format in string
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    // formatting java.sql.Date to required string Date format
                    String effectiveDate = sdf.format(effDate);
                    String expirationDate = sdf.format(expDate);
                    System.out.print(String.format("| Policy number : %s | Effective Date : %s | Expiration Date : %s |", policyNumber, effectiveDate, expirationDate));
                }
                else {
                    System.out.println("Record not found");
                }
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        try{
            InputStream inputStream = Main13.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
