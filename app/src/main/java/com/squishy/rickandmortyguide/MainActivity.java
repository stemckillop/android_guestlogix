package com.squishy.rickandmortyguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.squishy.rickandmortyguide.models.Episode;
import com.squishy.rickandmortyguide.requests.GetEpisodes;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    private Episode[] episodes;
    private RecyclerView myListView;
    private RecyclerView.Adapter myEpisodeAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getEpisodes();
    }

    void getEpisodes() {
        MyApp app = (MyApp)getApplicationContext();
        app.netQueue.add(new GetEpisodes(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    int count = response.getJSONObject("info").getInt("count");
                    episodes = new Episode[count];
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
                            characters[i] = chars.getString(i);
                        }
                        episodes[i] = new Episode(id, name, e, characters, url);
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }));
    }
}
