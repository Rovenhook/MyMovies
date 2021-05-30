package com.rovenhook.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3//movie/%s/videos";
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3//movie/%s/reviews";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_MIN_VOTE_COUNT = "vote_count.gte";

    private static final String VALUE_API_KEY = "886ab9e2f6815e07889fffb5fb18ff27";
//    private static final String VALUE_LANGUAGE = "en-US";
    public static final String VALUE_SORT_BY_POPULARITY = "popularity.desc";
    public static final String VALUE_SORT_BY_VOTE_AVERAGE = "vote_average.desc";
    public static final String VALUE_SORT_BY_RELEASE_DATE = "release_date.desc";
    private static final String VALUE_MIN_VOTE_COUNT = "500";

    public static URL buildURLToVideos(int id, String lang) {
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, lang).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildURLToReviews(int id, String lang) {
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, lang).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONForVideos(int id, String lang) {
        JSONObject result = null;
        URL url = buildURLToVideos(id, lang);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForReviews(int id, String lang) {
        JSONObject result = null;
        URL url = buildURLToReviews(id, lang);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }





    public static URL buildURL(String sortBy, int page, String lang) {
        URL result = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, lang)
                .appendQueryParameter(PARAM_SORT_BY, sortBy)
                .appendQueryParameter(PARAM_MIN_VOTE_COUNT, VALUE_MIN_VOTE_COUNT)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("link", result.toString());
        return result;
    }

    public static JSONObject getJSONFromNetwork(String sortBy, int page, String lang) {
        JSONObject result = null;
        URL url = buildURL(sortBy, page, lang);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public interface OnStartLoadingListener {
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null) {
                return null;
            }
            String urlAsString = bundle.getString("url");
            URL url = null                    ;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) {
                return null;
            }
            HttpURLConnection httpURLConnection = null;
            JSONObject result = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                result = new JSONObject(sb.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return result;
        }
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            if (urls == null || urls.length == 0) {
                return null;
            }
            HttpURLConnection httpURLConnection = null;
            JSONObject result = null;
            try {
                httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                result = new JSONObject(sb.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return result;
        }
    }
}
