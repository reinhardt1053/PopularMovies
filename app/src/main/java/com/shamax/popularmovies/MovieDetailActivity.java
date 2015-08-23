package com.shamax.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    @Bind(R.id.original_title) TextView originalTitle;
    @Bind(R.id.poster) ImageView poster;
    @Bind(R.id.release_date) TextView releaseDate;
    @Bind(R.id.plot) TextView plotSynopsis;
    @Bind(R.id.user_rating) RatingBar userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Display back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Set widgets variables
        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra("movie");

        Picasso.with(this)
                .load(Utility.IMAGES_BASE_URL + movie.getPosterPath())
                .into(poster);

        originalTitle.setText(movie.getOriginalTitle());
        releaseDate.setText(movie.getReleaseDate());
        plotSynopsis.setText(movie.getPlotSynopsis());
        userRating.setRating(movie.getVoteUserRating());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
