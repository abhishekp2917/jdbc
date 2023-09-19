/*
   Code to demonstrate how to use properties file for application configuration
*/

package org.example;

import org.example.utils.DisplayResultSet;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Main9 {
    public static void main(String[] args) {
        // creating Properties object which will be use to read '.properties' file values
        Properties properties = new Properties();
        InputStream inputStream;
        try{
            // loading the '.properties' file into class path using class loader which in turn will return an object of
            // type InputStream
            inputStream = Main9.class.getClassLoader().getResourceAsStream("db.properties");
            // loading the '.properties' file into Properties object
            properties.load(inputStream);
            // reading jdbc connection property from '.properties' file
            final String jdbcUrl = properties.getProperty("url");
            final String USERNAME = properties.getProperty("user");
            final String PASSWORD = properties.getProperty("password");
            // creating connection and Statement object and executing the query
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
}
