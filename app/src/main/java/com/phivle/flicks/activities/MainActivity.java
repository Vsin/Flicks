package com.phivle.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phivle.flicks.R;
import com.phivle.flicks.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MovieArrayAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final static String API_URL = "https://api.themoviedb.org/3/movie/now_playing";
    final static String API_KEY = "a9ca642dc47483882bcb4b4748c60473";
    final static String LANGUAGE = "en-US";
    final static String PAGE = "1";
    List<Movie> movies;
    ArrayAdapter<Movie> moviesAdapter;
    ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();

        updateDisplayedMovies();
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Movie viewMovieDetail = movies.get(pos);
                launchMovieDetailIntent(viewMovieDetail, pos);
            }
        });
    }

    private void launchMovieDetailIntent(Movie viewMovieDetail, int pos) {
        Intent viewMovieDetailIntent = new Intent(MainActivity.this, ViewMovieDetailActivity.class);
        viewMovieDetailIntent.putExtra("title", viewMovieDetail.getPopularity());
        viewMovieDetailIntent.putExtra("popularity", viewMovieDetail.getPopularity());
        viewMovieDetailIntent.putExtra("rating", viewMovieDetail.getVoteAverage());
        viewMovieDetailIntent.putExtra("overview", viewMovieDetail.getOverview());
    }

    private Request buildNowPlayingRequest() {
        HttpUrl.Builder urlBuilder;
        String requestUrl;

        urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("language", LANGUAGE);
        urlBuilder.addQueryParameter("page", PAGE);

        requestUrl = urlBuilder.build().toString();

        return new Request.Builder().url(requestUrl).build();
    }
    private void updateMoviesAdapter() {
        if (moviesAdapter == null) {
            moviesAdapter = new MovieArrayAdapter(this, movies);
            lvMovies.setAdapter(moviesAdapter);
        } else {
            moviesAdapter.clear();
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
        }
    }

    private void updateDisplayedMovies() {
        OkHttpClient client;
        Request nowPlayingRequest;

        nowPlayingRequest = buildNowPlayingRequest();
        client = new OkHttpClient();

        client.newCall(nowPlayingRequest).enqueue(new Callback() {
            String movieJsonResponse;
            JSONArray movieResults;

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                movieJsonResponse = response.body().string();

                try {
                    JSONObject responseJson = new JSONObject(movieJsonResponse);
                    movieResults = responseJson.getJSONArray("results");
                    Log.d("test", movieResults.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("test", movieResults.toString());
                        movies.addAll(Movie.fromJsonArray(movieResults));
                        updateMoviesAdapter();
                    }

                });
            }
        });
    }


}
