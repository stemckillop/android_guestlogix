package com.squishy.rickandmortyguide.requests;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetEpisodes extends JsonObjectRequest {
    public GetEpisodes(final Context ctx, Response.Listener<JSONObject> listener) {
        super(Method.GET, "https://rickandmortyapi.com/api/episode/", null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
