package com.squishy.rickandmortyguide.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.activities.EpisodeActivity;
import com.squishy.rickandmortyguide.dialog.CharacterDialog;
import com.squishy.rickandmortyguide.models.Character;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.MyViewHolder> {

    private static String TAG = "CharacterAdapter";
    public Context ctx;
    public ArrayList<Character> characters;

    public CharacterAdapter(Context ctx, ArrayList<Character> chars, boolean alive) {
        this.ctx = ctx;
        this.characters = chars;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_dead, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, characters.get(position).created);
                SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
                try {
                    Date date = d.parse(characters.get(position).created);
                } catch (Exception e) {
                    Log.e(TAG, "Failed on simple date format");
                }

                CharacterDialog frag = new CharacterDialog();
                frag.character = characters.get(position);
                frag.ctx = ((EpisodeActivity)ctx);
                frag.show(((EpisodeActivity)ctx).getSupportFragmentManager(), "frag");
            }
        });
        holder.characterLbl.setText(characters.get(position).name);
        Picasso.with(ctx).load(characters.get(position).image).into(holder.backgroundImage);
        if (characters.get(position).status.equals("Alive")) {
            holder.crossImage.setVisibility(View.INVISIBLE);
        } else {
            holder.crossImage.setVisibility(View.VISIBLE);
        }
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
