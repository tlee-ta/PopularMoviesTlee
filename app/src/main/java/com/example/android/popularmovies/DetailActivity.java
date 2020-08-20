package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String posterURL = "https://image.tmdb.org/t/p/w342/";

    ImageView ivPoster;
    TextView tvOriginalTitle;
    TextView tvSummary;
    TextView tvRating;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent == null) {
            closeOnError();
        }

        if (intent.hasExtra("movie") ) {
            Movie movie = (Movie) intent.getSerializableExtra("movie");

            if (movie == null)
            {
                closeOnError();
                return;
            }

            populateMovieUI(movie);
            setTitle(movie.getTitle());
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateMovieUI(Movie movie) {
        ivPoster = findViewById(R.id.iv_movie_poster);
        tvOriginalTitle = findViewById(R.id.tv_movie_og_title);
        tvSummary = findViewById(R.id.tv_movie_summary);
        tvRating = findViewById(R.id.tv_movie_rating);
        tvDate = findViewById(R.id.tv_movie_date);

        if ( movie.getPosterImage() != null ) {
            Picasso.get().load(posterURL + movie.getPosterImage()).into(ivPoster);
        }

        if ( movie.getOriginalTitle() != null ) {
            tvOriginalTitle.setText(movie.getOriginalTitle());
        }
        else {
            tvOriginalTitle.setText(R.string.na);
        }

        if ( movie.getOverview() != null ) {
            tvSummary.setText(movie.getOverview());
        }
        else {
            tvSummary.setText(R.string.na);
        }

        if ( movie.getVoteAvg() != null ) {
            tvRating.setText(movie.getVoteAvg());
        }
        else {
            tvRating.setText(R.string.na);
        }

        if ( movie.getReleaseDate() != null ) {
            tvDate.setText(movie.getReleaseDate());
        }
        else {
            tvDate.setText(R.string.na);
        }

    }
}