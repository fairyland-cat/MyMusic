package com.wang.mymusic.model;

/**
 * MyMusic
 * Created by wang on 2017.5.26.
 */

public class SongLocalBean {
    private int isMusic,duration;
    private String title,data,artist;

    public SongLocalBean(int isMusic, int duration, String title, String data, String artist) {
        this.isMusic = isMusic;
        this.duration = duration;
        this.title = title;
        this.data = data;
        this.artist = artist;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public int getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public String getArtist() {
        return artist;
    }
}
