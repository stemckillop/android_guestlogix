package com.squishy.rickandmortyguide;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApp extends Application {

    public RequestQueue netQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        netQueue = Volley.newRequestQueue(this);
    }
}
