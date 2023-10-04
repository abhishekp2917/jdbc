/*
   Code to demonstrate Transactions management and Savepoint in JDBC
*/

package org.example;

import org.example.utils.DisplayResultSet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main16 {

    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Demonstrate Transaction Management");
            System.out.println("2. Demonstrate Transaction Management using Savepoint");
            System.out.println("3. Demonstrate Read Uncommitted Isolation");
            System.out.println("4. Demonstrate Read committed Isolation");
            System.out.println("5. Demonstrate Repeatable Read Isolation");
            System.out.println("6. Demonstrate Serializable Isolation");
            System.out.println("7. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    transactionManagement();
                    break;
                }
                case 2 : {
                    transactionManagementUsingSavepoint();
                    break;
                }
                case 3 : {
                    readUncommittedIsolation();
                    break;
                }
                case 4 : {
                    readCommittedIsolation();
                    break;
                }
                case 5 : {
                    repeatableReadIsolation();
                    break;
                }
                case 6 : {
                    serializableIsolation();
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

    private static void transactionManagement() {
        Connection connection = null;
        try {
            connection = getConnection();
            try(Statement statement = connection.createStatement()) {
                // disabling auto commit so that we can do rollback all the query operation in case there is any error
                // while executing the queries.
                // if we don't disable the auto commit, then after executing each query, the result will be committed in
                // DB and we will not be able to rollback the query
                connection.setAutoCommit(false);
                String sqlQuery = "";
                while(true) {
                    // getting sql query to be executed from the user and executing it, which will not be committed as
                    // auto commit is disabled
                    System.out.print("Enter Non-select SQL query :");
                    sc.nextLine();
                    sqlQuery = sc.nextLine();
                    statement.executeUpdate(sqlQuery);
                    System.out.println("Do you want to execute one more query?[Yes/No]:");
                    if(sc.next().equalsIgnoreCase("no")) break;
                }
                // once all the query executed successfully, committing the transaction
                connection.commit();
                connection.close();
                System.out.println("All the query executed successfully");
            }
        }
        catch(SQLException e1) {
            System.out.println("Error occurred while executing one of the query. Hence rolling back all the query");
            try{
                // in case there is any error while executing the queries, rollback the transaction
                if(connection!=null) connection.rollback();
            }
            catch(SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static void transactionManagementUsingSavepoint() {
        Connection connection = null;
        try {
            connection = getConnection();
            // disabling auto commit so that we can do rollback all the query operation in case there is any error
            // while executing the queries.
            // if we don't disable the auto commit, then after executing each query, the result will be committed in
            // DB and we will not be able to rollback the query
            connection.setAutoCommit(false);
            // getting employee records value from the user and inserting it into table
            String sqlQuery1 = "INSERT INTO employee VALUES (?, ?, ?, ?)";
            try(PreparedStatement preparedStatement1 = connection.prepareStatement(sqlQuery1)) {
                System.out.println("Entering records into 'employee' table :");
                while(true) {
                    System.out.print("Enter ID :");
                    int id = sc.nextInt();
                    System.out.print("Enter Name:");
                    String name = sc.next();
                    System.out.print("Enter Age :");
                    int age = sc.nextInt();
                    System.out.print("Enter Salary :");
                    float salary = sc.nextFloat();
                    preparedStatement1.setInt(1, id);
                    preparedStatement1.setString(2, name);
                    preparedStatement1.setInt(3, age);
                    preparedStatement1.setFloat(4, salary);
                    preparedStatement1.executeUpdate();
                    System.out.println("Do you want to insert one more record?[Yes/No]:");
                    if(sc.next().equalsIgnoreCase("no")) break;
                }
                // adding save point after inserting employee records into DB so that if there is any error while
                // performing any operation after this Savepoint, we don't need to rollback employee records instead we
                // just have to rollback till this Savepoint. This way we will preserve employee records
                Savepoint savepoint = connection.setSavepoint("savepoint");
                // getting student records value from the user and inserting it into table
                String sqlQuery2 = "INSERT INTO student VALUES (?, ?, ?, ?)";
                try(PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery2)) {
                    System.out.println("Entering records into 'student' table :");
                    while(true) {
                        System.out.print("Enter ID :");
                        int id = sc.nextInt();
                        System.out.print("Enter FirstName:");
                        String firstName = sc.next();
                        System.out.print("Enter LastName:");
                        String lastName = sc.next();
                        System.out.print("Enter Age :");
                        int age = sc.nextInt();
                        preparedStatement2.setInt(1, id);
                        preparedStatement2.setString(2, firstName);
                        preparedStatement2.setString(3, lastName);
                        preparedStatement2.setInt(4, age);
                        preparedStatement2.executeUpdate();
                        System.out.println("Do you want to insert one more record?[Yes/No]:");
                        if(sc.next().equalsIgnoreCase("no")) break;
                    }
                }
                catch (SQLException innerException) {
                    System.out.println("Error occurred while inserting student record. Hence rolling back the transaction till Savepoint");
                    // if something goes wrong while inserting student records, rollback the transaction till the Savepoint
                    connection.rollback(savepoint);
                }
                // if everything works fine, commit the changes
                connection.commit();
                connection.close();
                System.out.println("Records inserted successfully");
            }
        }
        catch(SQLException e1) {
            System.out.println("Error occurred while executing one of the query. Hence rolling back all the query");
            try{
                // in case there is any error while executing the queries, rollback the entire transaction
                if(connection!=null) connection.rollback();
            }
            catch(SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static void readUncommittedIsolation() {
        Connection connection1 = null;
        Connection connection2 = null;
        try {
            // creating first transaction
            connection1 = getConnection();
            connection1.setAutoCommit(false);
            // creating second transaction and setting its Isolations level as Read Uncommitted
            // when reading records from second transaction, it will show uncommitted changes also made by first transaction
            connection2 = getConnection();
            connection2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            String sqlQuery1 = "INSERT INTO student VALUES (?, ?, ?, ?)";
            String sqlQuery2 = "SELECT * FROM student";
            try(PreparedStatement preparedStatement = connection1.prepareStatement(sqlQuery1)) {
                System.out.println("Entering records into 'student' table :");
                while(true) {
                    System.out.print("Enter ID :");
                    int id = sc.nextInt();
                    System.out.print("Enter FirstName:");
                    String firstName = sc.next();
                    System.out.print("Enter LastName:");
                    String lastName = sc.next();
                    System.out.print("Enter Age :");
                    int age = sc.nextInt();
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, firstName);
                    preparedStatement.setString(3, lastName);
                    preparedStatement.setInt(4, age);
                    preparedStatement.executeUpdate();
                    System.out.println("Do you want to insert one more record?[Yes/No]:");
                    if(sc.next().equalsIgnoreCase("no")) break;
                }
            }
            try(PreparedStatement preparedStatement = connection2.prepareStatement(sqlQuery2)) {
                System.out.println("Reading records from 'student' table :");
                // Since Isolation levels is set to Read Uncommitted, record inserted by first transaction will also be
                // reflected on reading the records
                ResultSet resultSet = preparedStatement.executeQuery();
                DisplayResultSet.display(resultSet);
            }
            // committing the changes made by first transaction after second transaction reads the record
            connection1.commit();
            if(connection1!=null) connection1.close();
            if(connection2!=null) connection2.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readCommittedIsolation() {
        Connection connection1 = null;
        Connection connection2 = null;
        try {
            // creating first transaction
            connection1 = getConnection();
            connection1.setAutoCommit(false);
            // creating second transaction and setting its Isolations level as Read Committed
            // when reading records from second transaction, it will show only committed changes made by first transaction
            // this way it solves Dirty ready problem
            connection2 = getConnection();
            connection2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            String sqlQuery1 = "INSERT INTO student VALUES (?, ?, ?, ?)";
            String sqlQuery2 = "SELECT * FROM student";
            try(PreparedStatement preparedStatement = connection1.prepareStatement(sqlQuery1)) {
                System.out.println("Entering records into 'student' table :");
                while(true) {
                    System.out.print("Enter ID :");
                    int id = sc.nextInt();
                    System.out.print("Enter FirstName:");
                    String firstName = sc.next();
                    System.out.print("Enter LastName:");
                    String lastName = sc.next();
                    System.out.print("Enter Age :");
                    int age = sc.nextInt();
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, firstName);
                    preparedStatement.setString(3, lastName);
                    preparedStatement.setInt(4, age);
                    preparedStatement.executeUpdate();
                    System.out.println("Do you want to insert one more record?[Yes/No]:");
                    if(sc.next().equalsIgnoreCase("no")) break;
                }
            }
            try(PreparedStatement preparedStatement = connection2.prepareStatement(sqlQuery2)) {
                System.out.println("Reading records from 'student' table :");
                // Since Isolation levels is set to Read Committed, record inserted by first transaction will not show up
                // on reading the records by second transaction
                ResultSet resultSet = preparedStatement.executeQuery();
                DisplayResultSet.display(resultSet);
            }
            // committing the changes made by first transaction after second transaction reads the record
            connection1.commit();
            if(connection1!=null) connection1.close();
            if(connection2!=null) connection2.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void repeatableReadIsolation() {
        Connection connection1 = null;
        Connection connection2 = null;
        try {
            // creating first transaction and setting its Isolations level as Repeatable Read
            // when executing select query in first transaction, due to Repeatable Read Isolation other transactions
            // can modify the table involved in first transaction but when reading those involved table again from
            // first transaction will not reflect the modification made by second transaction
            // This way it solves non-repeatable read problem but as the modification doesn't reflect, this Isolation
            // doesn't solve Phantom read problem
            connection1 = getConnection();
            connection1.setAutoCommit(false);
            connection1.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            // creating second transaction
            connection2 = getConnection();
            connection2.setAutoCommit(false);
            String sqlQuery1 = "SELECT * FROM student WHERE Age=?";
            String sqlQuery2 = "DELETE FROM student WHERE Age=?";
            String sqlQuery3 = "SELECT * FROM student";
            // reading records from 'student' table
            try(PreparedStatement preparedStatement = connection1.prepareStatement(sqlQuery1)) {
                System.out.println("Reading records from 'student' table :");
                System.out.print("Enter Age :");
                int age = sc.nextInt();
                preparedStatement.setInt(1, age);
                ResultSet resultSet = preparedStatement.executeQuery();
                DisplayResultSet.display(resultSet);
            }
            // making modification in 'student' table
            try(PreparedStatement preparedStatement = connection2.prepareStatement(sqlQuery2)) {
                System.out.println("Deleting records from 'student' table :");
                System.out.print("Enter Age :");
                int age = sc.nextInt();
                preparedStatement.setInt(1, age);
                preparedStatement.executeUpdate();

            }
            // again reading record from 'student' table using first transaction which will not reflect changes made by
            // second transaction
            try(PreparedStatement preparedStatement = connection1.prepareStatement(sqlQuery3)) {
                System.out.println("Reading records from 'student' table :");
                ResultSet resultSet = preparedStatement.executeQuery();
                DisplayResultSet.display(resultSet);
            }
            // committing the changes made by first and second transaction
            connection1.commit();
            connection2.commit();
            if(connection1!=null) connection1.close();
            if(connection2!=null) connection2.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void serializableIsolation() {
        Connection connection1 = null;
        Connection connection2 = null;
        Connection connection3 = null;
        try {
            // creating first transaction and setting its Isolations level as Serializable
            // making first transaction as serializable will lock the table that is being accessed by this transaction
            // due to which other transaction can access other tables but not the tables that are locked
            // This way it solves Phantom read problem along with other Concurrency problems
            connection1 = getConnection();
            connection1.setAutoCommit(false);
            connection1.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            // creating second and third transaction
            connection2 = getConnection();
            connection2.setAutoCommit(false);
            connection3 = getConnection();
            connection3.setAutoCommit(false);
            String sqlQuery1 = "SELECT * FROM student WHERE Age=?";
            String sqlQuery2 = "DELETE FROM employee WHERE ID=?";
            String sqlQuery3 = "DELETE FROM student WHERE Age=?";
            // reading records from 'student' table will lock the table and will only be released when this transaction
            // will commit the changes
            try(PreparedStatement preparedStatement = connection1.prepareStatement(sqlQuery1)) {
                System.out.println("Reading records from 'student' table :");
                System.out.print("Enter Age :");
                int age = sc.nextInt();
                preparedStatement.setInt(1, age);
                ResultSet resultSet = preparedStatement.executeQuery();
                DisplayResultSet.display(resultSet);
            }
            // reading records from 'employee' table which will be successful as it is not locked
            try(PreparedStatement preparedStatement = connection2.prepareStatement(sqlQuery2)) {
                System.out.println("Deleting records from 'employee' table :");
                System.out.print("Enter ID :");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
            // reading records from 'student' table will fail as the 'student' table is locked
            try(PreparedStatement preparedStatement = connection3.prepareStatement(sqlQuery3)) {
                System.out.println("Deleting records from 'student' table :");
                System.out.print("Enter Age :");
                int age = sc.nextInt();
                preparedStatement.setInt(1, age);
                preparedStatement.executeUpdate();
            }
            // committing the changes made by all the transaction
            connection1.commit();
            connection2.commit();
            connection3.commit();
            if(connection1!=null) connection1.close();
            if(connection2!=null) connection2.close();
            if(connection3!=null) connection2.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        try{
            InputStream inputStream = Main16.class.getClassLoader().getResourceAsStream("db.properties");
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
