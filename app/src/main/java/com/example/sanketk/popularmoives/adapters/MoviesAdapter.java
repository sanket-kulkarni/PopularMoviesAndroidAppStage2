package com.example.sanketk.popularmoives.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.sanketk.popularmoives.R;
import com.example.sanketk.popularmoives.model.ResultsItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MoviesAdapter extends Adapter<MoviesAdapter.ListHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private static final String imgUrl = "http://image.tmdb.org/t/p/w185/";
    Context context;
    public List<ResultsItem> resultsItemList;
    private final OnItemClickListener listener;

    public class ListHolder extends ViewHolder {
        ImageView ivMoivePoster;

        public ListHolder(View itemView) {
            super(itemView);
            this.ivMoivePoster = (ImageView) itemView.findViewById(R.id.movie_image);
        }
        public void bind(final ResultsItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public MoviesAdapter(Context context, List<ResultsItem> resultsItemList, OnItemClickListener listener) {
        this.context = context;
        this.resultsItemList = resultsItemList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));
    }

    public void onBindViewHolder(ListHolder holder, int position) {
        final ResultsItem resultsItem = (ResultsItem) this.resultsItemList.get(position);
        holder.bind(resultsItem, listener);
        Log.d(TAG,"onBindViewHolder position "+position);
        Log.d(TAG,"onBindViewHolder resultsItem "+resultsItem);

        if (!TextUtils.isEmpty(resultsItem.getPosterPath())) {
            Glide.with(this.context).load(imgUrl + resultsItem.getPosterPath()).placeholder(R.drawable.ic_launcher_background).into(holder.ivMoivePoster);
        }
        else {

            String filename = String.valueOf(resultsItem.getId());

            File picfile = new File(holder.ivMoivePoster.getContext().getFilesDir(), filename);

            if (picfile.exists()) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(picfile));
                    holder.ivMoivePoster.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    holder.ivMoivePoster.setImageResource(R.drawable.ic_error_outline_black_24dp);
                    e.printStackTrace();
                }
            } else {
                holder.ivMoivePoster.setImageResource(R.drawable.ic_error_outline_black_24dp);
            }
        }

    }

    public int getItemCount() {
        if (this.resultsItemList != null) {
            return this.resultsItemList.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ResultsItem item);
    }
}
