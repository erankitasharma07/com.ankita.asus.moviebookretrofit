package com.ankita.asus.moviebookretrofit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailers implements Parcelable {

    @SerializedName("id")
    public String id;

    @SerializedName("iso_639_1")
    public String iso_639_1;

    @SerializedName("iso_3166_1")
    public String iso_3166_1;

    @SerializedName("key")
    public String key;

    @SerializedName("name")
    public String name;

    @SerializedName("site")
    public String site;

    @SerializedName("size")
    public  int size;

    @SerializedName("type")
    public String type;

    protected Trailers(Parcel in){
        id= in.readString();
        iso_639_1 = in.readString();
        iso_3166_1=in.readString();
        key=in.readString();
        name=in.readString();
        site=in.readString();
        size=in.readInt();
        type=in.readString();

    }


    public  static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel source) {
            return new Trailers(source);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);


    }
}
