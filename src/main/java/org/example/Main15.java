/*
   Code to demonstrate working of various types of RowSet
*/

package org.example;

import org.example.utils.DisplayResultSet;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main15 {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Select record using jdbcRowSet");
            System.out.println("2. Insert record using jdbcRowSet");
            System.out.println("3. Update record using jdbcRowSet");
            System.out.println("4. Delete record using jdbcRowSet");
            System.out.println("5. Select record using CachedRowSet");
            System.out.println("6. Insert record using CachedRowSet");
            System.out.println("7. Update record using CachedRowSet");
            System.out.println("8. Delete record using CachedRowSet");
            System.out.println("9. Select record using WebRowSet");
            System.out.println("10. Insert record using WebRowSet");
            System.out.println("11. Update record using WebRowSet");
            System.out.println("12. Delete record using WebRowSet");
            System.out.println("13. Select record using JoinRowSet");
            System.out.println("14. Insert record using JoinRowSet");
            System.out.println("15. Update record using JoinRowSet");
            System.out.println("16. Delete record using JoinRowSet");
            System.out.println("17. Select record using FilteredRowSet");
            System.out.println("18. Insert record using FilteredRowSet");
            System.out.println("19. Update record using FilteredRowSet");
            System.out.println("20. Delete record using FilteredRowSet");
            System.out.println("21. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    selectRecordUsingJdbcRowSet();
                    break;
                }
                case 2 : {
                    insertRecordUsingJdbcRowSet();
                    break;
                }
                case 3 : {
                    updateRecordUsingJdbcRowSet();
                    break;
                }
                case 4 : {
                    deleteRecordUsingJdbcRowSet();
                    break;
                }
                case 5 : {
                    selectRecordUsingCachedRowSet();
                    break;
                }
                case 6 : {
                    insertRecordUsingCachedRowSet();
                    break;
                }
                case 7 : {
                    updateRecordUsingCachedRowSet();
                    break;
                }
                case 8 : {
                    deleteRecordUsingCachedRowSet();
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
        sc.close();
    }

    private static void selectRecordUsingJdbcRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            // getting RowSetFactory object to get implementation class object of JdbcRowSet
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            // getting JdbcRowSet implementation class object
            JdbcRowSet jdbcRowSet = rowSetFactory.createJdbcRowSet();
            // setting DB connection properties to establish connection
            // when dealing with RowSet we have to set the DB properties to RowSet object instead of using
            // connection object
            setConnectionPropertiesInRowSet(jdbcRowSet);
            // setting sql query that needs to be executed
            jdbcRowSet.setCommand(sqlQuery);
            // execute the query which will fetch the records from DB and store inside RowSet object
            jdbcRowSet.execute();
            // since RowSet is child of ResultSet, we can treat RowSet as ResultSet and can access records in the
            // same manner as ResultSet
            DisplayResultSet.display(jdbcRowSet);
            displayOptionsForScrollableResultSet(jdbcRowSet);
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertRecordUsingJdbcRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            // getting RowSetFactory object to get implementation class object of JdbcRowSet
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            // getting JdbcRowSet implementation class object
            JdbcRowSet jdbcRowSet = rowSetFactory.createJdbcRowSet();
            // setting DB connection properties to establish connection
            setConnectionPropertiesInRowSet(jdbcRowSet);
            // setting sql query that needs to be executed
            jdbcRowSet.setCommand(sqlQuery);
            // execute the query which will fetch the records from DB and store inside RowSet object
            jdbcRowSet.execute();
            System.out.println("Records before insertion of new record :");
            DisplayResultSet.display(jdbcRowSet);
            // move the cursor to new row to insert new record
            jdbcRowSet.moveToInsertRow();
            // get record info from the user
            System.out.print("Enter ID :");
            int id = sc.nextInt();
            System.out.print("Enter firstName :");
            String firstName = sc.next();
            System.out.print("Enter lastName :");
            String lastName = sc.next();
            System.out.print("Enter city ID :");
            int cityId = sc.nextInt();
            // inserting the values to newly added row
            jdbcRowSet.updateInt(1, id);
            jdbcRowSet.updateString(2, firstName);
            jdbcRowSet.updateString(3, lastName);
            jdbcRowSet.updateInt(4, cityId);
            // once all the values added to row, insert the record to the database
            jdbcRowSet.insertRow();
            System.out.println("Records after insertion of new record :");
            // move the cursor to before first row to print record starting from top
            jdbcRowSet.beforeFirst();
            DisplayResultSet.display(jdbcRowSet);
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRecordUsingJdbcRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            // getting RowSetFactory object to get implementation class object of JdbcRowSet
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            // getting JdbcRowSet implementation class object
            JdbcRowSet jdbcRowSet = rowSetFactory.createJdbcRowSet();
            // setting DB connection properties to establish connection
            setConnectionPropertiesInRowSet(jdbcRowSet);
            // setting sql query that needs to be executed
            jdbcRowSet.setCommand(sqlQuery);
            // execute the query which will fetch the records from DB and store inside RowSet object
            jdbcRowSet.execute();
            System.out.println("Records before update :");
            DisplayResultSet.display(jdbcRowSet);
            System.out.print("Enter row number that needs to be updated :");
            int rowNumber = sc.nextInt();
            // moving the cursor to the row which we want to update
            jdbcRowSet.absolute(rowNumber);
            // get updated info from the user
            System.out.print("Enter firstName :");
            String firstName = sc.next();
            System.out.print("Enter lastName :");
            String lastName = sc.next();
            System.out.print("Enter city ID :");
            int cityId = sc.nextInt();
            // updating the values of the record
            jdbcRowSet.updateString(2, firstName);
            jdbcRowSet.updateString(3, lastName);
            jdbcRowSet.updateInt(4, cityId);
            // once all the values added to row, update the record to the database
            jdbcRowSet.updateRow();
            System.out.println("Records after update :");
            // move the cursor to before first row to print record starting from top
            jdbcRowSet.beforeFirst();
            DisplayResultSet.display(jdbcRowSet);
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecordUsingJdbcRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            // getting RowSetFactory object to get implementation class object of JdbcRowSet
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            // getting JdbcRowSet implementation class object
            JdbcRowSet jdbcRowSet = rowSetFactory.createJdbcRowSet();
            // setting DB connection properties to establish connection
            setConnectionPropertiesInRowSet(jdbcRowSet);
            // setting sql query that needs to be executed
            jdbcRowSet.setCommand(sqlQuery);
            // execute the query which will fetch the records from DB and store inside RowSet object
            jdbcRowSet.execute();
            System.out.println("Records before deletion :");
            DisplayResultSet.display(jdbcRowSet);
            System.out.print("Enter row number that needs to be deleted :");
            int rowNumber = sc.nextInt();
            // moving the cursor to the row which we want to delete
            jdbcRowSet.absolute(rowNumber);
            // deleting the row after moving the cursor to the specific row
            jdbcRowSet.deleteRow();
            System.out.println("Records after deletion :");
            // move the cursor to before first row to print record starting from top
            jdbcRowSet.beforeFirst();
            DisplayResultSet.display(jdbcRowSet);
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectRecordUsingCachedRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            Connection connection = getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // using PreparedStatement to execute query cause we want ResultSet object to demonstrate disconnected
                // property of CachedRowSet
                // to get CachedRowSet, we will use ResultSet data and will populate the data into CachedRowSet
                ResultSet resultSet = preparedStatement.executeQuery();
                // getting RowSetFactory object to get implementation class object of JdbcRowSet
                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                // getting CachedRowSet implementation class object
                CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
                // populating ResultSet data into CachedRowSet
                cachedRowSet.populate(resultSet);
                // closing the connection just to showcase that after closing connection we can't access ResultSet but
                // can access CachedRowSet as it is disconnected
                connection.close();
                // getting records from CachedRowSet after having connection closed
                DisplayResultSet.display(cachedRowSet);
                displayOptionsForScrollableResultSet(cachedRowSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertRecordUsingCachedRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            Connection connection = getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // using PreparedStatement to execute query cause we want ResultSet object to demonstrate disconnected
                // property of CachedRowSet
                // to get CachedRowSet, we will use ResultSet data and will populate the data into CachedRowSet
                ResultSet resultSet = preparedStatement.executeQuery();
                // getting RowSetFactory object to get implementation class object of JdbcRowSet
                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                // getting CachedRowSet implementation class object
                CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
                // populating ResultSet data into CachedRowSet
                cachedRowSet.populate(resultSet);
                // closing the connection just to showcase that after closing connection we can't access ResultSet but
                // can access CachedRowSet as it is disconnected
                connection.close();
                // getting records from CachedRowSet after having connection closed
                System.out.println("Records before insertion of new record :");
                DisplayResultSet.display(cachedRowSet);
                // move the cursor to new row to insert new record
                cachedRowSet.moveToInsertRow();
                // get record info from the user
                System.out.print("Enter ID :");
                int id = sc.nextInt();
                System.out.print("Enter firstName :");
                String firstName = sc.next();
                System.out.print("Enter lastName :");
                String lastName = sc.next();
                System.out.print("Enter city ID :");
                int cityId = sc.nextInt();
                // inserting the values to newly added row
                cachedRowSet.updateInt(1, id);
                cachedRowSet.updateString(2, firstName);
                cachedRowSet.updateString(3, lastName);
                cachedRowSet.updateInt(4, cityId);
                // once all the values added to row, insert the record to the cachedRowSet
                // here the new record will not be transferred to DB as connection is close
                // when the connection will open, then only this newly added record will be inserted in DB
                cachedRowSet.insertRow();
                // opening new connection to reflect the changes in DB that was made in cachedRowSet
                connection = getConnection();
                // disabling Auto Commit cause if it set, we can't manually commit the changes. Since 'acceptChanges'
                // method manually commit the changes, we have to disable Auto Commit
                connection.setAutoCommit(false);
                // pass the Connection object for accepting the changes. This will reflect the changes in DB
                cachedRowSet.acceptChanges(connection);
                System.out.println("Records after insertion of new record :");
                // move the cursor to before first row to print record starting from top
                cachedRowSet.beforeFirst();
                DisplayResultSet.display(cachedRowSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRecordUsingCachedRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            Connection connection = getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // using PreparedStatement to execute query cause we want ResultSet object to demonstrate disconnected
                // property of CachedRowSet
                // to get CachedRowSet, we will use ResultSet data and will populate the data into CachedRowSet
                ResultSet resultSet = preparedStatement.executeQuery();
                // getting RowSetFactory object to get implementation class object of JdbcRowSet
                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                // getting CachedRowSet implementation class object
                CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
                // populating ResultSet data into CachedRowSet
                cachedRowSet.populate(resultSet);
                // closing the connection just to showcase that after closing connection we can't access ResultSet but
                // can access CachedRowSet as it is disconnected
                connection.close();
                // getting records from CachedRowSet after having connection closed
                System.out.println("Records before update :");
                DisplayResultSet.display(cachedRowSet);
                System.out.print("Enter row number that needs to be updated :");
                int rowNumber = sc.nextInt();
                // moving the cursor to the row which we want to update
                cachedRowSet.absolute(rowNumber);
                // get updated info from the user
                System.out.print("Enter firstName :");
                String firstName = sc.next();
                System.out.print("Enter lastName :");
                String lastName = sc.next();
                System.out.print("Enter city ID :");
                int cityId = sc.nextInt();
                // updating the values of the record
                cachedRowSet.updateString(2, firstName);
                cachedRowSet.updateString(3, lastName);
                cachedRowSet.updateInt(4, cityId);
                // once all the values updated in row, update the record to the cachedRowSet
                // here the record will not be updated in DB as connection is close
                // when the connection will open, then only this record will be updated in DB
                cachedRowSet.insertRow();
                // opening new connection to reflect the changes in DB that was made in cachedRowSet
                connection = getConnection();
                // disabling Auto Commit cause if it set, we can't manually commit the changes. Since 'acceptChanges'
                // method manually commit the changes, we have to disable Auto Commit
                connection.setAutoCommit(false);
                // pass the Connection object for accepting the changes. This will reflect the changes in DB
                cachedRowSet.acceptChanges(connection);
                System.out.println("Records after update :");
                // move the cursor to before first row to print record starting from top
                cachedRowSet.beforeFirst();
                DisplayResultSet.display(cachedRowSet);
            }
        }
        catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecordUsingCachedRowSet() {
        String sqlQuery = "SELECT * FROM professor";
        try {
            Connection connection = getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // using PreparedStatement to execute query cause we want ResultSet object to demonstrate disconnected
                // property of CachedRowSet
                // to get CachedRowSet, we will use ResultSet data and will populate the data into CachedRowSet
                ResultSet resultSet = preparedStatement.executeQuery();
                // getting RowSetFactory object to get implementation class object of JdbcRowSet
                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                // getting CachedRowSet implementation class object
                CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
                // populating ResultSet data into CachedRowSet
                cachedRowSet.populate(resultSet);
                // closing the connection just to showcase that after closing connection we can't access ResultSet but
                // can access CachedRowSet as it is disconnected
                connection.close();
                // getting records from CachedRowSet after having connection closed
                System.out.println("Records before deletion :");
                DisplayResultSet.display(cachedRowSet);
                System.out.print("Enter row number that needs to be deleted :");
                int rowNumber = sc.nextInt();
                // moving the cursor to the row which we want to delete
                cachedRowSet.absolute(rowNumber);
                // deleting row from CachedRowSet
                // here the record will not be deleted in DB as connection is close
                // when the connection will open, then only this record will be deleted in DB
                cachedRowSet.deleteRow();
                // opening new connection to reflect the changes in DB that was made in cachedRowSet
                connection = getConnection();
                // disabling Auto Commit cause if it set, we can't manually commit the changes. Since 'acceptChanges'
                // method manually commit the changes, we have to disable Auto Commit
                connection.setAutoCommit(false);
                // pass the Connection object for accepting the changes. This will reflect the changes in DB
                cachedRowSet.acceptChanges(connection);
                System.out.println("Records after deletion :");
                // move the cursor to before first row to print record starting from top
                cachedRowSet.beforeFirst();
                DisplayResultSet.display(cachedRowSet);
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
            InputStream inputStream = Main15.class.getClassLoader().getResourceAsStream("db.properties");
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

    private static void setConnectionPropertiesInRowSet(RowSet rowSet) throws SQLException {
        try{
            InputStream inputStream = Main15.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            // setting DB url, username and password in RowSet object
            rowSet.setUrl(properties.getProperty("url"));
            rowSet.setUsername(properties.getProperty("user"));
            rowSet.setPassword(properties.getProperty("password"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayOptionsForScrollableResultSet(ResultSet resultSet) throws SQLException {
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
