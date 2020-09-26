package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

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
    private static final String ID = "id";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private static final String YOUTUBE = "youtube";
    private static final String NAME = "name";
    private static final String SOURCE = "source";

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
                int mId = 0;

                JSONObject movieDetails = movieList.getJSONObject(i);

                mTitle = movieDetails.optString(TITLE);
                mPoster = movieDetails.optString(POSTER_IMAGE);
                mOriginalTitle = movieDetails.optString(ORIGINAL_TITLE);
                mVotes = movieDetails.optString(VOTE_AVERAGE);
                mOverview = movieDetails.optString(OVERVIEW);
                mDate = movieDetails.optString(RELEASE_DATE);
                mId = movieDetails.optInt(ID);

                parsedMovieData[i] = new Movie();

                parsedMovieData[i].setTitle(mTitle);
                parsedMovieData[i].setPosterImage(mPoster);
                parsedMovieData[i].setOriginalTitle(mOriginalTitle);
                parsedMovieData[i].setVoteAvg(mVotes);
                parsedMovieData[i].setOverview(mOverview);
                parsedMovieData[i].setReleaseDate(mDate);
                parsedMovieData[i].setId(mId);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return parsedMovieData;
    }


    public static Review[] parseReviewJson (String json) {
        Review[] parsedReviewData = null;

        try {
            JSONObject reviewJson = new JSONObject(json);
            JSONArray reviewList = reviewJson.getJSONArray(RESULTS);
            parsedReviewData = new Review[reviewList.length()];

            for (int i = 0; i < reviewList.length(); i++) {

                String mAuthor = "";
                String mContent = "";

                JSONObject reviewDetails = reviewList.getJSONObject(i);

                mAuthor = reviewDetails.optString(AUTHOR);
                mContent = reviewDetails.optString(CONTENT);

                parsedReviewData[i] = new Review();

                parsedReviewData[i].setAuthor(mAuthor);
                parsedReviewData[i].setContent(mContent);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return parsedReviewData;
    }


    public static Trailer[] parseTrailerJson (String json) {
        Trailer[] parsedTrailerData = null;

        try {
            JSONObject trailerJson = new JSONObject(json);
            JSONArray trailerList = trailerJson.getJSONArray(YOUTUBE);
            parsedTrailerData = new Trailer[trailerList.length()];

            for (int i = 0; i < trailerList.length(); i++) {

                String mName = "";
                String mSource = "";

                JSONObject trailerDetails = trailerList.getJSONObject(i);

                mName = trailerDetails.optString(NAME);
                mSource = trailerDetails.optString(SOURCE);

                parsedTrailerData[i] = new Trailer();

                parsedTrailerData[i].setName(mName);
                parsedTrailerData[i].setSource(mSource);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return parsedTrailerData;
    }


}
