package com.rovenhook.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rovenhook.mymovies.R;
import com.rovenhook.mymovies.data.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//github pushing works
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;


    private final String baseImageUrl = "https://image.tmdb.org/t/p/";
    private final String smallImage = "w185";
    private final String bigImage = "w780";

    public MoviesAdapter() {
        movies = new ArrayList<>();
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }
    public interface OnPosterClickListener {
        void onPosterClick(int position);
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        ImageView imageView = holder.imageViewSmallPoster;
        String imagePath = baseImageUrl + smallImage + movie.getPosterPath();
        Picasso.get().load(imagePath).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        if (movies.size() >= 20 && position >= movies.size() - 3 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPosterClickListener != null) {
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

}
