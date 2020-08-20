package com.example.android.popularmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String posterImage;
    private String originalTitle;
    private String voteAvg;
    private String overview;
    private String releaseDate;

    public Movie (){
    }

    public Movie(String title, String posterImage, String originalTitle, String voteAvg, String overview, String releaseDate) {
        this.title = title;
        this.posterImage = posterImage;
        this.originalTitle = originalTitle;
        this.voteAvg = voteAvg;
        this.overview = overview;
        this.releaseDate = releaseDate;
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
}
