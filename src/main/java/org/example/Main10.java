/*
   Code to demonstrate different approach of creating connection object
*/

package org.example;

import org.example.utils.DisplayResultSet;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main10 {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Create connection by using method with 3 parameter");
            System.out.println("2. Create connection by using method with 2 parameter");
            System.out.println("3. Create connection by using method with 1 parameter");
            System.out.println("4. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    createConnectionUsingThreeParameter();
                    break;
                }
                case 2 : {
                    createConnectionUsingTwoParameter();
                    break;
                }
                case 3 : {
                    createConnectionUsingOneParameter();
                    break;
                }
                case 4 : {
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

    private static void createConnectionUsingThreeParameter() {
        // creating Properties object which will be use to read '.properties' file values
        Properties properties = new Properties();
        InputStream inputStream;
        try{
            // loading the '.properties' file into class path using class loader which in turn will return an object of
            // type InputStream
            inputStream = Main10.class.getClassLoader().getResourceAsStream("db.properties");
            // loading the '.properties' file into Properties object
            properties.load(inputStream);
            // reading jdbc connection property from '.properties' file
            final String jdbcUrl = properties.getProperty("url");
            final String USERNAME = properties.getProperty("user");
            final String PASSWORD = properties.getProperty("password");
            // creating connection by passing all the three DB parameter values i.e url, user and password
            try(Connection connection = DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD)) {
                try (Statement statement = connection.createStatement()) {
                    String sqlQuery = "SELECT * FROM student";
                    ResultSet result = statement.executeQuery(sqlQuery);
                    DisplayResultSet.display(result);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void createConnectionUsingTwoParameter() {
        // creating Properties object which will be use to read '.properties' file values
        Properties properties = new Properties();
        InputStream inputStream;
        try{
            // loading the '.properties' file into class path using class loader which in turn will return an object of
            // type InputStream
            inputStream = Main10.class.getClassLoader().getResourceAsStream("db.properties");
            // loading the '.properties' file into Properties object
            properties.load(inputStream);
            // reading jdbc connection url from '.properties' file
            final String jdbcUrl = properties.getProperty("url");
            // creating connection by passing jdbc url and Properties object which has DB user and password
            try(Connection connection = DriverManager.getConnection(jdbcUrl, properties)) {
                try (Statement statement = connection.createStatement()) {
                    String sqlQuery = "SELECT * FROM student";
                    ResultSet result = statement.executeQuery(sqlQuery);
                    DisplayResultSet.display(result);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void createConnectionUsingOneParameter() {
        // creating Properties object which will be use to read '.properties' file values
        Properties properties = new Properties();
        InputStream inputStream;
        try{
            // loading the '.properties' file into class path using class loader which in turn will return an object of
            // type InputStream
            inputStream = Main10.class.getClassLoader().getResourceAsStream("db.properties");
            // loading the '.properties' file into Properties object
            properties.load(inputStream);
            // reading jdbc connection property from '.properties' file and constructing jdbc url which has DB
            // user and password as a query parameter
            final String jdbcUrl = String.format("%s?user=%s&password=%s",
                    properties.getProperty("url"),
                    properties.getProperty("user"),
                    properties.getProperty("password"));
            // creating connection by passing juts jdbc url which has db user and password as a query parameter
            try(Connection connection = DriverManager.getConnection(jdbcUrl)) {
                try (Statement statement = connection.createStatement()) {
                    String sqlQuery = "SELECT * FROM student";
                    ResultSet result = statement.executeQuery(sqlQuery);
                    DisplayResultSet.display(result);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
