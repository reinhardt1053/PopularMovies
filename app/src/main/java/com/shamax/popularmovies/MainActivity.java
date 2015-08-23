package com.shamax.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String MOVIES_KEY = "movies";

    List<Movie> movies;
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If the internet connection is lost and the phone get rotated I can fetch the previous stored state
        if (savedInstanceState != null)
        {
            movies = (List<Movie>)savedInstanceState.get(MOVIES_KEY);
        }
        else
           movies = new ArrayList<Movie>();

        mMoviesAdapter = new MoviesAdapter(this,movies);
        GridView moviesGrid = (GridView) findViewById(R.id.movies_grid);
        moviesGrid.setAdapter(mMoviesAdapter);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie movie = mMoviesAdapter.getItem(position);

                Intent detailIntent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                detailIntent.putExtra("movie", movie);
                startActivity(detailIntent);

            }
        });

    }

    private void updateMoviesList() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ordered_by = prefs.getString(getString(R.string.pref_movies_list_ordered_by_key),
                getString(R.string.pref_movies_list_ordered_by_default));
        fetchMoviesTask.execute(ordered_by);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<Movie>) movies);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String API_KEY_PARAM = "api_key";
                final String API_KEY_VALUE = Config.TMDB_API_KEY;
                final String SORT_BY_PARAM = "sort_by";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data.
            return null;
        }


        private List<Movie> getMoviesFromJson(String jsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_POSTER_PATH = "poster_path";

            JSONObject json = new JSONObject(jsonStr);
            JSONArray resultsArray = json.getJSONArray(TMDB_RESULTS);

            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                // Get the JSON object representing the day
                JSONObject movieJson = resultsArray.getJSONObject(i);

                Movie movie = new Movie();
                movie.setId(movieJson.getLong(TMDB_ID));
                movie.setPosterPath(movieJson.getString(TMDB_POSTER_PATH));
                movie.setOriginalTitle(movieJson.getString(TMDB_ORIGINAL_TITLE));
                movie.setPlotSynopsis(movieJson.getString(TMDB_OVERVIEW));
                movie.setReleaseDate(movieJson.getString(TMDB_RELEASE_DATE));
                movie.setVoteUserRating((float)movieJson.getDouble(TMDB_VOTE_AVERAGE));
                movies.add(movie);

            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mMoviesAdapter.clear();
                for(Movie movie : result) {
                    mMoviesAdapter.add(movie);
                }
            }
        }
    }



}
