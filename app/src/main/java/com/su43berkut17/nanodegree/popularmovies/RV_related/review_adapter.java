package com.su43berkut17.nanodegree.popularmovies.RV_related;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.su43berkut17.nanodegree.popularmovies.R;

import java.util.List;

public class review_adapter extends RecyclerView.Adapter<review_adapter.ViewHolder>{
    private List<reviews> thumbnail_list;
    private Context context;
    final private movieClickListener mClickListener;

    //listener
    public interface movieClickListener{
        void onMovieItemClick(reviews thumb);
    }

    public review_adapter(List<reviews> rec_list, Context context, movieClickListener listener){
        this.thumbnail_list = rec_list;
        this.context = context;
        this.mClickListener=listener;
    }

    @Override
    public review_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_thumbnails,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(review_adapter.ViewHolder holder, int position) {
        final reviews thumb=thumbnail_list.get(position);

        holder.title.setText(thumb.getAuthor());
        holder.content.setText(thumb.getContent());
        holder.url.setText(thumb.getUrl());

        //here we set the listener if we need one
        //Picasso.get().load("https://image.tmdb.org/t/p/w500"+thumb.getUrl()).into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return thumbnail_list.size();
    }

    //the view holder with the contents
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView content;
        private TextView url;

        public ViewHolder(final View itemView){
            super(itemView);
            //imageUrl=(ImageView) itemView.findViewById(R.id.thumbnail);
            title=(TextView)itemView.findViewById(R.id.tv_review_title);
            content=(TextView)itemView.findViewById(R.id.tv_review_content);
            url=(TextView)itemView.findViewById(R.id.tv_review_url);
            url.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            reviews thumbSend=thumbnail_list.get(clickedPosition);
            mClickListener.onMovieItemClick(thumbSend);
        }
    }
}