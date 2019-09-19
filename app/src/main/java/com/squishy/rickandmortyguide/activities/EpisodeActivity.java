package com.squishy.rickandmortyguide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;

public class EpisodeActivity extends AppCompatActivity {
    private static String TAG = "EpisodeActivity";
    private Episode episode;
    private MyApp app;

    private ImageView loadingImg;
    private TextView loadingLbl;
    private View loadingList;
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

        loadingList = findViewById(R.id.episode_list);
        loadingLbl = findViewById(R.id.episode_lbl_loading);
        loadingImg = findViewById(R.id.episode_img_portal);
        lblTitle = findViewById(R.id.episode_lbl_title);
        aliveList = findViewById(R.id.episode_alive_list);
        deadList = findViewById(R.id.episode_dead_list);
        aliveList.setLayoutManager(new LinearLayoutManager(this));
        deadList.setLayoutManager(new LinearLayoutManager(this));

        try {
            Intent i = getIntent();
            episode = i.getParcelableExtra("episode");
            lblTitle.setText(episode.getName());

            if (MyApp.checkInternetConnection(getApplicationContext())) {
                getEpisodeCharacters();
            } else {
                Toast.makeText(getBaseContext(), "You need internet access to request character list...", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            Log.e(TAG, "Unable to get episode name");
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

        openLoadingPortal();
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
                        Log.e(TAG, "Unable to get characters...");
                    }
                }

                Collections.sort(alive);
                Collections.sort(dead);

                aliveAdapter = new CharacterAdapter(EpisodeActivity.this, alive, true);
                deadAdapter = new CharacterAdapter(EpisodeActivity.this, dead, false);
                aliveList.setAdapter(aliveAdapter);
                deadList.setAdapter(deadAdapter);
                closeLoadingPortal();

            }


        }));

    }

    private void openLoadingPortal() {
        loadingList.setVisibility(View.INVISIBLE);
        loadingImg.setVisibility(View.VISIBLE);
        loadingLbl.setVisibility(View.VISIBLE);

        Animation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        an.setDuration(1000);
        an.setInterpolator(new LinearInterpolator());
        an.setRepeatCount(-1);

        loadingImg.setAnimation(an);
    }

    private void closeLoadingPortal() {

        loadingImg.setAnimation(null);
        loadingList.setVisibility(View.VISIBLE);
        loadingImg.setVisibility(View.INVISIBLE);
        loadingLbl.setVisibility(View.INVISIBLE);
    }

    public void killCharacter(int position) {
        Character cha = aliveAdapter.characters.get(position);
        cha.status = "Dead";
        aliveAdapter.characters.remove(position);
        deadAdapter.characters.add(cha);
        Collections.sort(aliveAdapter.characters);
        Collections.sort(deadAdapter.characters);
        aliveAdapter.notifyDataSetChanged();
        deadAdapter.notifyDataSetChanged();
    }
    public void reviveCharacter(int position) {
        Character cha = deadAdapter.characters.get(position);
        cha.status = "Alive";
        deadAdapter.characters.remove(position);
        aliveAdapter.characters.add(cha);
        Collections.sort(aliveAdapter.characters);
        Collections.sort(deadAdapter.characters);
        aliveAdapter.notifyDataSetChanged();
        deadAdapter.notifyDataSetChanged();
    }
    public void switchCharacter(Character cha) {
        int index = findCharacter(cha);
        if (cha.status.equals("Alive")) {
            killCharacter(index);
        } else {
            reviveCharacter(index);
        }
    }

    private int findCharacter(Character cha) {
        if (cha.status.equals("Alive")) {
            for (int i= 0; i < aliveAdapter.characters.size(); i++) {
                if (aliveAdapter.characters.get(i).name.equals(cha.name)) {
                    return i;
                }
            }
        } else {
            for (int i= 0; i < deadAdapter.characters.size(); i++) {
                if (deadAdapter.characters.get(i).name.equals(cha.name)) {
                    return i;
                }
            }
        }

        return 0;
    }
}
