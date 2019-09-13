package com.squishy.rickandmortyguide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.squishy.rickandmortyguide.MyApp;
import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.adapter.CharacterAdapter;
import com.squishy.rickandmortyguide.models.Character;
import com.squishy.rickandmortyguide.models.Episode;
import com.squishy.rickandmortyguide.requests.GetCharacters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EpisodeActivity extends AppCompatActivity {
    private static String TAG = "EpisodeActivity";
    private Episode episode;
    private MyApp app;

    private RecyclerView aliveList;
    private RecyclerView deadList;
    public CharacterAdapter aliveAdapter;
    public CharacterAdapter deadAdapter;

    private TextView lblTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        app = (MyApp)getApplicationContext();

        lblTitle = findViewById(R.id.episode_lbl_title);
        aliveList = findViewById(R.id.episode_alive_list);
        deadList = findViewById(R.id.episode_dead_list);
        aliveList.setLayoutManager(new LinearLayoutManager(this));
        deadList.setLayoutManager(new LinearLayoutManager(this));

        try {
            Intent i = getIntent();
            episode = i.getParcelableExtra("episode");
            Log.e(TAG, episode.getName());
            lblTitle.setText(episode.getName());
            getEpisodeCharacters();

        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    String getCharacterString(String[] chars) {
        //build url string
        String url = "";
        for (String aChar : chars) {
            //get url
            //find last /
            //get number after /
            //add string to url
            int backIndex = aChar.lastIndexOf('/');
            url += aChar.substring(backIndex + 1) + ',';
        }

        Log.e(TAG, "Finished URL: " + url);
        return url;
    }

    void getEpisodeCharacters() {

        app.netQueue.add(new GetCharacters(this, getCharacterString(episode.getCharacters()), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final ArrayList<Character> alive = new ArrayList<>();
                final ArrayList<Character> dead = new ArrayList<>();

                for (int i= 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String stat = obj.getString("status");
                        String name = obj.getString("name");
                        String url = obj.getString("image");
                        String create = obj.getString("created");
                        String from = obj.getJSONObject("origin").getString("name");
                        String spe = obj.getString("species");
                        Character cha = new Character(name, stat, url, create, from, spe);

                        if (cha.status.equals("Alive")) {
                            Log.e(TAG, "Alive");
                            alive.add(cha);
                        } else {
                            Log.e(TAG, "Dead");
                            dead.add(cha);
                        }
                    } catch (Exception e) {

                    }
                }

                aliveAdapter = new CharacterAdapter(EpisodeActivity.this, alive);
                deadAdapter = new CharacterAdapter(EpisodeActivity.this, dead);
                aliveList.setAdapter(aliveAdapter);
                deadList.setAdapter(deadAdapter);

            }


        }));

    }

    public void killCharacter(int position) {
        Character cha = aliveAdapter.characters.get(position);
        cha.status = "Dead";
        aliveAdapter.characters.remove(position);
        deadAdapter.characters.add(cha);
        aliveAdapter.notifyDataSetChanged();
        deadAdapter.notifyDataSetChanged();
    }

    public void reviveCharacter(int position) {
        Character cha = deadAdapter.characters.get(position);
        cha.status = "Alive";
        deadAdapter.characters.remove(position);
        aliveAdapter.characters.add(cha);
        aliveAdapter.notifyDataSetChanged();
        deadAdapter.notifyDataSetChanged();
    }
}
