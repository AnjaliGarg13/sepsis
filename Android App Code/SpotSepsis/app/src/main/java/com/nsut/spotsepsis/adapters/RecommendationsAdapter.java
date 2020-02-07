package com.nsut.spotsepsis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsut.spotsepsis.R;
import com.nsut.spotsepsis.models.Recommendation;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    private ArrayList<Recommendation> recommendationArrayList;
    private Context context;

    public RecommendationsAdapter(ArrayList<Recommendation> recommendationArrayList, Context context) {
        this.recommendationArrayList = recommendationArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_single_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationsAdapter.ViewHolder holder, int position) {
        final Recommendation recommendation = recommendationArrayList.get(position);
        Glide.with(context).load(recommendation.getImageURL()).into(holder.recommendationPostImageView);
        holder.recommendationPostTitleTextView.setText(recommendation.getTitle());
        holder.recommendationPostDescTextView.setText(recommendation.getShortDescription());
        holder.recommendationPostTagTextView.setText(recommendation.getTag());
        holder.recommendationPostOpenLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open recommendation.getRedirectTo() URL in web page.

            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendationArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView recommendationPostImageView;
        private TextView recommendationPostTitleTextView;
        private TextView recommendationPostDescTextView;
        private TextView recommendationPostTagTextView;
        private Button recommendationPostOpenLinkButton;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            recommendationPostImageView = itemView.findViewById(R.id.recommendationPostImageView);
            recommendationPostTitleTextView = itemView.findViewById(R.id.recommendationPostTitleTextView);
            recommendationPostDescTextView = itemView.findViewById(R.id.recommendationPostDescTextView);
            recommendationPostTagTextView = itemView.findViewById(R.id.recommendationPostTagTextView);
            recommendationPostOpenLinkButton = itemView.findViewById(R.id.recommendationPostOpenLinkButton);
        }
    }
}
