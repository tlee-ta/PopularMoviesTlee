package com.example.android.popularmovies.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.model.Movie;


public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteDatabase database;

    public FavoriteViewModel(Application application) {
        super(application);
        database = FavoriteDatabase.getInstance(this.getApplication());
    }

    public LiveData<Movie> getFavMovie(int id) {
        return database.favoriteDao().loadFavoriteById(id);
    }
}
