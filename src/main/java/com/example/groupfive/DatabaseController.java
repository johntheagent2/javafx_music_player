package com.example.groupfive;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {
    private final Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/musiclist", "root", "");
    private final Statement stmt = conn.createStatement();


    public DatabaseController() throws SQLException {
    }

    public void createTable(String tableName) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                " (ID INT PRIMARY KEY AUTO_INCREMENT , " +
                " NAME VARCHAR(255) NOT NULL UNIQUE)";
        stmt.execute(sql);
    }

    public ResultSet getTable(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement pstm = conn.prepareStatement(sql);
        return pstm.executeQuery();
    }

    public ResultSet getAllTableName() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("Show tables");
        return rs;
    }

    public void addItemToDatabase(ArrayList<File> songs, String tableName) throws SQLException {
        String sql = "INSERT INTO "+tableName+"(NAME) VALUES(?)";
        for(int i = 0; i < songs.size(); i++){
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, songs.get(i).getName());
            System.out.println(i + " " + songs.get(i).getName() + " added to database!");
            pstm.execute();
        }conn.close();
    }

    public void addSongsToDatabase(ArrayList<String> songs, String tableName) throws SQLException {
        String sql = "INSERT INTO "+tableName+"(NAME) VALUES(?)";
        for(int i = 0; i < songs.size(); i++){
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, songs.get(i));
            System.out.println(i + " " + songs.get(i) + " added to database!");
            pstm.execute();
        }conn.close();
    }

    public void deleteContentFromTable(String playlist, ArrayList<String> songs) throws SQLException {
        for(String i : songs){
            String sql="DELETE FROM "+ playlist + " WHERE NAME = '"+i+"'";
            System.out.println(sql);
            PreparedStatement something = conn.prepareStatement(sql);
            something.execute();
        }
    }


}
