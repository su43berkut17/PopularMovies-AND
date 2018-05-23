package com.su43berkut17.nanodegree.popularmovies.RV_related;

import android.os.Parcel;
import android.os.Parcelable;

public class trailer implements Parcelable {
    private String movieID;
    private String trailerName;
    private String trailerType;

    public String getMovieID() { return movieID;}

    public String getTrailerName() { return trailerName; }

    public String getTrailerType() { return trailerType; }

    public trailer (String rec_movieId, String rec_trailerName, String rec_trailerType){
        movieID=rec_movieId;
        trailerName=rec_trailerName;
        trailerType=rec_trailerType;
    }

    private trailer(Parcel out){
        movieID=out.readString();
        trailerName=out.readString();
        trailerType=out.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieID);
        dest.writeString(trailerName);
        dest.writeString(trailerType);
    }

    public static final Parcelable.Creator<trailer> CREATOR = new Parcelable.Creator<trailer>(){
        @Override
        public trailer createFromParcel(Parcel out){
            return new trailer(out);
        }

        @Override
        public trailer[] newArray(int size){
            return new trailer[size];
        }
    };
}
