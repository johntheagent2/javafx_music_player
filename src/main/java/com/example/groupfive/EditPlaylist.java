package com.example.groupfive;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditPlaylist implements Initializable {

    private DatabaseController conn;

    public ListView songListView;
    public ListView songView;
    public ResultSet savedChosenPlaylist;
    private ArrayList<String> songs;


    Stage stage;
    Scene scene;

    public EditPlaylist(){

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPlaylist();
    }

    public void showPlaylist(){
        try {
            conn = new DatabaseController();
            ResultSet rs = conn.getAllTableName();
            List<String> list = new ArrayList<>();
            while(rs.next()){
                if(!rs.getString(1).equals("music"))
                    list.add(rs.getString(1));
            }
            songListView.getItems().addAll(list);
            songListView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
                try {
                    songView.getItems().clear();
                    savedChosenPlaylist = chosenPlaylist((String) songListView.getSelectionModel().getSelectedItem());
                    showSongFromPlaylist((String) songListView.getSelectionModel().getSelectedItem());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            System.out.println("Error!");
            throw new RuntimeException(e);
        }
    }

    public ResultSet chosenPlaylist(String playlistName) throws SQLException {
        conn = new DatabaseController();
        ResultSet rs = conn.getTable(playlistName);
        return rs;
    }

    public void showSongFromPlaylist(String savedChosenPlaylistToShow){
        try {
            conn = new DatabaseController();
            ResultSet rs = conn.getTable(savedChosenPlaylistToShow);
            songs = new ArrayList<>();
            while(rs.next()){
                songView.getItems().add(rs.getString(2));
                songView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(String item) {
                        BooleanProperty observable = new SimpleBooleanProperty();
                        observable.addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                songs.add(item);
                                System.out.println(item);
                            } else {
                                songs.remove(item);
                            }
                        });
                        return observable;
                    }
                }));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            throw new RuntimeException(e);
        }
    }

    public void removeSongs() throws SQLException {
        conn = new DatabaseController();
        String playlistName = (String) songListView.getSelectionModel().getSelectedItem();
        System.out.println(playlistName + ' ' + songs);
        conn.deleteContentFromTable(playlistName, songs);

    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
