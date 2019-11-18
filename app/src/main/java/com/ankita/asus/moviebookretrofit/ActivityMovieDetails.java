package com.ankita.asus.moviebookretrofit;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankita.asus.moviebookretrofit.Adapter.MovieTrailerAdapter;
import com.ankita.asus.moviebookretrofit.Database.PopulorMovieContract;
import com.ankita.asus.moviebookretrofit.models.TrailerData;
import com.ankita.asus.moviebookretrofit.models.Trailers;
import com.ankita.asus.moviebookretrofit.network.ApiClient;
import com.ankita.asus.moviebookretrofit.network.ApiInterface;
import com.ankita.asus.moviebookretrofit.utils.Constants;
import com.ankita.asus.moviebookretrofit.utils.helper;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMovieDetails extends AppCompatActivity {
    TextView tv_movie_title, tv_release_date, tv_overview, tv_censor_rating, tv_rating;
    ImageView iv_collapsing;
    RatingBar ratingBar;
    ProgressBar progressBar;
    RecyclerView rv_trailers;
    FloatingActionButton mfab_trailers, fab_favourite;
    private static int id;
    private static String mposter_path, mbackdrop_path, title, overview, release, success, failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tv_movie_title = findViewById(R.id.tv_movie_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_overview = findViewById(R.id.tv_overview);
        tv_censor_rating = findViewById(R.id.tv_censor_rating);
        tv_rating = findViewById(R.id.tv_rating);
        iv_collapsing = findViewById(R.id.iv_collapsing);
        ratingBar = findViewById(R.id.rating_bar);
        progressBar = findViewById(R.id.progress_bar);
        rv_trailers = findViewById(R.id.rv_trailers);
        mfab_trailers = findViewById(R.id.fab_trailers);
        fab_favourite = findViewById(R.id.fab_favourite);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (ActivityMovieDetails.this);
        rv_trailers.setLayoutManager(linearLayoutManager);
        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFavouriteMovieAsync favouriteMovieAsync = new SetFavouriteMovieAsync(ActivityMovieDetails.this);
                favouriteMovieAsync.execute();
            }
        });
        Intent intent = getIntent();
        tv_movie_title.setText(String.format("Title: %s",
                intent.getStringExtra("title")));
        title = intent.getStringExtra("title");
        mbackdrop_path = intent.getStringExtra("backdrop_path");
        mposter_path = intent.getStringExtra("poster_path");
        tv_overview.setText(String.format("Overview: %s",
                intent.getStringExtra
                        ("overview")));
        overview = intent.getStringExtra("overview");
        tv_release_date.setText(String.format("release_date: %s",
                intent.getStringExtra("release_date")));
        release = intent.getStringExtra("release_date");
        ratingBar.setRating(intent.getFloatExtra("vote_average", 0));
        id = (intent.getIntExtra("id", 0));
        ratingBar.setClickable(false);
        tv_rating.setText(String.format("%s/10", ratingBar.getRating()));
        if (intent.getBooleanExtra("adult", false)) {
            tv_censor_rating.setVisibility(View.VISIBLE);
        } else tv_censor_rating.setVisibility(View.GONE);
        setTitle(intent.getStringExtra("title"));
        Cursor cursor = ActivityMovieDetails.this.getContentResolver().query(
                PopulorMovieContract.PopularMovieEntry.BUILD_CONTENT,
                new String[]{PopulorMovieContract.PopularMovieEntry.COL_MOVIE_ID},
                PopulorMovieContract.PopularMovieEntry.COL_MOVIE_ID + " = ?",
                new String[]{String.valueOf(id)}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                fab_favourite.setImageResource(R.drawable.ic_arrow_back_white_24dp);
            }
        }

        Picasso.with(this)
                .load(helper.base_url + helper.poster_sizes + mposter_path)
                .fit()
                .into
                        (iv_collapsing);

        mfab_trailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                Call<TrailerData> call = apiService.getMovieTrailers(id, "videos", Constants.api_key);
                call.enqueue(new Callback<TrailerData>() {
                    @Override
                    public void onResponse(Call<TrailerData> call, Response<TrailerData> response) {
                        List<Trailers> trailers = response.body().getResults();
                        rv_trailers.setAdapter(new MovieTrailerAdapter(ActivityMovieDetails.this, trailers));
                        progressBar.setVisibility(View.GONE);
                        rv_trailers.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<TrailerData> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("RESPONSE", t.toString());
                    }
                });
            }
        });


    }

    private class SetFavouriteMovieAsync extends AsyncTask<Void, Void, Void> {
        private Context mcontext;

        public SetFavouriteMovieAsync(Context context) {
            mcontext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SaveAndDeleteMovieRow();
            return null;
        }


        private void SaveAndDeleteMovieRow() {
            Cursor cursor = ActivityMovieDetails.this.getContentResolver().query(
                    PopulorMovieContract.PopularMovieEntry.BUILD_CONTENT,
                    new String[]{PopulorMovieContract.PopularMovieEntry.COL_MOVIE_ID},
                    PopulorMovieContract.PopularMovieEntry.COL_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(id)}, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int rowDeleted = ActivityMovieDetails.this.getContentResolver().delete(
                            PopulorMovieContract.PopularMovieEntry.BUILD_CONTENT, PopulorMovieContract
                                    .PopularMovieEntry.COL_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(id)});

                    if (rowDeleted > 0) {
                        success = "Deleted from Favourite";

                    } else {
                        failure = "Failed";
                    }

                } else {
                    ContentValues values = new ContentValues();
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_MOVIE_ID, id);
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_TITLE, title);
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_POSTER_IMAGE, mposter_path);
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_OVERVIEW, overview);
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_RATING, tv_rating.getText().toString());
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_RELEASE_DATE, release);
                    values.put(PopulorMovieContract.PopularMovieEntry.COL_BACKDROP_IMAGE, mbackdrop_path);

                    Uri uri = getContentResolver().insert(PopulorMovieContract.PopularMovieEntry.BUILD_CONTENT,
                            values);
                    long movieRowId = ContentUris.parseId(uri);
                    if (movieRowId > 0) {
                        success = "Added As Favourite";


                    } else {
                        failure = "Failed";
                        success = "";

                    }
                    cursor.close();


                }

            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (success) {
                case "Added As Favourite":
                    Toast.makeText(mcontext, "Added To Favourite", Toast.LENGTH_LONG).show();
                    fab_favourite.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                    break;
                case "Deleted from Favourite":
                    Toast.makeText(mcontext, "Deleted from Favourite", Toast.LENGTH_LONG).show();
                    fab_favourite.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                    break;
                default:
                    Toast.makeText(mcontext, "Failed", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}