package com.squishy.rickandmortyguide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.models.Episode;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {
    private ArrayList<Episode> dataset;

    public void addEpisodes(ArrayList<Episode> epi) {
        dataset.addAll(epi);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView seasonLbl;
        TextView titleLbl;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            seasonLbl = itemView.findViewById(R.id.list_lbl_episode);
            titleLbl = itemView.findViewById(R.id.list_lbl_title);
        }
    }

    public EpisodeAdapter(ArrayList<Episode> data) {
        this.dataset = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_episodes, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.seasonLbl.setText(dataset.get(position).getEpisode());
        holder.titleLbl.setText(dataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
