package com.rovenhook.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

@Database(entities = {Movie.class, FavouriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieRoomDatabase extends androidx.room.RoomDatabase {
    private static MovieRoomDatabase movieRoomDatabase;
    private static final Object LOCK = new Object();

    public static MovieRoomDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (movieRoomDatabase == null) {
                movieRoomDatabase = Room.databaseBuilder(context, MovieRoomDatabase.class, "movies.db").fallbackToDestructiveMigration().build();
            }
        }
        return movieRoomDatabase;
    }

    public abstract MovieDao movieDao();
}
