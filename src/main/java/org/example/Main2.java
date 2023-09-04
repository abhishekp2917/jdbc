/*
   Code to demonstrate how to connect to DB and execute Non-Select query in JDBC
*/


package org.example;

import java.sql.*;

public class Main2 {
    public static void main(String[] args) {

        // JDBC URL for MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
        // credentials for connecting to MySQL
        String username = "root";
        String password = "password@123";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if (connection != null) {

                // SQL query to create a table
                String sqlQuery = "CREATE TABLE student (Id INT PRIMARY KEY, Name VARCHAR(20), Age INT)";

                // preparing statement
                Statement statement = connection.createStatement();

                // executing 'NON SELECT' query using executeUpdate method
                statement.executeUpdate(sqlQuery);

                System.out.println("Student table created successfully");

                statement.close();
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to MySQL database failed.");
            e.printStackTrace();
        }
    }
}
