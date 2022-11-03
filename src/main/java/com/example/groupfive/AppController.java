package com.example.groupfive;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
    @FXML
    private ProgressBar progressBar;

    private Timer timer;

    private TimerTask timerTask;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;

    private boolean running;

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
        beginTimer();
    }


    @FXML
    protected void playMusic() {
        beginTimer();
        mediaPlayer.play();
    }
    @FXML
    protected void pauseMusic() {
        mediaPlayer.pause();
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
            if (running) {
                cancelTimer();
            }

        }else{
            songNumber = 0;
            if (running) {
                cancelTimer();
            }
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

    public void beginTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end);
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
}