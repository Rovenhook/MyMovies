package com.rovenhook.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.rovenhook.mymovies.adapters.MoviesAdapter;
import com.rovenhook.mymovies.data.MainViewModel;
import com.rovenhook.mymovies.data.Movie;
import com.rovenhook.mymovies.utils.JSONUtils;
import com.rovenhook.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private RecyclerView recyclerViewMovies;
    private MoviesAdapter moviesAdapter;
    private Chip chipPopular;
    private Chip chipTopRated;
    private Chip chipNewest;
    ProgressBar progressBarLoading;

    private MainViewModel mainViewModel;

    private static final int LOADER_ID = 133;
    LoaderManager loaderManager;

    private static int page = 1;
    private static boolean isLoading = false;

    private String sortBy = NetworkUtils.VALUE_SORT_BY_POPULARITY;

    private static String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lang = Locale.getDefault().getLanguage();

        loaderManager = LoaderManager.getInstance(this);

        chipPopular = findViewById(R.id.chipPopular);
        chipTopRated = findViewById(R.id.chipTopRated);
        chipNewest = findViewById(R.id.chipNewest);

        progressBarLoading = findViewById(R.id.progressBarLoading);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, getColumnCount()));

        moviesAdapter = new MoviesAdapter();
        recyclerViewMovies.setAdapter(moviesAdapter);
        setMovies();
        
        moviesAdapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = moviesAdapter.getMovies().get(position);
                Toast.makeText(MainActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });
        moviesAdapter.setOnReachEndListener(new MoviesAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    Toast.makeText(MainActivity.this, "Reached end", Toast.LENGTH_SHORT).show();
                    setMovies();
                }
            }
        });

        LiveData<List<Movie>> moviesFromLiveData = mainViewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
//                moviesAdapter.setMovies(movies);
                if (page == 1) {
                    moviesAdapter.setMovies(movies);
                }
            }
        });
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);

        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuItemMain:
//                Intent intentToMain = new Intent(this, MainActivity.class);
//                startActivity(intentToMain);
//                finish();
                break;

            case R.id.menuItemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                finish();
                break;

            case R.id.menuItemExit:
                moveTaskToBack(true);
                this.finishAffinity();
                finish();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onChipClick(View view) {
//        String sortBy;
        if (view == chipNewest) {
            sortBy = NetworkUtils.VALUE_SORT_BY_RELEASE_DATE;
            page = 1;
            Toast.makeText(this, "Release date", Toast.LENGTH_SHORT).show();
        } else if (view == chipTopRated) {
            sortBy = NetworkUtils.VALUE_SORT_BY_VOTE_AVERAGE;
            page = 1;
            Toast.makeText(this, "Vote average", Toast.LENGTH_SHORT).show();
        } else { //(view == chipPopular)
            sortBy = NetworkUtils.VALUE_SORT_BY_POPULARITY;
            page = 1;
            Toast.makeText(this, "Popularity", Toast.LENGTH_SHORT).show();
        }
        setMovies();
    }

    private void setMovies() {
        Log.i("page", "Current page - " + page);
        URL url = NetworkUtils.buildURL(sortBy, page, lang);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);


//        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(sortBy, page);
//        ArrayList<Movie> movies =  JSONUtils.getMoviesFromJSON(jsonObject);
//        if (movies != null && !movies.isEmpty()) {
//            mainViewModel.deleteAllMovies();
//            for (Movie movie : movies) {
//                mainViewModel.insertMovie(movie);
//            }
//        }
    }


    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle bundle) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, bundle);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(sortBy, page, lang);
        ArrayList<Movie> movies =  JSONUtils.getMoviesFromJSON(jsonObject);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                mainViewModel.deleteAllMovies();
                moviesAdapter.clear();
            }
            for (Movie movie : movies) {
                mainViewModel.insertMovie(movie);
            }
            moviesAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}