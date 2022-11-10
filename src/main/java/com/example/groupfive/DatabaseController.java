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
        String sql = "CREATE TABLE " +
                tableName +
                " (ID INT PRIMARY KEY AUTO_INCREMENT , " +
                " NAME VARCHAR(255))";
        stmt.execute(sql);
    }

    public ResultSet getTable(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement pstm = conn.prepareStatement(sql);
        return pstm.executeQuery();
    }

    public void addItemToDatabase(ArrayList<File> songs) throws SQLException {
        String sql = "INSERT INTO MUSIC(NAME) VALUES(?)";
        for(int i = 0; i < songs.size(); i++){
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, songs.get(i).getName());
            System.out.println(i + " " + songs.get(i).getName());
            pstm.execute();
        }conn.close();
    }


}