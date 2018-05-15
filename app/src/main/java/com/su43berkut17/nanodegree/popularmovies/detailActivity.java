package com.su43berkut17.nanodegree.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.su43berkut17.nanodegree.popularmovies.RV_related.thumbnail;

public class detailActivity extends AppCompatActivity {

    //get the view ids
    TextView tv_title;
    TextView tv_release;
    TextView tv_plot;
    ImageView iv_poster;
    ImageView iv_thumb;
    RatingBar rv_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get the views
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_release=(TextView)findViewById(R.id.tv_release);
        tv_plot=(TextView)findViewById(R.id.tv_plot);
        iv_poster=(ImageView)findViewById(R.id.iv_poster);
        iv_thumb=(ImageView)findViewById(R.id.iv_thumb);
        rv_score=(RatingBar)findViewById(R.id.rat_score);

        //get the parcelable
        thumbnail object=(thumbnail) getIntent().getParcelableExtra("detailParce");
        tv_title.setText(object.getTitle());
        tv_release.setText(object.getRelease_date());
        tv_plot.setText(object.getDetails());
        rv_score.setRating(object.getVote_average());

        //load the image
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+object.getUrl()).into(iv_thumb);
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+object.getwideUrl()).into(iv_poster);
    }
}
