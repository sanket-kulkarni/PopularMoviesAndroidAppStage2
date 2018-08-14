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

import com.example.sanketk.popularmoives.R;
import com.example.sanketk.popularmoives.model.ReviewsResultsItem;
import com.example.sanketk.popularmoives.model.TrailerResultsItem;

import java.util.List;

/**
 * Created by sanketk on 12-Aug-18.
 */

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ListHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    Context context;
    public List<ReviewsResultsItem> reviewsResultsItems;
    //private final ReviewAdapter.OnItemClickListener listener;

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView tvReviewAuthor;
        TextView tvReviewContent;

        public ListHolder(View itemView) {
            super(itemView);
            this.tvReviewAuthor = (TextView) itemView.findViewById(R.id.tvReviewAuthor);
            this.tvReviewContent = (TextView) itemView.findViewById(R.id.tvReviewContent);
        }

    }

    public ReviewAdapter(Context context, List<ReviewsResultsItem> resultsItemList) {
        this.context = context;
        this.reviewsResultsItems = resultsItemList;
      //  this.listener = listener;
        notifyDataSetChanged();
    }

    public ReviewAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewAdapter.ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false));
    }

    public void onBindViewHolder(ReviewAdapter.ListHolder holder, int position) {
        final ReviewsResultsItem resultsItem = (ReviewsResultsItem) this.reviewsResultsItems.get(position);
    //    holder.bind(resultsItem, listener);
        Log.d(TAG,"onBindViewHolder position "+position);
        Log.d(TAG,"onBindViewHolder resultsItem "+resultsItem);

        if (!TextUtils.isEmpty(resultsItem.getAuthor())) {
            holder.tvReviewAuthor.setText(resultsItem.getAuthor());
        }
        if (!TextUtils.isEmpty(resultsItem.getContent())) {
            holder.tvReviewContent.setText(resultsItem.getContent());
        }

    }

    public int getItemCount() {
        if (this.reviewsResultsItems != null) {
            return this.reviewsResultsItems.size();
        }
        return 0;
    }

//
}

//public interface OnReviewItemClickListener {
//        void onItemClick(ReviewsResultsItem item);
//    }
