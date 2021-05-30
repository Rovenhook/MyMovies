package com.rovenhook.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rovenhook.mymovies.adapters.ReviewsAdapter;
import com.rovenhook.mymovies.adapters.TrailersAdapter;
import com.rovenhook.mymovies.data.FavouriteMovie;
import com.rovenhook.mymovies.data.MainViewModel;
import com.rovenhook.mymovies.data.Movie;
import com.rovenhook.mymovies.data.Review;
import com.rovenhook.mymovies.data.Trailer;
import com.rovenhook.mymovies.utils.JSONUtils;
import com.rovenhook.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private final String baseImageUrl = "https://image.tmdb.org/t/p/";
    private final String smallImage = "w185";
    private final String bigImage = "w780";

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewVotesCount;
    private TextView textViewVoteAverage;
    private TextView textViewReleaseDate;
    private TextView textViewOverview            ;
    private MainViewModel mainViewModel;
    private int id;
    Movie movie;
    FavouriteMovie favouriteMovie;
    ImageView imageViewFavouriteStar;

    RecyclerView recyclerViewTrailers;
    RecyclerView recyclerViewReviews;
    ReviewsAdapter reviewsAdapter;
    TrailersAdapter trailersAdapter;

    ScrollView scrollViewDetails;

    private static String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lang = Locale.getDefault().getLanguage();

 //       mainViewModel =

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewVotesCount = findViewById(R.id.textViewVotesCount);
        textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewFavouriteStar = findViewById(R.id.imageViewFavouriteStar);

        scrollViewDetails = findViewById(R.id.scrollViewDetails);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Log.i("link", "id = " + id);
        movie = mainViewModel.getMovieById(id);

        if (movie == null) {
            Log.i("link", "NULL POINTER MOVIE");
        }

        if (movie.getBigPosterPath() == null) {
            Log.i("link", "NULL POINTER BIGPOSTER PATH");
        }

        String imagePath = baseImageUrl + bigImage + movie.getBigPosterPath();
        Picasso.get().load(imagePath).placeholder(R.drawable.placeholder).into(imageViewBigPoster);
        Log.i("link", "past bigposter problem");
        Log.i("link", movie.getPosterPath());
        Log.i("link", movie.getBigPosterPath());
        Log.i("link", movie.getBackdropPath());
        textViewTitle.setText(movie.getTitle());
        textViewVotesCount.setText("" + movie.getVoteCount());
        textViewVoteAverage.setText("" + movie.getVoteAverage());
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
        setFavouriteImage();

        reviewsAdapter = new ReviewsAdapter();
        trailersAdapter = new TrailersAdapter();
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setAdapter(trailersAdapter);
        recyclerViewReviews.setAdapter(reviewsAdapter);
        JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(movie.getId(), lang);
        JSONObject jsonObjectReviews = NetworkUtils.getJSONForReviews(movie.getId(), lang);
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReviews);
        reviewsAdapter.setReviews(reviews);
        trailersAdapter.setTrailers(trailers);

        trailersAdapter.setOnTrailerClickListener(new TrailersAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailder = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailder);
            }
        });

        scrollViewDetails.smoothScrollTo(0, 0);
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

    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            mainViewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
        } else {
            mainViewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
        }
        setFavouriteImage();
    }

    private void setFavouriteImage() {
        favouriteMovie = mainViewModel.getFavouriteMovieById(movie.getId());
        if (favouriteMovie == null) {
            imageViewFavouriteStar.setImageResource(R.drawable.empty_star);
        } else {
            imageViewFavouriteStar.setImageResource(R.drawable.full_star);
        }
    }
}