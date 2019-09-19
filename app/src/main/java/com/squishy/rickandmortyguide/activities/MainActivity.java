package com.squishy.rickandmortyguide.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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
import com.squishy.rickandmortyguide.models.EpisodeRepository;
import com.squishy.rickandmortyguide.requests.GetEpisodes;
import com.squishy.rickandmortyguide.requests.GetNextEpisodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    String next;
    private EpisodeRepository model;
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

        myEpisodeAdapter = new EpisodeAdapter(getApplicationContext(), new ArrayList<Episode>());
        myListView.setAdapter(myEpisodeAdapter);
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

                    loadingLbl.setText(getResources().getString(R.string.loading_more));
                    openLoadingPortal();
                    myListView.setVisibility(View.INVISIBLE);
                    if (!model.getNextEpisodes()) {
                        closeLoadingPortal();
                    }

                }
            }
        });
        myListView.setLayoutManager(new LinearLayoutManager(this));

        model = ViewModelProviders.of(this).get(EpisodeRepository.class);
        model.setContext(getApplicationContext());
        openLoadingPortal();
        model.getEpisodeList().observe(this, new Observer<ArrayList<Episode>>() {
            @Override
            public void onChanged(ArrayList<Episode> episodes) {
                myEpisodeAdapter.setEpisodes(episodes);
                updateUI();
            }
        });
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
