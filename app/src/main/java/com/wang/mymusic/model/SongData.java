package com.wang.mymusic.model;

/**
 * MyMusic
 * Created by wang on 2017.5.22.
 */

public class SongData {
    private String song_id;
    private String title;
    private String author;
    private String pic_small;

    public SongData(String song_id, String title, String author, String imge) {
        this.song_id = song_id;
        this.title = title;
        this.author = author;
        this.pic_small = imge;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String imge) {
        this.pic_small = imge;
    }
}
