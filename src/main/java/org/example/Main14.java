/*
   Code to demonstrate working of various types of ResultSet
*/

package org.example;

import org.example.utils.DisplayResultSet;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main14 {

    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Scroll ResultSet in insensitive manner");
            System.out.println("2. Scroll ResultSet in sensitive manner");
            System.out.println("3. Insert record using updatable ResultSet");
            System.out.println("4. Update record using updatable ResultSet");
            System.out.println("5. Delete record using updatable ResultSet");
            System.out.println("6. Close ResultSet cursor at commit");
            System.out.println("7. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    scrollInsensitive();
                    break;
                }
                case 2 : {
                    scrollSensitive();
                    break;
                }
                case 3 : {
                    insertRecordUsingResultSet();
                    break;
                }
                case 4 : {
                    updateRecordUsingResultSet();
                    break;
                }
                case 5 : {
                    deleteRecordUsingResultSet();
                    break;
                }
                case 6 : {
                    closeResultSetCursorAtCommit();
                    break;
                }
                case 7 : {
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

    private static void scrollInsensitive() {
        String sqlQuery = "SELECT * FROM student";
        try(Connection connection = getConnection()) {
            // creating Statement object and configuring it with ResultSet constants as scroll insensitive, read only and
            // hold cursors over commit
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT)) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                boolean exit = false;
                while(!exit) {
                    System.out.println("\n_________________________________________________");
                    System.out.println("Select any of the below operation : ");
                    System.out.println("1. Move the cursor at before first row");
                    System.out.println("2. Move the cursor at after last row");
                    System.out.println("3. Move the cursor at first row");
                    System.out.println("4. Move the cursor at last row");
                    System.out.println("5. Get nth row");
                    System.out.println("6. Get nth row from current row");
                    System.out.println("7. Get all the row from top to bottom");
                    System.out.println("8. Get all the row from bottom to top");
                    System.out.println("9. Exit");
                    System.out.println("_________________________________________________\n");
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1 : {
                            // this will move the ResultSet cursor to the position before first row
                            resultSet.beforeFirst();
                            // checking if the cursor is before the first row or not
                            if(resultSet.isBeforeFirst()) System.out.println("Moved the cursor at before first row");
                            else System.out.println("Error occurred!");
                            break;
                        }
                        case 2 : {
                            // this will move the ResultSet cursor to the position after last row
                            resultSet.afterLast();
                            // checking if the cursor is after the last row or not
                            if(resultSet.isAfterLast()) System.out.println("Moved the cursor at after last row");
                            else System.out.println("Error occurred!");
                            break;
                        }
                        case 3 : {
                            // this will move the ResultSet cursor to the first row
                            resultSet.first();
                            // checking if the cursor is at the first row or not
                            if(resultSet.isFirst()) System.out.println("Moved the cursor at first row");
                            else System.out.println("Error occurred!");
                            break;
                        }
                        case 4 : {
                            // this will move the ResultSet cursor to the last row
                            resultSet.last();
                            // checking if the cursor is at the last row or not
                            if(resultSet.isLast()) System.out.println("Moved the cursor at last row");
                            else System.out.println("Error occurred!");
                            break;
                        }
                        case 5 : {
                            System.out.print("Enter row number :");
                            int rowNumber = sc.nextInt();
                            System.out.print("Nth row from top?[YES/NO] :");
                            if(sc.next().equalsIgnoreCase("NO")) rowNumber = -rowNumber;
                            // moving the ResultSet cursor to the nth (nth = rowNumber) position from before the first
                            // row or after the last row
                            // if n is +ve then, it is before the first row otherwise after the last row
                            // if n value is out of the ResultSet range, it will raise exception
                            resultSet.absolute(rowNumber);
                            System.out.println();
                            int id = resultSet.getInt(1);
                            String firstName = resultSet.getString(2);
                            String lastName = resultSet.getString(3);
                            int age = resultSet.getInt(4);
                            System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                            System.out.println();
                            break;
                        }
                        case 6 : {
                            System.out.print("Enter row number :");
                            int rowNumber = sc.nextInt();
                            System.out.print("Nth row after current row?[YES/NO] :");
                            if(sc.next().equalsIgnoreCase("NO")) rowNumber = -rowNumber;
                            // moving the ResultSet cursor to the nth (nth = rowNumber) position from the cursor's
                            // current position
                            // if n is +ve then, it is nth position before the current position otherwise it is after
                            // the current position
                            resultSet.relative(rowNumber);
                            System.out.println();
                            int id = resultSet.getInt(1);
                            String firstName = resultSet.getString(2);
                            String lastName = resultSet.getString(3);
                            int age = resultSet.getInt(4);
                            System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                            System.out.println();
                            break;
                        }
                        case 7 : {
                            resultSet.beforeFirst();
                            System.out.println();
                            // iterating ResultSet in forward fashion
                            while(resultSet.next()) {
                                int id = resultSet.getInt(1);
                                String firstName = resultSet.getString(2);
                                String lastName = resultSet.getString(3);
                                int age = resultSet.getInt(4);
                                System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                            }
                            System.out.println();
                            break;
                        }
                        case 8 : {
                            resultSet.afterLast();
                            System.out.println();
                            // iterating ResultSet in backward fashion
                            while(resultSet.previous()) {
                                int id = resultSet.getInt(1);
                                String firstName = resultSet.getString(2);
                                String lastName = resultSet.getString(3);
                                int age = resultSet.getInt(4);
                                System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                            }
                            System.out.println();
                            break;
                        }
                        case 9 : {
                            exit = true;
                            break;
                        }
                        default: {
                            System.out.println("Invalid choice. Please select a valid option.");
                            break;
                        }
                    }
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

    private static void scrollSensitive() {
        String sqlQuery = "SELECT * FROM student WHERE age=?";
        try(Connection connection = getConnection()) {
            // creating PreparedStatement object and configuring it with ResultSet constants as scroll sensitive,
            // updatable
            // to get benefit of scroll sensitive, one has to use updatable concurrency mode else ResultSet will not
            // update on refreshing row instead it will raise an exception
            // it is to be noted that, ResultSet will fetch update if there is any update operation done on row. Insert
            // and Delete operation will not reflect.
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                System.out.print("Enter age :");
                int studentAge = sc.nextInt();
                preparedStatement.setInt(1, studentAge);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Records before any update to database :\n");
                // getting result before any update to database
                while(resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String firstName = resultSet.getString(2);
                    String lastName = resultSet.getString(3);
                    int age = resultSet.getInt(4);
                    System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                }
                resultSet.beforeFirst();
                System.in.read();
                System.out.println("Records after update to database :\n");
                // getting result after updating row on DB through another process
                while(resultSet.next()) {
                    // refreshing the row before getting the result. This will make request to DB for updated value
                    resultSet.refreshRow();
                    int id = resultSet.getInt(1);
                    String firstName = resultSet.getString(2);
                    String lastName = resultSet.getString(3);
                    int age = resultSet.getInt(4);
                    System.out.println(String.format("%d\t%s\t%s\t%d",id, firstName, lastName, age));
                }
                System.out.println();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertRecordUsingResultSet() {
        String sqlQuery = "SELECT * FROM student";
        try(Connection connection = getConnection()) {
            // creating Statement object and configuring it with ResultSet constants as scroll sensitive, updatable
            // it is not necessary to select type as Scrollable along with Updatable mode
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Records before update :");
                DisplayResultSet.display(resultSet);
                // This will create a new row in ResultSet and will move the cursor to that row
                resultSet.moveToInsertRow();
                System.out.print("Enter ID :");
                int id = sc.nextInt();
                System.out.print("Enter firstName :");
                String firstName = sc.next();
                System.out.print("Enter lastName :");
                String lastName = sc.next();
                System.out.print("Enter age :");
                int age = sc.nextInt();
                // inserting the values to newly added row
                resultSet.updateInt(1, id);
                resultSet.updateString(2, firstName);
                resultSet.updateString(3, lastName);
                resultSet.updateInt(4, age);
                // once all the values added to row, insert the record to the database
                resultSet.insertRow();
                System.out.println("Records after insertion of new record :");
                resultSet.beforeFirst();
                DisplayResultSet.display(resultSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRecordUsingResultSet() {
        String sqlQuery = "SELECT * FROM student";
        try(Connection connection = getConnection()) {
            // creating Statement object and configuring it with ResultSet constants as scroll sensitive, updatable
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Records before update :");
                DisplayResultSet.display(resultSet);
                System.out.print("Enter row number that needs to be updated :");
                int rowNumber = sc.nextInt();
                // moving the cursor to the row which we want to update
                resultSet.absolute(rowNumber);
                System.out.print("Enter firstName :");
                String firstName = sc.next();
                System.out.print("Enter lastName :");
                String lastName = sc.next();
                System.out.print("Enter age :");
                int age = sc.nextInt();
                // updating row values with new value
                resultSet.updateString(2, firstName);
                resultSet.updateString(3, lastName);
                resultSet.updateInt(4, age);
                // this will make request to DB which will update the record in DB
                resultSet.updateRow();
                System.out.println("Records after update :");
                resultSet.beforeFirst();
                DisplayResultSet.display(resultSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecordUsingResultSet() {
        String sqlQuery = "SELECT * FROM student";
        try(Connection connection = getConnection()) {
            // creating Statement object and configuring it with ResultSet constants as scroll sensitive, updatable
            try(Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Records before deletion :");
                DisplayResultSet.display(resultSet);
                System.out.print("Enter row number that needs to be deleted :");
                int rowNumber = sc.nextInt();
                // moving the cursor to the row which we want to delete
                resultSet.absolute(rowNumber);
                // this will make request to DB which will delete the record in DB
                resultSet.deleteRow();
                System.out.println("Records after deletion :");
                resultSet.beforeFirst();
                DisplayResultSet.display(resultSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeResultSetCursorAtCommit() {
        String sqlQuery = "SELECT * FROM student";
        try(Connection connection = getConnection()) {
            // disabling autocommit to false so that we can manually commit the transaction as we want to read the
            // ResultSet after committing
            connection.setAutoCommit(false);
            // creating Statement object and configuring it with ResultSet constants as close cursor at commit which will
            // close the Result once the transaction is being committed
            // This feature is available to limited JDBC drivers and might not work on some of the drivers
            try(Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                connection.commit();
                DisplayResultSet.display(resultSet);
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
            InputStream inputStream = Main14.class.getClassLoader().getResourceAsStream("db.properties");
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
