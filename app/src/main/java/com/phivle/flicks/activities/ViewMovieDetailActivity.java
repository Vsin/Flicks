package com.phivle.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phivle.flicks.R;

/**
 * Created by Vsin on 9/15/17.
 */

public class ViewMovieDetailActivity extends AppCompatActivity {

    private String viewMovieDetailOverview;
    private double viewMovieDetailPopularity;
    private double viewMovieDetailVoteAverage;
    private static final double DEFAULT_DOUBLE = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie_detail);

        viewMovieDetailOverview = getIntent().getStringExtra("overview");
        viewMovieDetailPopularity = getIntent().getDoubleExtra("popularity", DEFAULT_DOUBLE);
        viewMovieDetailVoteAverage = getIntent().getDoubleExtra("vote_average", DEFAULT_DOUBLE);

    }
}
