package org.example;

import javax.sql.RowSet;
import java.sql.*;
import java.util.*;

public class Main3 {

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/temp";
    private static String username = "root";
    private static String password = "password@123";
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select any of the below operation : ");
            System.out.println("1. Create a Table");
            System.out.println("2. Drop a Table");
            System.out.println("3. Insert into a Table");
            System.out.println("4. Update record of a Table");
            System.out.println("5. Delete from a Table");
            System.out.println("6. Select from a Table");
            System.out.println("7. Manually enter a query");
            System.out.println("8. Execute Select query");
            System.out.println("9. Execute Non-Select query");
            System.out.println("10. Exit");
            System.out.println("_________________________________________________\n");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 : {
                    createTable();
                    break;
                }
                case 2 : {
                    dropTable();
                    break;
                }
                case 3 : {
                    insert();
                    break;
                }
                case 4 : {
                    update();
                    break;
                }
                case 5 : {
                    delete();
                    break;
                }
                case 6 : {
                    select();
                    break;
                }
                case 7 : {
                    executeQuery();
                    break;
                }
                case 8 : {
                    executeSelectQuery();
                    break;
                }
                case 9 : {
                    executeNonSelectQuery();
                    break;
                }
                case 10 : {
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

    public static void createTable() {
        System.out.print("Enter Table name : ");
        String tableName = sc.next();

        System.out.print("Enter number of columns : ");
        int colCount = sc.nextInt();

        ArrayList<String> columns = new ArrayList<>();
        String columnDetails = "";

        for(int i=0; i<colCount; i++) {
            System.out.print("Enter column name : ");
            String colName = sc.next();

            System.out.println("Select column type : ");
            System.out.println("1. INT");
            System.out.println("2. VARCHAR");
            String colType = "";

            switch (sc.nextInt()) {
                case 1: {
                    colType = "INT";
                    break;
                }
                case 2: {
                    System.out.print("Enter VARCHAR size : ");
                    colType = String.format("VARCHAR(%d)", sc.nextInt());
                    break;
                }
            }
            columnDetails += String.format("%s %s,", colName, colType);
            columns.add(colName);
        }

        String sqlQuery = String.format("CREATE TABLE %s (%s)", tableName.toLowerCase(), columnDetails.substring(0, columnDetails.length()-1));
        System.out.println(String.format("SQL Query : %s", sqlQuery));

        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlQuery);
                System.out.println(String.format("'%s' table created successfully", tableName.toLowerCase()));
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

    public static void dropTable() {
        System.out.print("Enter table name to be dropped : ");
        String tableName = sc.next();
        if(getColumnList(tableName).size()>0) {
            String sqlQuery = String.format("DROP TABLE %s", tableName.toLowerCase());
            System.out.println(String.format("SQL Query : %s", sqlQuery));
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try(Statement statement = connection.createStatement()) {
                    statement.executeUpdate(sqlQuery);
                    System.out.println(String.format("Table '%s' dropped successfully", tableName.toLowerCase()));
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
        else System.out.println("Table doesn't exists.");
    }

    public static void insert() {
        System.out.print("Enter table name in which record to be entered : ");
        String tableName = sc.next();
        HashMap<String, String> columnList = getColumnList(tableName);
        if(columnList.size()>0) {
            boolean exit = false;
            String colNames = "";
            for(String column : columnList.keySet()) {
                colNames += String.format("%s,", column);
            }
            String rows = "";
            System.out.println("Enter columns value : ");
            while(!exit) {
                String rowValues = "";
                System.out.println("\n_________________________________________________");
                for(String column : columnList.keySet()) {
                    System.out.print(String.format("%s : ", column));
                    String value = sc.next();
                    if(columnList.get(column).toUpperCase().equalsIgnoreCase("VARCHAR")) rowValues += String.format("'%s',", value);
                    else rowValues += String.format("%s,", value);
                }
                System.out.println("_________________________________________________\n");
                rows += String.format("(%s),", rowValues.substring(0, rowValues.length()-1));

                System.out.print("Wants to provide another record? [YES/NO] : ");
                if(sc.next().equalsIgnoreCase("NO")) break;
            }
            String sqlQuery = String.format("INSERT INTO %s (%s) VALUES %s", tableName.toLowerCase(), colNames.substring(0, colNames.length()-1), rows.substring(0, rows.length()-1));
            System.out.println(String.format("SQL Query : %s", sqlQuery));
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                // disabling auto commit due to which after executing any DML query using this connection,
                // we must commit the changes else changes will not reflect in DB
                connection.setAutoCommit(false);
                try(Statement statement = connection.createStatement()) {
                    int rowsEffected = statement.executeUpdate(sqlQuery);
                    // committing the changes
                    connection.commit();
                    System.out.println(String.format("%d rows inserted into table '%s' successfully", rowsEffected, tableName.toLowerCase()));
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
        else System.out.println("Table doesn't exists.");
    }

    public static void update() {
        System.out.print("Enter table name whose record to be updated : ");
        String tableName = sc.next();
        HashMap<String, String> columnMap = getColumnList(tableName);
        ArrayList<String> columnList = new ArrayList<>(columnMap.keySet());
        if(columnList.size()>0) {
            System.out.println("\n_________________________________________________");
            System.out.println("Select column for update criteria : ");
            for(int i=1; i<=columnList.size(); i++) {
                System.out.println(String.format("%d. %s", i, columnList.get(i-1)));
            }
            System.out.println("_________________________________________________\n");
            String selectedColumn = columnList.get(sc.nextInt()-1);
            System.out.print(String.format("Enter value for column '%s': ", selectedColumn));
            String selectedColumnValue = columnMap.get(selectedColumn).equalsIgnoreCase("VARCHAR")? String.format("'%s'", sc.next()) : sc.next();
            System.out.println("\n_________________________________________________");
            System.out.println("Select column whose value to be updated : ");
            for(int i=1; i<=columnList.size(); i++) {
                System.out.println(String.format("%d. %s", i, columnList.get(i-1)));
            }
            System.out.println("_________________________________________________\n");
            String column = columnList.get(sc.nextInt()-1);
            System.out.print(String.format("Enter new value for column '%s': ", column));
            String columnValue = columnMap.get(column).equalsIgnoreCase("VARCHAR")? String.format("'%s'", sc.next()) : sc.next();
            String sqlQuery = String.format("UPDATE %s SET %s=%s WHERE %s=%s", tableName.toLowerCase(), column.toLowerCase(), columnValue.toLowerCase(), selectedColumn.toLowerCase(), selectedColumnValue.toLowerCase());
            System.out.println(String.format("SQL Query : %s", sqlQuery));
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try(Statement statement = connection.createStatement()) {
                    int rowsEffected = statement.executeUpdate(sqlQuery);
                    System.out.println(String.format("%d rows updated of table '%s' successfully", rowsEffected, tableName.toLowerCase()));
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
        else System.out.println("Table doesn't exists.");
    }

    public static void delete() {
        System.out.print("Enter table name from which record to be deleted : ");
        String tableName = sc.next();
        HashMap<String, String> columnList = getColumnList(tableName);
        if(columnList.size()>0) {
            ArrayList<String> columns = new ArrayList<>(columnList.keySet());
            System.out.println("\n_________________________________________________");
            System.out.println("Select a column as a deletion criteria  : ");
            for(int i=1; i<=columns.size(); i++) {
                System.out.println(String.format("%d. %s", i, columns.get(i-1)));
            }
            System.out.println("_________________________________________________\n");
            String colName = columns.get(sc.nextInt()-1);
            System.out.print(String.format("Enter %s : ", colName));
            String colvalue = sc.next();
            if(columnList.get(colName).equalsIgnoreCase("VARCHAR")) colvalue = String.format("'%s'", colvalue);
            String sqlQuery = String.format("DELETE FROM %s WHERE %s=%s", tableName.toLowerCase(), colName.toLowerCase(), colvalue.toLowerCase());
            System.out.println(String.format("SQL Query : %s", sqlQuery));
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try(Statement statement = connection.createStatement()) {
                    int rowsEffected = statement.executeUpdate(sqlQuery);
                    System.out.println(String.format("%d rows deleted successfully from table '%s'", rowsEffected, tableName.toLowerCase()));
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
        else System.out.println("Table doesn't exists.");
    }

    public static void select() {
        System.out.print("Enter table name whose records you want to view : ");
        String tableName = sc.next();
        HashMap<String, String> columnList = getColumnList(tableName);
        if(columnList.size()>0) {
            String sqlQuery = String.format("SELECT * FROM %s", tableName.toLowerCase());
            System.out.println(String.format("SQL Query : %s", sqlQuery));
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try(Statement statement = connection.createStatement()) {
                    ResultSet result = statement.executeQuery(sqlQuery);
                    displayResultSet(result);
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
        else System.out.println("Table doesn't exists.");
    }

    public static void executeQuery() {
        System.out.println("Enter a query to be executed :");
        sc.nextLine();
        String sqlQuery = sc.nextLine();
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try(Statement statement = connection.createStatement()) {
                if(statement.execute(sqlQuery)) {
                    ResultSet result = statement.executeQuery(sqlQuery);
                    displayResultSet(result);
                }
                else {
                    int rowsEffected = statement.executeUpdate(sqlQuery);
                    System.out.println(String.format("%d rows effected.", rowsEffected));
                }
            }
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    public static void executeSelectQuery() {
        System.out.println("Enter a Select query to be executed :");
        sc.nextLine();
        String sqlQuery = sc.nextLine();
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try(Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(sqlQuery);
                displayResultSet(result);
            }
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    public static void executeNonSelectQuery() {
        System.out.println("Enter a Non-Select query to be executed :");
        sc.nextLine();
        String sqlQuery = sc.nextLine();
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try(Statement statement = connection.createStatement()) {
                int rowsEffected = statement.executeUpdate(sqlQuery);
                System.out.println(String.format("%d rows effected.", rowsEffected));
            }
        }
        catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    private static HashMap<String, String> getColumnList(String tableName) {
        HashMap<String, String> columnsList = new HashMap<>();
        String sqlQuery = String.format("SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'", tableName.toLowerCase());
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try(Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(sqlQuery);
                while (result.next()) {
                    columnsList.put(result.getString(1), result.getString(2));
                }
            }
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Incorrect SQL Syntax.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        return columnsList;
    }

    private static void displayResultSet(ResultSet result) {
        try{

            // ResultSetMetaData hold meta data i.e. data about the data (result set) such as
            // column count, column name of a specific index etc
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnMaxSize = new int[columnCount];
            for(int i=1; i<=columnCount; i++) {
                columnMaxSize[i-1] = Math.max(columnMaxSize[i-1], metaData.getColumnName(i).length());
            }
            ArrayList<String[]> records = new ArrayList<>();
            while(result.next()) {
                String[] record = new String[columnCount];
                for(int i=1; i<=columnCount; i++) {
                    record[i-1] = result.getString(i);
                    columnMaxSize[i-1] = Math.max(columnMaxSize[i-1], record[i-1].length());
                }
                records.add(record);
            }
            System.out.println("\n_________________________________________________");
            for(int i=1; i<=columnCount; i++) {
                int desiredWidth = columnMaxSize[i-1];
                String stringWithTrailingSpaces = String.format("%-" + desiredWidth + "s", metaData.getColumnName(i));
                System.out.print(String.format("\t%s\t", stringWithTrailingSpaces));
            }
            System.out.println("\n_________________________________________________");
            for(String[] record : records) {
                for(int i=0; i<record.length; i++) {
                    int desiredWidth = columnMaxSize[i];
                    String stringWithTrailingSpaces = String.format("%-" + desiredWidth + "s", record[i]);
                    System.out.print(String.format("\t%s\t", stringWithTrailingSpaces));
                }
                System.out.println();
            }
            System.out.println("_________________________________________________\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
