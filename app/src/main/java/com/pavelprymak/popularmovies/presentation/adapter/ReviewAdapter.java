package com.pavelprymak.popularmovies.presentation.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.databinding.ItemViewReviewBinding;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.ReviewsResultsItem;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewsResultsItem> mReviews;
    private ReviewListItemClickListener clickListener;
    private Context mContext;

    public ReviewAdapter(ReviewListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<ReviewsResultsItem> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemViewReviewBinding itemViewReviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_review, parent, false);
        return new ReviewViewHolder(itemViewReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        else return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewReviewBinding binding;

        ReviewViewHolder(@NonNull ItemViewReviewBinding reviewBinding) {
            super(reviewBinding.getRoot());
            this.binding = reviewBinding;
            reviewBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            ReviewsResultsItem review = mReviews.get(position);
            if (review != null && mContext != null) {
                //Author Name
                String authorInfo = mContext.getString(R.string.movie_users_reviews_author) + review.getAuthor();
                binding.authorNameTv.setText(authorInfo);
                //Content
                String content = review.getContent();
                if (content == null) {
                    content = "";
                }
                binding.contentTv.setText(Html.fromHtml(content));
            }
        }

        @Override
        public void onClick(View v) {
            if (mReviews != null) {
                ReviewsResultsItem review = mReviews.get(getAdapterPosition());
                if (review != null) {
                    clickListener.onReviewItemClick(review.getId());
                }
            }
        }
    }
}