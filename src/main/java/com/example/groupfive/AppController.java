package com.example.groupfive;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AppController implements Initializable {
    public Label welcomeText;
    @FXML
    private Label songLabel;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();

        directory = new File("src/main/java/com/example/groupfive/music");

        files = directory.listFiles();
        if(files != null){
            for(File file : files){
                songs.add(file);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());
        mediaPlayer.play();
    }


    @FXML
    protected void playMusic() {
        mediaPlayer.play();
    }
    @FXML
    protected void pauseMusic() {
        mediaPlayer.pause();
    }
    @FXML
    protected void resetMusic() {
        mediaPlayer.seek(Duration.seconds(0));
    }

    @FXML
    protected void nextSong() {
        if(songNumber < songs.size() - 1){
            songNumber++;

        }else{
            songNumber = 0;

        }
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
}