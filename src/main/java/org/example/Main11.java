/*
   Code to demonstrate how to deal with BLOB and CLOB type of file in JDBC
*/

package org.example;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main11 {

    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Insert BLOB into DB without using Buffer");
            System.out.println("2. Retrieve BLOB from DB without using Buffer");
            System.out.println("3. Retrieve BLOB from DB using Buffer");
            System.out.println("4. Insert CLOB into DB without using Buffer");
            System.out.println("5. Retrieve CLOB from DB without using Buffer");
            System.out.println("6. Retrieve CLOB from DB using Buffer");
            System.out.println("7. Exit");
            System.out.println("_________________________________________________\n");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    insertBLOBWithoutBuffer();
                    break;
                }
                case 2 : {
                    retrieveBLOBWithoutBuffer();
                    break;
                }
                case 3 : {
                    retrieveBLOBWithBuffer();
                    break;
                }
                case 4 : {
                    insertCLOBWithoutBuffer();
                    break;
                }
                case 5 : {
                    retrieveCLOBWithoutBuffer();
                    break;
                }
                case 6 : {
                    retrieveCLOBWithBuffer();
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

    private static void insertBLOBWithoutBuffer() {
        // Create InputStream object to read BLOB
        FileInputStream fileInputStream = null;
        try{
            // getting file path and passing it to InputStream object to read that file
            System.out.print("Enter file path :");
            String filePath = sc.next().replace("\\", "\\\\");
            fileInputStream = new FileInputStream(filePath);
            try(Connection connection = getConnection()) {
                String sqlQuery = "INSERT INTO person (Image) VALUES(?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                    // passing InputStream to PreparedStatement so that file will be read and will get inserted to DB
                    preparedStatement.setBinaryStream(1, fileInputStream);
                    // execute the PreparedStatement
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println(String.format("Number of rows effected : %d", rowsEffected));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void retrieveBLOBWithoutBuffer() {
        // Create InputStream to read the file that we will get from the DB
        InputStream inputStream = null;
        // Create FileOutputStream to write the file that was read using InputStream
        FileOutputStream fileOutputStream = null;
        try(Connection connection = getConnection()) {
            String sqlQuery = "SELECT * FROM person WHERE ID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                System.out.print("Enter ID : ");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                // execute the PreparedStatement
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    // get the InputStream from the ResultSet after executing the PreparedStatement to read the BLOB
                    inputStream = resultSet.getBinaryStream(2);
                    System.out.print("Enter file name : ");
                    String fileName = sc.next();
                    fileOutputStream = new FileOutputStream(String.format("C:\\Users\\Abhishek.Pandey1\\Pictures\\Background\\%s", fileName));
                    // read the file using InputStream and then write it using FileOutputStream
                    int data;
                    while ((data = inputStream.read()) != -1) {
                        fileOutputStream.write(data);
                    }
                    fileOutputStream.flush();
                    System.out.println(String.format("File %s read and saved successfully", fileName));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void retrieveBLOBWithBuffer() {
        // Create InputStream to read the file that we will get from the DB
        InputStream inputStream = null;
        // Create FileOutputStream to write the file that was read using InputStream
        FileOutputStream fileOutputStream = null;
        try(Connection connection = getConnection()) {
            String sqlQuery = "SELECT * FROM person WHERE ID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                System.out.print("Enter ID : ");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                // execute the PreparedStatement
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    // get the InputStream from the ResultSet after executing the PreparedStatement to read the BLOB
                    inputStream = resultSet.getBinaryStream(2);
                    System.out.print("Enter file name : ");
                    String fileName = sc.next();
                    fileOutputStream = new FileOutputStream(String.format("C:\\Users\\Abhishek.Pandey1\\Pictures\\Background\\%s", fileName));
                    // create byte array as a buffer and read the file in that buffer and then write it
                    // using FileOutputStream
                    byte[] buffer = new byte[1024];
                    while ((inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer);
                    }
                    fileOutputStream.flush();
                    System.out.println(String.format("File %s read and saved successfully", fileName));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertCLOBWithoutBuffer() {
        // Create FileReader object to read CLOB
        FileReader fileReader = null;
        try{
            // getting file path and passing it to FileReader object to read that file
            System.out.print("Enter file path :");
            String filePath = sc.next().replace("\\", "\\\\");
            fileReader = new FileReader(filePath);
            try(Connection connection = getConnection()) {
                String sqlQuery = "INSERT INTO person (Description) VALUES(?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                    // passing FileReader to PreparedStatement so that file will be read and will get inserted to DB
                    // since here stream size is set to 1024 Byte, only this much data will be inserted
                    preparedStatement.setCharacterStream(1, fileReader, 1024);
                    // execute the PreparedStatement
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println(String.format("Number of rows effected : %d", rowsEffected));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void retrieveCLOBWithoutBuffer() {
        // Create Reader to read the file that we will get from the DB
        Reader fileReader = null;
        // Create FileOutputStream to write the file that was read using Reader
        FileOutputStream fileOutputStream = null;
        try(Connection connection = getConnection()) {
            String sqlQuery = "SELECT * FROM person WHERE ID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                System.out.print("Enter ID : ");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                // execute the PreparedStatement
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    // get the Reader from the ResultSet after executing the PreparedStatement to read the CLOB
                    fileReader = resultSet.getCharacterStream(3);
                    System.out.print("Enter file name : ");
                    String fileName = sc.next();
                    fileOutputStream = new FileOutputStream(String.format("C:\\Users\\Abhishek.Pandey1\\Documents\\%s", fileName));
                    // read the file using Reader and then write it using FileOutputStream
                    int data;
                    while ((data = fileReader.read()) != -1) {
                        fileOutputStream.write(data);
                    }
                    fileOutputStream.flush();
                    System.out.println(String.format("File %s read and saved successfully", fileName));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void retrieveCLOBWithBuffer() {
        // Create Reader to read the file that we will get from the DB
        Reader fileReader = null;
        // Create FileWriter to write the file that was read using Reader
        FileWriter fileWriter = null;
        try(Connection connection = getConnection()) {
            String sqlQuery = "SELECT * FROM person WHERE ID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                System.out.print("Enter ID : ");
                int id = sc.nextInt();
                preparedStatement.setInt(1, id);
                // execute the PreparedStatement
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    // get the InputStream from the ResultSet after executing the PreparedStatement to read the BLOB
                    fileReader = resultSet.getCharacterStream(3);
                    System.out.print("Enter file name : ");
                    String fileName = sc.next();
                    fileWriter = new FileWriter(String.format("C:\\Users\\Abhishek.Pandey1\\Documents\\%s", fileName));
                    // create char array as a buffer and read the file in that buffer and then write it
                    // using fileWriter
                    char[] buffer = new char[1024];
                    while ((fileReader.read(buffer)) != -1) {
                        fileWriter.write(buffer);
                    }
                    fileWriter.flush();
                    System.out.println(String.format("File %s read and saved successfully", fileName));
                }
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        try{
            InputStream inputStream = Main11.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        return connection;
    }
}
