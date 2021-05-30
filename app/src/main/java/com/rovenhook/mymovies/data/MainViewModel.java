package com.rovenhook.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {
    private static MovieRoomDatabase movieRoomDatabase;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRoomDatabase = MovieRoomDatabase.getInstance(getApplication());
        movies = movieRoomDatabase.movieDao().getAllMovies();
        favouriteMovies = movieRoomDatabase.movieDao().getAllFavouriteMovies();
    }

    public Movie getMovieById(int movieId) {
        try {
            return new GetMovieTask().execute(movieId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void deleteAllMovies() {
        new DeleteMoviesTask().execute();
    }

    public void insertMovie(Movie movie) {
        new InsertMovieTask().execute(movie);
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask().execute(movie);
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return movieRoomDatabase.movieDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class DeleteMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            movieRoomDatabase.movieDao().deleteAllMovies();
            return null;
        }
    }

    private static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                movieRoomDatabase.movieDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                movieRoomDatabase.movieDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }








    public FavouriteMovie getFavouriteMovieById(int favouriteMovieId) {
        try {
            return new GetFavouriteMovieTask().execute(favouriteMovieId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        Log.i("link", "MainViewModel works");
        return favouriteMovies;
    }

    public void deleteAllFavouriteMovies() {
        new DeleteFavouriteMoviesTask().execute();
    }

    public void insertFavouriteMovie(FavouriteMovie favouriteMovie) {
        new InsertFavouriteMovieTask().execute(favouriteMovie);
    }

    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie) {
        new DeleteFavouriteMovieTask().execute(favouriteMovie);
    }
    private static class GetFavouriteMovieTask extends AsyncTask<Integer, Void, FavouriteMovie> {

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return movieRoomDatabase.movieDao().getFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class DeleteFavouriteMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            movieRoomDatabase.movieDao().deleteAllFavouriteMovies();
            return null;
        }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies != null && favouriteMovies.length > 0) {
                movieRoomDatabase.movieDao().insertFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }

    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies != null && favouriteMovies.length > 0) {
                movieRoomDatabase.movieDao().deleteFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }
}
