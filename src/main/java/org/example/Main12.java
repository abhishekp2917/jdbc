/*
   Code to demonstrate how to deal with metadata in jdbc
*/

package org.example;

import org.example.utils.DisplayResultSet;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main12 {

    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Get Database metadata");
            System.out.println("2. Get ResultSet metadata");
            System.out.println("3. Get Parameter metadata");
            System.out.println("4. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    getDatabaseMetadata();
                    break;
                }
                case 2 : {
                    getResultSetMetadata();
                    break;
                }
                case 3 : {
                    getParameterMetadata();
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

    private static void getDatabaseMetadata() {
        try(Connection connection = getConnection()) {
            // getting DatabaseMetaData object from Connection object
            DatabaseMetaData dbMetaData = connection.getMetaData();
            // fetching database related details from DatabaseMetaData object
            System.out.println(String.format("DBMS Name : %s ", dbMetaData.getDatabaseProductName()));
            System.out.println(String.format("DBMS version : %s ", dbMetaData.getDatabaseProductVersion()));
            System.out.println(String.format("Driver Name : %s ", dbMetaData.getDriverName()));
            System.out.println(String.format("DB url : %s ", dbMetaData.getURL()));
            System.out.println(String.format("DB username : %s", dbMetaData.getUserName()));
            DisplayResultSet.display(dbMetaData.getSchemas());
            DisplayResultSet.display(dbMetaData.getTables(null, null, null, null));
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getResultSetMetadata() {
        try(Connection connection = getConnection()) {
            try(Statement statement = connection.createStatement()) {
                System.out.print("Enter Select Query : ");
                sc.nextLine();
                String sqlQuery = sc.nextLine();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                // getting ResultSetMetaData object from ResultSet object
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                // fetching resultSet related details from ResultSetMetaData object
                System.out.println(String.format("Number of columns : %d ", resultSetMetaData.getColumnCount()));
                for(int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                    System.out.println("\n_________________________________________________");
                    System.out.println(String.format("Column Name : %s ", resultSetMetaData.getColumnName(i)));
                    System.out.println(String.format("Is Column Nullable? : %s ", resultSetMetaData.isNullable(i)));
                    System.out.println(String.format("Column Type Name : %s ", resultSetMetaData.getColumnTypeName(i)));
                    System.out.println(String.format("Is AutoIncrement? : %s ", resultSetMetaData.isAutoIncrement(i)));
                    System.out.println("_________________________________________________");
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

    private static void getParameterMetadata() {
        String sqlQuery = "INSERT INTO student (ID, Firstname, LastName, Age) VALUES (?, ?, ?, ?)";
        try(Connection connection = getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // getting ParameterMetaData object from PreparedStatement object
                ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
                // fetching PreparedStatement parameters related details from ParameterMetaData object
                System.out.println(String.format("Number of parameters : %d ", parameterMetaData.getParameterCount()));
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
            InputStream inputStream = Main12.class.getClassLoader().getResourceAsStream("db.properties");
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
