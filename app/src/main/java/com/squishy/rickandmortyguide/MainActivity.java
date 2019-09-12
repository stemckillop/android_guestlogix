package com.squishy.rickandmortyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.squishy.rickandmortyguide.adapter.EpisodeAdapter;
import com.squishy.rickandmortyguide.models.Episode;
import com.squishy.rickandmortyguide.requests.GetEpisodes;
import com.squishy.rickandmortyguide.requests.GetNextEpisodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    String next;
    private ArrayList<Episode> episodes;
    private RecyclerView myListView;
    private EpisodeAdapter myEpisodeAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListView = findViewById(R.id.main_list_episodes);
        myListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && next != "") {
                    getNextEpisodes();
                }
            }
        });
        myLayoutManager = new LinearLayoutManager(this);
        myListView.setLayoutManager(myLayoutManager);

        getEpisodes();
    }

    void getEpisodes() {
        MyApp app = (MyApp)getApplicationContext();
        app.netQueue.add(new GetEpisodes(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    //int count = response.getJSONObject("info").getInt("count");
                    next = response.getJSONObject("info").getString("next");
                    JSONArray episode = response.getJSONArray("results");
                    episodes = new ArrayList<Episode>();
                    for (int i = 0; i < episode.length(); i++) {
                        JSONObject epi = episode.getJSONObject(i);
                        int id = epi.getInt("id");
                        String name = epi.getString("name");
                        String url = epi.getString("url");
                        String e = epi.getString("episode");
                        JSONArray chars = epi.getJSONArray("characters");
                        String[] characters = new String[chars.length()];
                        for (int j = 0; j < chars.length(); j++) {
                            characters[j] = chars.getString(j);
                        }
                        episodes.add(new Episode(id, name, e, characters, url));
                    }

                    myEpisodeAdapter = new EpisodeAdapter(episodes);
                    myListView.setAdapter(myEpisodeAdapter);
                    updateUI();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }));
    }

    void getNextEpisodes() {
        if (next.equals("")) {
            return;
        }
        MyApp app = (MyApp)getApplicationContext();
        final ArrayList<Episode> nextEpisode = new ArrayList<>();
        app.netQueue.add(new GetNextEpisodes(this, next, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    //int count = response.getJSONObject("info").getInt("count");
                    next = response.getJSONObject("info").getString("next");
                    JSONArray episode = response.getJSONArray("results");
                    for (int i = 0; i < episode.length(); i++) {
                        JSONObject epi = episode.getJSONObject(i);
                        int id = epi.getInt("id");
                        String name = epi.getString("name");
                        String url = epi.getString("url");
                        String e = epi.getString("episode");
                        JSONArray chars = epi.getJSONArray("characters");
                        String[] characters = new String[chars.length()];
                        for (int j = 0; j < chars.length(); j++) {
                            characters[j] = chars.getString(j);
                        }
                        nextEpisode.add(new Episode(id, name, e, characters, url));
                    }

                    myEpisodeAdapter.addEpisodes(nextEpisode);
                    updateUI();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }));
    }

    public void updateUI() {
        myEpisodeAdapter.notifyDataSetChanged();
    }
}
