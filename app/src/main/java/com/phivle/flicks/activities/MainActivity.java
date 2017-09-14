package com.phivle.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phivle.flicks.R;
import com.phivle.flicks.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MovieArrayAdapter;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    List<Movie> movies;
    ArrayAdapter<Movie> moviesAdapter;
    ListView lvMovies;

    final String api_url = "https://api.themoviedb.org/3/movie/now_playing";
    final String api_key = "a9ca642dc47483882bcb4b4748c60473";
    final String language = "en-US";
    final String page = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();

        updateDisplayedMovies();
    }

    private void updateDisplayedMovies() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api_key", api_key);
        params.put("language", language);
        params.put("page", page);

        client.get(api_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    updateMoviesAdapter();
                    Log.d("DEBUG", movieJsonResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
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
}
