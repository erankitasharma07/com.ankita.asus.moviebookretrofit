package com.ankita.asus.moviebookretrofit;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ankita.asus.moviebookretrofit.Adapter.MainMovieAdapter;
import com.ankita.asus.moviebookretrofit.databinding.ActivityMainBinding;
import com.ankita.asus.moviebookretrofit.models.Movie;
import com.ankita.asus.moviebookretrofit.models.MovieData;
import com.ankita.asus.moviebookretrofit.network.ApiClient;
import com.ankita.asus.moviebookretrofit.network.ApiInterface;
import com.ankita.asus.moviebookretrofit.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    MainMovieAdapter mainMovieAdapter;
    public static List<Movie> movieList = new ArrayList<>();
    RecyclerView recycler;
    ProgressBar progress;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);

        binding.recycler.setLayoutManager(layoutManager);


        getPopularData();
        getTopRatedData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.populor) {
            // progress.setVisibility(View.VISIBLE);
            //     recycler.setVisibility(View.GONE);
            getPopularData();

            return true;
        }

        if (id == R.id.toprated) {
            getTopRatedData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTopRatedData() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieData> call = apiService.getTopRatedMovies(Constants.api_key);
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                movieList = response.body().getResults();
                mainMovieAdapter = new MainMovieAdapter(MainActivity.this,
                        movieList);
                binding.recycler.setAdapter(mainMovieAdapter);
                mainMovieAdapter.notifyDataSetChanged();
//                progress.setVisibility(View.GONE);
  //              recycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Log.e("RESPONSE", t.toString());
            }
        });
    }

    private void getPopularData() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieData> call = apiService.getPopularMovies(Constants.api_key);
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {

                Log.d("###Response", response.body().toString());
                movieList = response.body().getResults();
                mainMovieAdapter = new MainMovieAdapter(MainActivity.this, movieList);
                binding.recycler.setAdapter(mainMovieAdapter);
//                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                // Log error here since request failed
                Log.e("RESPONSE", t.toString());
            }
        });
    }
}
