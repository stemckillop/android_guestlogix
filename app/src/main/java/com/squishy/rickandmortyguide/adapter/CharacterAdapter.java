package com.squishy.rickandmortyguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.models.Character;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.MyViewHolder> {

    public Context ctx;
    public ArrayList<Character> characters;

    public CharacterAdapter(Context ctx, ArrayList<Character> alive) {
        this.ctx = ctx;
        this.characters = alive;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_dead, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.characterLbl.setText(characters.get(position).name);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView backgroundImage;
        ImageView crossImage;
        TextView characterLbl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImage = itemView.findViewById(R.id.list_alive_image);
            crossImage = itemView.findViewById(R.id.list_alive_dieded);
            characterLbl = itemView.findViewById(R.id.list_alive_name);
        }
    }
}
