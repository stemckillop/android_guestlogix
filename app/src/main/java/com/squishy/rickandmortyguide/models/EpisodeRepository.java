package com.squishy.rickandmortyguide.models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Response;

import com.squishy.rickandmortyguide.MyApp;
import com.squishy.rickandmortyguide.activities.MainActivity;
import com.squishy.rickandmortyguide.adapter.EpisodeAdapter;
import com.squishy.rickandmortyguide.requests.GetEpisodes;
import com.squishy.rickandmortyguide.requests.GetNextEpisodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EpisodeRepository extends ViewModel {
    private static String TAG = "EpisodeRepository";

    private Context ctx;
    public void setContext(Context ctx) {
        this.ctx = ctx;
    }
    private MutableLiveData<ArrayList<Episode>> episodesList;
    private String next;

    public EpisodeRepository() {
    }

    public LiveData<ArrayList<Episode>> getEpisodeList() {
        if (episodesList == null) {
            episodesList = new MutableLiveData<>();
            getEpisodes();
        }

        return episodesList;
    }

    private void getEpisodes() {

        MyApp app = (MyApp)ctx;

        if (MyApp.checkInternetConnection(ctx)) {
            app.netQueue.add(new GetEpisodes(ctx, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, response.toString());
                    try {
                        ArrayList<Episode> episodes;
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

                        episodesList.setValue(episodes);

                        //myEpisodeAdapter = new EpisodeAdapter(getApplicationContext(), episodes);
                        //myListView.setAdapter(myEpisodeAdapter);
                        //updateUI();

                    } catch (Exception e) {
                        Log.e(TAG, "Unable to build episode list...");
                    }
                }
            }));
        } else {

            Toast.makeText(ctx, "You need internet access to get episode list...", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean getNextEpisodes() {

        if (next.equals("")) {
            return false;
        }
        if (MyApp.checkInternetConnection(ctx)) {
            MyApp app = (MyApp) ctx;
            final ArrayList<Episode> nextEpisode = new ArrayList<>();
            app.netQueue.add(new GetNextEpisodes(ctx, next, new Response.Listener<JSONObject>() {
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

                        ArrayList<Episode> epi = episodesList.getValue();
                        epi.addAll(nextEpisode);
                        episodesList.setValue(epi);

                    } catch (Exception e) {
                        Log.e(TAG, "Unable to add to episode list...");
                    }
                }
            }));
        } else {
            Toast.makeText(ctx, "You need internet access to get more episodes...", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
