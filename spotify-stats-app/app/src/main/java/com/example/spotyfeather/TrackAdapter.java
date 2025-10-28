package com.example.spotyfeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private ArrayList<String> trackNames;

    public TrackAdapter(ArrayList<String> trackNames) {
        this.trackNames = trackNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trackName.setText(trackNames.get(position));
    }

    @Override
    public int getItemCount() {
        return trackNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trackName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.track_name);
        }
    }
}
