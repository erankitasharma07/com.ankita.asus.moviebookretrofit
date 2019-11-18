package com.ankita.asus.moviebookretrofit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerData {

    @SerializedName("id")
    public int id;
    private ArrayList<Trailers> results;
    public ArrayList<Trailers> getResults(){
        return results;
    }

    public int getId() {
        return id;
    }
}
