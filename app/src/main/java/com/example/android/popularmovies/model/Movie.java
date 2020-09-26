package com.example.android.popularmovies.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favMovies")
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;

    private String title;
    private String posterImage;
    private String originalTitle;
    private String voteAvg;
    private String overview;
    private String releaseDate;

    public Movie (){
    }

    public Movie(String title, String posterImage, String originalTitle, String voteAvg, String overview, String releaseDate, int id) {
        this.title = title;
        this.posterImage = posterImage;
        this.originalTitle = originalTitle;
        this.voteAvg = voteAvg;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
