package com.example.groupfive;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreatePlaylistController implements Initializable {

    @FXML
    private ListView<String> myListView;

    Stage stage;
    Scene scene;

    @FXML
    private TextField playlistNaming;

    private DatabaseController conn;

    public CreatePlaylistController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            conn = new DatabaseController();
            ResultSet rs = conn.getTable("music");

            while(rs.next()){
                myListView.getItems().add(rs.getInt(1) + " " + rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            throw new RuntimeException(e);
        }
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void creatPlaylistButton() throws SQLException {
        String playlistName = playlistNaming.getText().replace(" ", "_");
        conn.createTable(playlistName);
        System.out.println("Created " + playlistName +" table in database...");
    }
}