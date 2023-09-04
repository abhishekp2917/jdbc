/*
   Code to demonstrate how to connect to DB and execute Select query to get the records in JDBC
*/

package org.example;

import java.sql.*;

public class Main1 {
    public static void main(String[] args) {
        // JDBC URL for MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
        // credentials for connecting to MySQL
        String username = "root";
        String password = "password@123";

        try {
            // Load the MySQL JDBC driver
            // we need to load the driver class explicitly because in our code we aren't using this class directly due
            // to which while compiling, JVM will not load this class automatically which will raise an exception
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if (connection != null) {

                String sql = "SELECT * FROM employee";
                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("employeeID");
                    String name = resultSet.getString("name");
                    double salary = resultSet.getDouble("salary");
                    System.out.println("ID: " + id + ", Name: " + name + ", Salary: " + salary);
                }

                resultSet.close();
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
