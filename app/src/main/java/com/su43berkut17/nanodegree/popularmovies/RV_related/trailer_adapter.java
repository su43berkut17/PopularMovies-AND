package com.su43berkut17.nanodegree.popularmovies.RV_related;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.su43berkut17.nanodegree.popularmovies.R;

import java.util.List;

public class trailer_adapter extends RecyclerView.Adapter<trailer_adapter.ViewHolder>{
    private List<trailer> trailer_list;
    private Context context;
    final private movieClickListener mClickListener;

    //listener
    public interface movieClickListener{
        void onMovieItemClick(trailer thumb);
    }

    public trailer_adapter(List<trailer> rec_list, Context context, movieClickListener listener){
        this.trailer_list = rec_list;
        this.context = context;
        this.mClickListener=listener;
    }

    @Override
    public trailer_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_thumbnails,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(trailer_adapter.ViewHolder holder, int position) {
        final trailer trailers=trailer_list.get(position);

        //set texts
        holder.title.setText(trailers.getTrailerName());
        holder.type.setText(trailers.getTrailerType());
        String videoName;
        videoName="https://img.youtube.com/vi/"+trailers.getMovieID()+"/0.jpg";

        //here we set the listener if we need one
        Picasso.get().load(videoName).into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return trailer_list.size();
    }

    //the view holder with the contents
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageUrl;
        private TextView title;
        private TextView type;
        private ImageView play;

        public ViewHolder(final View itemView){
            super(itemView);
            imageUrl=(ImageView) itemView.findViewById(R.id.iv_thumb_trailer);
            title=(TextView)itemView.findViewById(R.id.tv_title);
            type=(TextView)itemView.findViewById(R.id.tv_type);
            play=(ImageView)itemView.findViewById(R.id.button_play);

            //itemView.setOnClickListener(this);
            play.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            trailer trailer_send=trailer_list.get(clickedPosition);
            mClickListener.onMovieItemClick(trailer_send);
        }
    }
}
