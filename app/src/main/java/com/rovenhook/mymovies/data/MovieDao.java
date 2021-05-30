package com.rovenhook.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id == :movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);



    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM favourite_movies WHERE id == :favouriteMovieId")
    FavouriteMovie getFavouriteMovieById(int favouriteMovieId);

    @Query("DELETE FROM favourite_movies")
    void deleteAllFavouriteMovies();

    @Insert
    void insertFavouriteMovie(FavouriteMovie favouriteMovie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);
}
