package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.model.Movie;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favMovies")
    LiveData<Movie[]> loadAllFavorites();

    @Query("SELECT * FROM favMovies WHERE id = :id")
    LiveData<Movie> loadFavoriteById(int id);

    @Insert
    void insertFavMovie(Movie movie);

    @Delete
    void deleteFavMovie(Movie movie);
}
