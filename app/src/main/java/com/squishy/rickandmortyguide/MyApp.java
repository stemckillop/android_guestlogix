package com.squishy.rickandmortyguide;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApp extends Application {
    private static String TAG = "MyApp";

    public RequestQueue netQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        netQueue = Volley.newRequestQueue(this);
    }

    public static boolean checkInternetConnection(Context ctx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
                throw new NullPointerException();
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null)
                throw new NullPointerException();
            return ni.isConnected();
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException Called");
            return false;
        }
    }
}
