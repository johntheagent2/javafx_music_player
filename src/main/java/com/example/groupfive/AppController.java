package com.example.groupfive;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

public class AppController implements Initializable {
    public Label welcomeText;
    public Button createPlaylist;
    public ListView songListView;
    public ListView songView;
    @FXML
    private Label songLabel;

    @FXML
    private Label timeProgress;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Slider volumeController;
    private Timer timer;

    private TimerTask timerTask;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;
    private int songNumber;

    private boolean running;
    private String tempDirect = "src/main/java/com/example/groupfive/music";
    private DatabaseController conn;
    Stage stage;
    Scene scene;
    ResultSet savedChosenPlaylist;

    public AppController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPlaylist();
        setVolumeController();
    }

    public void showPlaylist(){
        try {
            conn = new DatabaseController();
            ResultSet rs = conn.getAllTableName();
            List<String> list = new ArrayList<>();
            while(rs.next()){
                list.add(rs.getString(1));
            }
            songListView.getItems().addAll(list);
            songListView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
                try {
                    songView.getItems().clear();
                    savedChosenPlaylist = chosenPlaylist((String) songListView.getSelectionModel().getSelectedItem());
                    showSongFromPlaylist((String) songListView.getSelectionModel().getSelectedItem());
                    runningMedia();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            System.out.println("Error!");
            throw new RuntimeException(e);
        }
    }

    public void showSongFromPlaylist(String savedChosenPlaylistToShow){
        try {
            conn = new DatabaseController();
            ResultSet rs = conn.getTable(savedChosenPlaylistToShow);
            songs = new ArrayList<>();
            while(rs.next()){
                songView.getItems().add(rs.getString(2));
            }
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

    public void runningMedia() throws SQLException {
        songs = new ArrayList<>();

        directory = new File(tempDirect);
        files = directory.listFiles();

        ArrayList<String> songsName = new ArrayList<>();

        if(savedChosenPlaylist != null){
            while(savedChosenPlaylist.next()){
                songsName.add(savedChosenPlaylist.getString(2));
            }
        }

        if(files != null){
            for(File i : files){
                for(String j : songsName){
                    if (i.getName().equals(j)){
                        songs.add(i);
                        System.out.println(i.getName() + " " +j);
                    }
                }
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
    }

    public void setVolumeController(){
        volumeController.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumeController.getValue() * 0.01));
    }

    @FXML
    protected void playMusic() {
        beginTimer();
        mediaPlayer.play();
    }
    @FXML
    protected void pauseMusic() {
        mediaPlayer.pause();
        cancelTimer();
    }
    @FXML
    protected void resetMusic() {
        progressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    @FXML
    protected void nextSong() {
        if(songNumber < songs.size() - 1){
            songNumber++;

        }else{
            songNumber = 0;
        }
        if (running) {
            cancelTimer();
        }
        beginTimer();
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        mediaPlayer.play();
    }

    @FXML
    protected void previousSong() {
        if(songNumber >0){
            songNumber--;

        }else{
            songNumber = songs.size()-1;

        }
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        mediaPlayer.play();
    }

    public void beginTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                String time = String.valueOf(current);
                progressBar.setProgress(current/end);

                if(current/end == 1){
                    cancelTimer();
                }

            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }

    public void importMusic() throws SQLException, IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));

        Stage stagePrimary = new Stage();
        File selectedDirectory = directoryChooser.showDialog(stagePrimary);

        conn = new DatabaseController();

//        songs.clear();

        if(selectedDirectory != null){
            directory = new File(selectedDirectory.getAbsolutePath());
        }
        files = directory.listFiles();

        ArrayList<File> importSongs = new ArrayList<>();

        for (File i : files) {
            System.out.println(i.getPath());
            File dest = new File("src\\main\\java\\com\\example\\groupfive\\music" + "\\" + i.getName());
            if(!dest.exists() && i.getName().endsWith((".mp3"))){
                Files.copy(Path.of(i.getPath()), dest.toPath());
                importSongs.add(new File(i.getName()));
                System.out.println(importSongs);
            }
        }

        if (files != null) {
            System.out.println("Connected");
            conn.addItemToDatabase(importSongs, "music");
        }
    }

    public void createPlaylist(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("createPlaylist.fxml")));

        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Create Playlist!");
        stage.setScene(scene);
        stage.show();
    }
}