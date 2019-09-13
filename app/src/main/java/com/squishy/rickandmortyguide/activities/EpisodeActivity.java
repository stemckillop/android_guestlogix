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
import com.squishy.rickandmortyguide.models.Episode;
import com.squishy.rickandmortyguide.requests.GetCharacters;

import org.json.JSONObject;

public class EpisodeActivity extends AppCompatActivity {
    private static String TAG = "EpisodeActivity";
    private Episode episode;
    private MyApp app;

    private RecyclerView aliveList;
    private RecyclerView deadList;
    private CharacterAdapter aliveAdapter;
    private CharacterAdapter deadAdapter;

    private TextView lblTitle;
    private TextView lblBack;


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
            lblBack.setText(episode.getName());
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
        app.netQueue.add(new GetCharacters(this, getCharacterString(episode.getCharacters()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                
            }
        }));

    }
}
