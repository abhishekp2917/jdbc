package org.example.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

// utility class to display records of ResultSet
public class DisplayResultSet {
    public static void display(ResultSet result) {
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
