package com.squishy.rickandmortyguide.requests;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetNextEpisodes extends JsonObjectRequest {
    public GetNextEpisodes(final Context ctx, String url, @Nullable Response.Listener<JSONObject> listener) {
        super(url, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
