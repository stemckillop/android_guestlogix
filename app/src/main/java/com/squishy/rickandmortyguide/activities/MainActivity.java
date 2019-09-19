package com.squishy.rickandmortyguide.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.squishy.rickandmortyguide.MyApp;
import com.squishy.rickandmortyguide.R;
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
    private ImageView loadingPortal;
    private TextView loadingLbl;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingPortal = findViewById(R.id.main_img_portal);
        loadingLbl = findViewById(R.id.main_lbl_loading);
        myListView = findViewById(R.id.main_list_episodes);
        swipeRefresh = findViewById(R.id.main_list_swipe);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        myListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && next != "") {
                    if (MyApp.checkInternetConnection(getApplicationContext())) {
                        getNextEpisodes();
                    } else {
                        Toast.makeText(getBaseContext(), "You need internet access to get more episodes...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        myLayoutManager = new LinearLayoutManager(this);
        myListView.setLayoutManager(myLayoutManager);

        if (MyApp.checkInternetConnection(getApplicationContext())) {
            getEpisodes();
        } else {
            Toast.makeText(getBaseContext(), "You need internet access to get episode list...", Toast.LENGTH_SHORT).show();
        }
    }

    void getEpisodes() {
        openLoadingPortal();
        MyApp app = (MyApp)getApplicationContext();
        app.netQueue.add(new GetEpisodes(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
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

                            myEpisodeAdapter = new EpisodeAdapter(getApplicationContext(), episodes);
                            myListView.setAdapter(myEpisodeAdapter);
                    updateUI();

                } catch (Exception e) {
                    Log.e(TAG, "Unable to build episode list...");
                }
            }
        }));
    }

    void getNextEpisodes() {


        if (next.equals("")) {
            return;
        }

        loadingLbl.setText("Loading more...");
        openLoadingPortal();
        myListView.setVisibility(View.INVISIBLE);

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
                    Log.e(TAG, "Unable to add to episode list...");
                }
            }
        }));
    }

    private void openLoadingPortal() {
        loadingPortal.setVisibility(View.VISIBLE);
        loadingLbl.setVisibility(View.VISIBLE);

        Animation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        an.setDuration(1000);
        an.setInterpolator(new LinearInterpolator());
        an.setRepeatCount(-1);

        loadingPortal.setAnimation(an);
    }

    private void closeLoadingPortal() {

        loadingPortal.setAnimation(null);
        loadingPortal.setVisibility(View.INVISIBLE);
        loadingLbl.setVisibility(View.INVISIBLE);
        myListView.setVisibility(View.VISIBLE);

    }

    public void updateUI() {
        myEpisodeAdapter.notifyDataSetChanged();
        closeLoadingPortal();
    }
}
