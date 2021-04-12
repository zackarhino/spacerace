package com.example.spacerace.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instance = null;
    private RequestQueue requestQueue;

    private final Context context;

    private VolleySingleton(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public static VolleySingleton getInstance(Context context) {
        if(instance == null)
            instance = new VolleySingleton(context);
        return instance;
    }
}
