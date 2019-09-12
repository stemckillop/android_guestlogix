package com.squishy.rickandmortyguide.requests;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetCharacters extends JsonObjectRequest {

    public GetCharacters(final Context ctx, String characters, Response.Listener<JSONObject> listener) {
        super("https://rickandmortyapi.com/api/character/" + characters, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
