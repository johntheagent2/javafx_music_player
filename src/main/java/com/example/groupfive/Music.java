package com.example.groupfive;

public class Music {
    public String name;
    public double duration;

    public Music() {
    }

    public Music(String name, double duration) {
        this.name = name;
        this.duration = duration;
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
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
