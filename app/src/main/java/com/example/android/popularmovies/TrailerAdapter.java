package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.model.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailerData;

    private final TrailerAdapterOnClickHandler trailerAdapterOnClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void OnClick(Trailer selectedTrailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        trailerAdapterOnClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTrailerName;

        public TrailerAdapterViewHolder (@NonNull View itemView) {
            super(itemView);
            tvTrailerName = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer selectedTrailer = mTrailerData[position];
            trailerAdapterOnClickHandler.OnClick(selectedTrailer);
        }
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);

        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        Trailer trailerInfo = mTrailerData[position];

        holder.tvTrailerName.setText(trailerInfo.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) {
            return 0;
        }

        return mTrailerData.length;
    }

    public void setTrailerData(Trailer[] TrailerData) {
        mTrailerData = TrailerData;
        notifyDataSetChanged();
    }
}
