package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewData;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView tvReviewAuthor;
        public TextView tvReviewContent;

        public ReviewAdapterViewHolder (@NonNull View itemView) {
            super(itemView);
            tvReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            tvReviewContent = itemView.findViewById(R.id.tv_review_content);
        }
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        Review reviewInfo = mReviewData[position];

        holder.tvReviewAuthor.setText(reviewInfo.getAuthor());
        holder.tvReviewContent.setText(reviewInfo.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) {
            return 0;
        }

        return mReviewData.length;
    }

    public void setReviewData(Review[] ReviewData) {
        mReviewData = ReviewData;
        notifyDataSetChanged();
    }
}
