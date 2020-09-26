package com.example.android.popularmovies.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.model.Movie;


public class MainFavoriteViewModel extends AndroidViewModel {

    private LiveData<Movie[]> favorites;

    public MainFavoriteViewModel(Application application) {
        super(application);
        FavoriteDatabase database = FavoriteDatabase.getInstance(this.getApplication());
        favorites = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<Movie[]> getFavorites() {
        return favorites;
    }
}
