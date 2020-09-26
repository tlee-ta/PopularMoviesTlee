package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.database.MainFavoriteViewModel;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.JsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingBar;
    private TextView mErrorMessage;

    private final String sortPopular = "popular";
    private final String sortTop = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mLoadingBar = findViewById(R.id.loading_bar);
        mErrorMessage = findViewById(R.id.tv_error_message);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(sortPopular);
    }

    private void loadMovieData(String sortType){

        new FetchMoviesTask().execute(sortType);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent = new Intent(context, destinationClass);

        intent.putExtra("movie", selectedMovie);
        startActivity(intent);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
            mLoadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {

            URL movieUrl = NetworkUtils.buildUrl(strings[0]);
            try {
                String movieDataResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);

                Movie[] jsonMovieData = JsonUtils.parseMovieJson(movieDataResponse);

                return jsonMovieData;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingBar.setVisibility(View.INVISIBLE);
            if (movieData != null)
            {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(movieData);
            }
            else {
                mErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular)
        {
            loadMovieData(sortPopular);
            return true;
        }
        else if (id == R.id.sort_top)
        {
            loadMovieData(sortTop);
            return true;
        }
        else if (id == R.id.sort_favorite)
        {
            getAllFavorites();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllFavorites() {
        MainFavoriteViewModel mainFavoriteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainFavoriteViewModel.class);
        final LiveData<Movie[]> allFavorites = mainFavoriteViewModel.getFavorites();
        allFavorites.observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] favorites) {
                allFavorites.removeObserver(this);
                mMovieAdapter.setMovieData(favorites);
            }
        });
    }

}