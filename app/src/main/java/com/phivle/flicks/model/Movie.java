package com.phivle.flicks.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    final static String IMG_URL_BASE = "https://image.tmdb.org/t/p/w%s/%s";
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    double voteAverage;
    double popularity;

    public enum MovieClassification {
        POPULAR, UNPOPULAR
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.popularity = jsonObject.getDouble("popularity");
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath(String width) {
        return String.format(IMG_URL_BASE, width, backdropPath);
    }

    public String getPosterPath(String width) {
        return String.format(IMG_URL_BASE, width, posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }
}
