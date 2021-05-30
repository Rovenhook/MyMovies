package com.rovenhook.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int uniqueId, int id, String posterPath, String backdropPath, String title,
                          String releaseDate, double popularity, long voteCount, double voteAverage, String overview) {
        super(uniqueId, id, posterPath, backdropPath, title, releaseDate, popularity, voteCount, voteAverage, overview);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getPosterPath(), movie.getBackdropPath(), movie.getTitle(), movie.getReleaseDate(),
                movie.getPopularity(), movie.getVoteCount(), movie.getVoteAverage(), movie.getOverview());
    }
}
