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

public class thumb_adapter extends RecyclerView.Adapter<thumb_adapter.ViewHolder>{
    private List<thumbnail> thumbnail_list;
    private Context context;
    final private movieClickListener mClickListener;

    //listener
    public interface movieClickListener{
        void onMovieItemClick(thumbnail thumb);
    }

    public thumb_adapter(List<thumbnail> rec_list, Context context, movieClickListener listener){
        this.thumbnail_list = rec_list;
        this.context = context;
        this.mClickListener=listener;
    }

    @Override
    public thumb_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(thumb_adapter.ViewHolder holder, int position) {
        final thumbnail thumb=thumbnail_list.get(position);

        //here we set the listener if we need one
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+thumb.getUrl()).into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return thumbnail_list.size();
    }

    //the view holder with the contents
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageUrl;

        public ViewHolder(final View itemView){
            super(itemView);
            imageUrl=(ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            thumbnail thumbSend=thumbnail_list.get(clickedPosition);
            mClickListener.onMovieItemClick(thumbSend);
        }
    }
}
