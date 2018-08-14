package com.example.sanketk.popularmoives.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanketk.popularmoives.R;
import com.example.sanketk.popularmoives.model.TrailerResultsItem;

import java.util.List;

/**
 * Created by sanketk on 12-Aug-18.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ListHolder> {
    private static final String TAG = TrailersAdapter.class.getSimpleName();
    Context context;
    public List<TrailerResultsItem> trailerResultsItems;
    private final TrailersAdapter.OnItemClickListener listener;

    public class ListHolder extends RecyclerView.ViewHolder {
        ImageView ivPlayTrailer;
        TextView tvTrailerName;
        TextView tvTrailerShare;

        public ListHolder(View itemView) {
            super(itemView);
            this.ivPlayTrailer = (ImageView) itemView.findViewById(R.id.ivPlayTrailer);
            this.tvTrailerName = (TextView) itemView.findViewById(R.id.tvTrailerName);
            this.tvTrailerShare = (TextView) itemView.findViewById(R.id.tvTrailerShare);
        }
        public void bind(final TrailerResultsItem item, final TrailersAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            tvTrailerShare.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onShareClick(item);
                }
            });
        }
    }

    public TrailersAdapter(Context context, List<TrailerResultsItem> resultsItemList, TrailersAdapter.OnItemClickListener listener) {
        this.context = context;
        this.trailerResultsItems = resultsItemList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    public TrailersAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailersAdapter.ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false));
    }

    public void onBindViewHolder(TrailersAdapter.ListHolder holder, int position) {
        final TrailerResultsItem resultsItem = (TrailerResultsItem) this.trailerResultsItems.get(position);
        holder.bind(resultsItem, listener);
        Log.d(TAG,"onBindViewHolder position "+position);
        Log.d(TAG,"onBindViewHolder resultsItem "+resultsItem);

        if (!TextUtils.isEmpty(resultsItem.getName())) {
           holder.tvTrailerName.setText(resultsItem.getName());
        }

    }

    public int getItemCount() {
        if (this.trailerResultsItems != null) {
            return this.trailerResultsItems.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(TrailerResultsItem item);
        void onShareClick(TrailerResultsItem item);
    }
}
