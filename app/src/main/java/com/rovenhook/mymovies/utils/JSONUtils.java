package com.rovenhook.mymovies.utils;

import com.rovenhook.mymovies.data.Movie;
import com.rovenhook.mymovies.data.Review;
import com.rovenhook.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    private static final String KEY_RESULTS = "results";

    // for reviews
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT= "content";

    // for trailers
    private static final String KEY_KEY_OF_VIDEO = "key";
    private static final String KEY_NAME= "name";
    private static final String BASE_YOUTUBE_URL= "https://www.youtube.com/watch?v=";

    // information about a movie
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_ID = "id";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_VOTE_COUNT = "vote_count";

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectReviews = jsonArray.getJSONObject(i);
                String author = objectReviews.getString(KEY_AUTHOR);
                String content = objectReviews.getString(KEY_CONTENT);

                result.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectTrailers = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + objectTrailers.getString(KEY_KEY_OF_VIDEO);
                String name = objectTrailers.getString(KEY_NAME);

                result.add(new Trailer(key, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                String posterPath = objectMovie.getString(KEY_POSTER_PATH);
                String backdropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                String title = objectMovie.getString(KEY_TITLE);
                String releaseDate = objectMovie.getString(KEY_RELEASE_DATE);
                double popularity = objectMovie.getDouble(KEY_POPULARITY);
                long voteCount = objectMovie.getLong(KEY_VOTE_COUNT);
                double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                String overview = objectMovie.getString(KEY_OVERVIEW);

                result.add(new Movie(id, posterPath, backdropPath, title,
                        releaseDate, popularity, voteCount, voteAverage, overview));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
