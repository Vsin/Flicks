package com.phivle.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    final String apiUrl = "https://api.themoviedb.org/3/movie/now_playing";
    final String apiKey = "a9ca642dc47483882bcb4b4748c60473";
    final String language = "en-US";
    final String page = "1";
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
    }

    private void updateDisplayedMovies() {
        OkHttpClient client;
        HttpUrl.Builder urlBuilder;
        String requestUrl;
        Request request;

        urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
        urlBuilder.addQueryParameter("api_key", apiKey);
        urlBuilder.addQueryParameter("language", language);
        urlBuilder.addQueryParameter("page", page);

        requestUrl = urlBuilder.build().toString();

        request = new Request.Builder().url(requestUrl).build();
        client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
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
