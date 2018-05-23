package com.su43berkut17.nanodegree.popularmovies.RV_related;

import android.os.Parcel;
import android.os.Parcelable;

public class reviews implements Parcelable {
    private String author;
    private String content;
    private String url;

    public String getAuthor(){return author;}

    public String getContent() {return content;}

    public String getUrl() { return url; }

    public reviews(String rec_author, String rec_content, String rec_url){
        author=rec_author;
        content=rec_content;
        url=rec_url;
    }

    private reviews (Parcel out){
        author=out.readString();
        content=out.readString();
        url=out.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<reviews> CREATOR = new Parcelable.Creator<reviews>(){
        @Override
        public reviews createFromParcel(Parcel out){
            return new reviews(out);
        }

        @Override
        public reviews[] newArray(int size){
            return new reviews[size];
        }
    };
}
