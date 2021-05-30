package com.rovenhook.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rovenhook.mymovies.adapters.MoviesAdapter;
import com.rovenhook.mymovies.data.FavouriteMovie;
import com.rovenhook.mymovies.data.MainViewModel;
import com.rovenhook.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    RecyclerView recyclerViewFavouriteMovies;
    MoviesAdapter moviesAdapter;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, getColumnCount()));

        moviesAdapter = new MoviesAdapter();
        recyclerViewFavouriteMovies.setAdapter(moviesAdapter);

        LiveData<List<FavouriteMovie>> favouriteMovies = mainViewModel.getFavouriteMovies();
        favouriteMovies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> movies) {
                if (movies != null) {
                    List<Movie> plainMovies = new ArrayList<>();
                    plainMovies.addAll(movies);
                    moviesAdapter.setMovies(plainMovies);
                }
            }
        });
        Toast.makeText(this, "Favourites", Toast.LENGTH_LONG).show();

        moviesAdapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = moviesAdapter.getMovies().get(position);
                Toast.makeText(FavouriteActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
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
                Intent intentToMain = new Intent(this, MainActivity.class);
                startActivity(intentToMain);
                finish();
                break;

            case R.id.menuItemFavourite:
//                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
//                startActivity(intentToFavourite);
//                finish();
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
}