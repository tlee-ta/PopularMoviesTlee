package com.example.android.popularmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.database.FavoriteDatabase;
import com.example.android.popularmovies.database.FavoriteViewModel;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utils.JsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String posterURL = "https://image.tmdb.org/t/p/w342/";

    ImageView ivPoster;
    TextView tvOriginalTitle;
    TextView tvSummary;
    TextView tvRating;
    TextView tvDate;

    RecyclerView rvReviews;
    ReviewAdapter reviewAdapter;
    TextView reviewUnavailable;

    RecyclerView rvTrailers;
    TrailerAdapter trailerAdapter;
    TextView trailerUnavailable;

    private final String reviews = "reviews";
    private final String trailers = "trailers";

    private FavoriteDatabase favDB;
    private ImageView btnFavorite;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent == null) {
            closeOnError();
        }

        if (intent.hasExtra("movie") ) {
            final Movie movie = (Movie) intent.getSerializableExtra("movie");

            if (movie == null)
            {
                closeOnError();
                return;
            }

            populateMovieUI(movie);
            setTitle(movie.getTitle());

            rvReviews = findViewById(R.id.rv_review_list);
            reviewUnavailable = findViewById(R.id.tv_review_unavailable);
            LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
            rvReviews.setLayoutManager(reviewLayoutManager);
            rvReviews.setHasFixedSize(true);
            reviewAdapter = new ReviewAdapter();
            rvReviews.setAdapter(reviewAdapter);

            new FetchReviewsTask().execute(movie);

            rvTrailers = findViewById(R.id.rv_trailer_list);
            trailerUnavailable = findViewById(R.id.tv_trailer_unavailable);
            LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
            rvTrailers.setLayoutManager(trailerLayoutManager);
            rvTrailers.setHasFixedSize(true);
            trailerAdapter = new TrailerAdapter(this);
            rvTrailers.setAdapter(trailerAdapter);

            new FetchTrailersTask().execute(movie);

            favDB = FavoriteDatabase.getInstance(getApplicationContext());

            FavoriteViewModel favViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavoriteViewModel.class);
            final LiveData<Movie> favorite = favViewModel.getFavMovie(movie.getId());
            favorite.observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie favMovie) {
                    favorite.removeObserver(this);
                    isFavorite = favMovie != null;
                    updateFavoriteUI();
                }
            });

            btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateFavorite(movie);
                }
            });
        }
    }

    public void updateFavorite(final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFavorite) {
                    favDB.favoriteDao().deleteFavMovie(movie);
                    isFavorite = false;
                }
                else {
                    favDB.favoriteDao().insertFavMovie(movie);
                    isFavorite = true;
                }
                updateFavoriteUI();
            }
        });
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.btn_star_on);
        }
        else {
            btnFavorite.setImageResource(R.drawable.btn_star_off);
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
            String total_rating = movie.getVoteAvg() + getString(R.string.rating_total);
            tvRating.setText(total_rating);
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

    @Override
    public void OnClick(Trailer selectedTrailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse("https://youtu.be/" + selectedTrailer.getSource()));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class FetchReviewsTask extends AsyncTask<Movie, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvReviews.setVisibility(View.INVISIBLE);
            reviewUnavailable.setVisibility(View.GONE);
        }

        @Override
        protected Review[] doInBackground(Movie... movies) {
            String reviewStr = movies[0].getId() + "/" + reviews;
            URL reviewsUrl = NetworkUtils.buildUrl(reviewStr);
            try{
                String reviewDataResponse = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);

                Review[] jsonReviewData = JsonUtils.parseReviewJson(reviewDataResponse);

                return jsonReviewData;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews != null && reviews.length > 0)
            {
                rvReviews.setVisibility(View.VISIBLE);
                reviewAdapter.setReviewData(reviews);
            }
            else if (reviews.length == 0)
            {
                reviewUnavailable.setVisibility(View.VISIBLE);
                reviewUnavailable.setText(R.string.no_reviews);
            }
            else
            {
                reviewUnavailable.setVisibility(View.VISIBLE);
                reviewUnavailable.setText(R.string.reviews_unavailable);
            }
        }
    }

    private class FetchTrailersTask extends AsyncTask<Movie, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvTrailers.setVisibility(View.INVISIBLE);
            trailerUnavailable.setVisibility(View.GONE);
        }

        @Override
        protected Trailer[] doInBackground(Movie... movies) {
            String trailerStr = movies[0].getId() + "/" + trailers;
            URL trailersUrl = NetworkUtils.buildUrl(trailerStr);
            try{
                String trailerDataResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);

                Trailer[] jsonTrailerData = JsonUtils.parseTrailerJson(trailerDataResponse);

                return jsonTrailerData;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers != null && trailers.length > 0)
            {
                rvTrailers.setVisibility(View.VISIBLE);
                trailerAdapter.setTrailerData(trailers);
            }
            else if (trailers.length == 0)
            {
                trailerUnavailable.setVisibility(View.VISIBLE);
                trailerUnavailable.setText(R.string.no_trailers);
            }
            else
            {
                trailerUnavailable.setVisibility(View.VISIBLE);
                trailerUnavailable.setText(R.string.trailers_unavailable);
            }
        }
    }
}