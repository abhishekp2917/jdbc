/*
   Code to demonstrate how to execute stored procedure and functions using CallableStatement
*/

package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.utils.DisplayResultSet;
import java.sql.*;

public class Main8 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String USERNAME = "root";
    private static String PASSWORD = "password@123";
    public static void main(String[] args) {
        // creating 'DataSource' object using third-party API library
        BasicDataSource dataSource = new BasicDataSource();

        // setting JDBC properties
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        // Set additional properties
        // setting the maximum number of connections that the connection pool can create and manage
        dataSource.setMaxTotal(10);
        // the default auto-commit mode for connections in the pool to true.
        // Auto-commit mode determines whether each SQL statement executed within a connection is automatically
        // committed to the database
        dataSource.setDefaultAutoCommit(true);

        // creating connection using 'DataSource' object
        try(Connection connection = dataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                String sqlQuery = "SELECT * FROM student";
                ResultSet result = statement.executeQuery(sqlQuery);
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
}
