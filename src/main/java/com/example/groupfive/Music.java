package com.example.groupfive;

public class Music {
    public int songNumber;
    public String name;
    public double duration;

    public Music() {
    }

    public Music(int songNumber, String name, double duration) {
        this.songNumber = songNumber;
        this.name = name;
        this.duration = duration;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Music{" +
                "songNumber=" + songNumber +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
