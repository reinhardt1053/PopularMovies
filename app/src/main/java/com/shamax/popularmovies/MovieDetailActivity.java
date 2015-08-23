package com.shamax.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Movie movie = (Movie) getIntent().getParcelableExtra("movie");

        //Poster Image
        ImageView poster = (ImageView)findViewById(R.id.poster);
        Picasso.with(this)
                .load(Config.IMAGES_BASE_URL + movie.getPosterPath())
                .into(poster);

        //Original Title
        TextView originalTitle = (TextView)findViewById(R.id.original_title);
        originalTitle.setText(movie.getOriginalTitle());

        //Release Date
        TextView releaseDate = (TextView)findViewById(R.id.release_date);
        releaseDate.setText(movie.getReleaseDate());

        //Synopsis
        TextView plotSynopsis = (TextView)findViewById(R.id.plot);
        plotSynopsis.setText(movie.getPlotSynopsis());

        //Rating
        RatingBar userRating = (RatingBar)findViewById(R.id.user_rating);
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
