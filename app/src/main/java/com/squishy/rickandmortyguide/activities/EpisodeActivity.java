package com.squishy.rickandmortyguide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.squishy.rickandmortyguide.MyApp;
import com.squishy.rickandmortyguide.R;
import com.squishy.rickandmortyguide.models.Episode;
import com.squishy.rickandmortyguide.requests.GetCharacters;

import org.json.JSONObject;

public class EpisodeActivity extends AppCompatActivity {
    private static String TAG = "EpisodeActivity";
    private Episode episode;
    private MyApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        app = (MyApp)getApplicationContext();

        try {
            Intent i = getIntent();
            episode = (Episode)i.getParcelableExtra("episode");
            Log.e(TAG, episode.getName());
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
                Log.e(TAG, response.toString());
            }
        }));

    }
}
