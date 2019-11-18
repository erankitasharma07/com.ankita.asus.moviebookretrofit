package com.ankita.asus.moviebookretrofit.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ankita.asus.moviebookretrofit.ActivityMovieDetails;
import com.ankita.asus.moviebookretrofit.R;
import com.ankita.asus.moviebookretrofit.models.Movie;
import com.ankita.asus.moviebookretrofit.utils.helper;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class MainMovieAdapter extends RecyclerView.Adapter<MainMovieAdapter.MyViewHolder> {
    private Context mcontext;
    public List<Movie> movieList;

    public MainMovieAdapter(Context mcontext, List<Movie> movieList) {
        this.mcontext = mcontext;
        this.movieList = movieList;
    }

//    public MainMovieAdapter(List<Movie> movieList) {
//        this.movieList = movieList;
//    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie_poster, viewGroup, false);


        return new MainMovieAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        Picasso.with(mcontext).load(helper.base_url + helper.poster_sizes + movieList.get(i).poster_path).into
                (myViewHolder.image);
        myViewHolder.ratingtext.setText(String.valueOf(movieList.get(i).vote_average));
        myViewHolder.rating.setRating(movieList.get(i).vote_average);
        myViewHolder.rating.setEnabled(false);
        myViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mcontext, ActivityMovieDetails.class);

                in.putExtra("poster_path", movieList.get(i).poster_path);
                in.putExtra("overview", movieList.get(i).overview);
                in.putExtra("release_date", movieList.get(i).release_date);
                in.putExtra("vote_average", movieList.get(i).vote_average);
             //   in.putExtra("adult",movieList.get(i).get);
                in.putExtra("title", movieList.get(i).title);
                in.putExtra("id", movieList.get(i).id);
                String transitionName = mcontext.getString(R.string.title);
                ActivityOptions transitionActivityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mcontext,
                            v, transitionName);
                }
                if (transitionActivityOptions != null) {
                    mcontext.startActivity(in, transitionActivityOptions.toBundle());
                } else mcontext.startActivity(in);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView image;
        TextView ratingtext;
        RatingBar rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.image);
            ratingtext = itemView.findViewById(R.id.ratingtext);
            rating = itemView.findViewById(R.id.rating);


        }
    }
}
