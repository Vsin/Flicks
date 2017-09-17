package adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phivle.flicks.R;
import com.phivle.flicks.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Vsin on 9/12/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    final static int POPULAR = 0;
    final static int UNPOPULAR = 1;
    final static double AVERAGE_THRESHOLD = 5.0;

    private static class PopularMovieViewHolder {
        ImageView image;
    }

    private static class UnpopularMovieViewHolder {
        ImageView image;
        TextView title;
        TextView overview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return Movie.MovieClassification.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        Movie item = getItem(position);
        if (item.getVoteAverage() >= AVERAGE_THRESHOLD) {
            return Movie.MovieClassification.POPULAR.ordinal();
        } else {
            return Movie.MovieClassification.UNPOPULAR.ordinal();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie;
        int viewType;
        View v = convertView;

        movie = getItem(position);
        viewType = getItemViewType(position);

        switch (viewType) {
            case POPULAR:
                return getPopularMovieView(movie, v);
            case UNPOPULAR:
                return getUnpopularMovieView(movie, v);
            default:
        }

        return convertView;

    }

    @NonNull
    private View getPopularMovieView(Movie movie, View v) {
        PopularMovieViewHolder popularMovieViewHolder;
        if (v == null) {
            popularMovieViewHolder = new PopularMovieViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.popular_movie, null);
            popularMovieViewHolder.image = (ImageView) v.findViewById(R.id.ivMovieImage);
        } else {
            popularMovieViewHolder = (PopularMovieViewHolder) v.getTag();
        }

        popularMovieViewHolder.image.setImageResource(0);
        loadPopularViewholderImage(movie, popularMovieViewHolder);

        v.setTag(popularMovieViewHolder);

        return v;
    }

    @NonNull
    private View getUnpopularMovieView(Movie movie, View v) {
        UnpopularMovieViewHolder unpopularMovieViewHolder;
        if (v == null) {
            unpopularMovieViewHolder = new UnpopularMovieViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.unpopular_movie, null);

            unpopularMovieViewHolder.image = (ImageView) v.findViewById(R.id.ivMovieImage);
            unpopularMovieViewHolder.title = (TextView) v.findViewById(R.id.tvTitle);
            unpopularMovieViewHolder.overview = (TextView) v.findViewById(R.id.tvOverview);
        } else {
            unpopularMovieViewHolder = (UnpopularMovieViewHolder) v.getTag();
        }

        unpopularMovieViewHolder.image.setImageResource(0);
        loadUnpopularViewholderImage(movie, unpopularMovieViewHolder);

        unpopularMovieViewHolder.title.setText(movie.getOriginalTitle());
        unpopularMovieViewHolder.overview.setText(movie.getOverview());

        v.setTag(unpopularMovieViewHolder);

        return v;
    }

    private void loadPopularViewholderImage(Movie movie, PopularMovieViewHolder viewHolder) {
        Picasso.with(getContext()).load(movie.getBackdropPath("342")).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.image);
    }

    private void loadUnpopularViewholderImage(Movie movie, UnpopularMovieViewHolder viewHolder) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(movie.getBackdropPath("500")).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.image);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext()).load(movie.getPosterPath("342")).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.image);
        }
    }
}
