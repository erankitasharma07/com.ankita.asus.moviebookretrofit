package com.ankita.asus.moviebookretrofit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankita.asus.moviebookretrofit.R;
import com.ankita.asus.moviebookretrofit.models.Trailers;
import com.ankita.asus.moviebookretrofit.utils.Constants;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter
        <MovieTrailerAdapter.MyViewHolder> {
    private Context context;
    private List<Trailers> trailers;

    public MovieTrailerAdapter(Context context, List<Trailers> trailers) {
        this.context = context;
        this.trailers = trailers;
    }


    @NonNull
    @Override
    public MovieTrailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_row, viewGroup, false);
        return new MovieTrailerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.MyViewHolder myViewHolder,final int i) {
       myViewHolder.mtv_trailer.setText(trailers.get(i).name);
        myViewHolder.mfab_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayTrailer(trailers.get(i).key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton mfab_trailer;
        TextView mtv_trailer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtv_trailer = itemView.findViewById(R.id.tv_trailer);
            mfab_trailer = itemView.findViewById(R.id.fab_trailer);

        }
    }
    public void PlayTrailer(String key){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YouTube_URL + key));
        }
        context.startActivity(intent);
    }
}
