package com.example.groupfive;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class AppController implements Initializable {
    public Label welcomeText;
    public Button createPlaylist;
    @FXML
    private Label songLabel;

    @FXML
    private Label timeProgress;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Slider volumeController;


    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost?useSSL=false", "root", "");

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
    private String direct;
    Stage stage;
    Scene scene;

    public AppController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();

        directory = new File(tempDirect);

        files = directory.listFiles();
        if(files != null){
            Collections.addAll(songs, files);
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());

        volumeController.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeController.getValue() * 0.01);
            }
        });
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

    public void chooseFolder() throws SQLException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));

        Stage stagePrimary = new Stage();
        File selectedDirectory = directoryChooser.showDialog(stagePrimary);

        songs.clear();
        directory = new File(selectedDirectory.getAbsolutePath());

        files = directory.listFiles();
        if(files != null){
            Collections.addAll(songs, files);


            System.out.println("Connected");

            Statement stm = conn.createStatement();
            stm.execute("USE MUSICLIST");
            String sql = "INSERT INTO MUSIC(NAME) VALUES(?)";
            for(int i = 0; i < songs.size(); i++){
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, songs.get(i).getName());
                System.out.println(i + " " + songs.get(i).getName());
                pstm.execute();
            }conn.close();
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
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