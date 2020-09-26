package com.example.android.popularmovies.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase favInstance;
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "favorite_database";

    public static FavoriteDatabase getInstance(Context context) {
        if (favInstance == null) {
            synchronized (LOCK) {
                favInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteDatabase.class, DB_NAME)
                        .build();
            }
        }
        return favInstance;
    }

    public abstract FavoriteDao favoriteDao();
}
