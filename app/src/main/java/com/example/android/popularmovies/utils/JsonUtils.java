package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String POSTER_IMAGE = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    public static Movie[] parseMovieJson (String json){

        Movie[] parsedMovieData = null;

        try{

            JSONObject movieJson = new JSONObject(json);
            JSONArray movieList = movieJson.getJSONArray(RESULTS);
            parsedMovieData = new Movie[movieList.length()];

            for (int i = 0; i < movieList.length(); i++) {

                String mTitle = "";
                String mPoster = "";
                String mOriginalTitle = "";
                String mVotes = "";
                String mOverview = "";
                String mDate = "";

                JSONObject movieDetails = movieList.getJSONObject(i);

                mTitle = movieDetails.optString(TITLE);
                mPoster = movieDetails.optString(POSTER_IMAGE);
                mOriginalTitle = movieDetails.optString(ORIGINAL_TITLE);
                mVotes = movieDetails.optString(VOTE_AVERAGE);
                mOverview = movieDetails.optString(OVERVIEW);
                mDate = movieDetails.optString(RELEASE_DATE);

                parsedMovieData[i] = new Movie();

                parsedMovieData[i].setTitle(mTitle);
                parsedMovieData[i].setPosterImage(mPoster);
                parsedMovieData[i].setOriginalTitle(mOriginalTitle);
                parsedMovieData[i].setVoteAvg(mVotes);
                parsedMovieData[i].setOverview(mOverview);
                parsedMovieData[i].setReleaseDate(mDate);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return parsedMovieData;
    }
}
