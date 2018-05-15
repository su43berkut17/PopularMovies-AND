package com.su43berkut17.nanodegree.popularmovies.RV_related;

import android.os.Parcel;
import android.os.Parcelable;

public class thumbnail implements Parcelable {
    private String title;
    private String url;
    private String wideUrl;
    private String details;
    private String release_date;
    private float vote_average;
    //Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

    public String getTitle(){return title;}

    public String getUrl(){return url;}

    public String getwideUrl(){return wideUrl;}

    public String getDetails(){return details;}

    public String getRelease_date(){return release_date;}

    public float getVote_average(){return vote_average;}

    public thumbnail(String rec_title, String rec_url, String rec_wideUrl, String rec_details,String rec_release_date,float rec_vote_average){
        title=rec_title;
        url=rec_url;
        wideUrl=rec_wideUrl;
        details=rec_details;
        release_date=rec_release_date;
        vote_average=rec_vote_average;
    }

    private thumbnail(Parcel out){
        title=out.readString();
        url=out.readString();
        wideUrl=out.readString();
        details=out.readString();
        release_date=out.readString();
        vote_average=out.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(url);
        out.writeString(wideUrl);
        out.writeString(details);
        out.writeString(release_date);
        out.writeFloat(vote_average);
    }

    public static final Parcelable.Creator<thumbnail> CREATOR = new Parcelable.Creator<thumbnail>(){
        @Override
        public thumbnail createFromParcel(Parcel out){
            return new thumbnail(out);
        }

        @Override
        public thumbnail[] newArray(int size){
            return new thumbnail[size];
        }
    };
}
