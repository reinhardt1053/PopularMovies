package com.shamax.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MoviesAdapter extends ArrayAdapter<Movie> {
    public MoviesAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Movie movie = getItem(position);

        Picasso.with(getContext())
                    .load(Config.IMAGES_BASE_URL + movie.getPosterPath())
                    .into(imageView);
        return imageView;
    }

}
