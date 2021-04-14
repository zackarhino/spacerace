package com.example.spacerace.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.spacerace.R;
import com.example.spacerace.api.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Displays atmospheric data for the current Sol on Mars
 * Information is pulled from NASA's InSight API
 * @author Zachary Allard
 */
public class WeatherFragment extends Fragment {

    TextView sol_textview;
    TextView season_textview;
    TextView n_season_textview;
    TextView s_season_textview;
    TextView avg_pressure_textview;
    TextView min_pressure_textview;
    TextView max_pressure_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        sol_textview = view.findViewById(R.id.sol_textview);
        season_textview = view.findViewById(R.id.season_textview);
        n_season_textview = view.findViewById(R.id.n_season_textview);
        s_season_textview = view.findViewById(R.id.s_season_textview);
        avg_pressure_textview = view.findViewById(R.id.avg_pressure_textview);
        min_pressure_textview = view.findViewById(R.id.min_pressure_textview);
        max_pressure_textview = view.findViewById(R.id.max_pressure_textview);

        updateMarsWeather("i68uCMxS1T7fZjiMEbW3E0qEc0tMBxQaCxYZ08ZL");

        return view;
    }

    /**
     * Update the weather info
     * @param apiKey The API key to query the APOD API with
     * @author Zachary Allard
     */
    private void updateMarsWeather(String apiKey){
        VolleySingleton volley = VolleySingleton.getInstance(getContext());
        String url = "https://api.nasa.gov/insight_weather/?api_key=" + apiKey + "&feedtype=json&ver=1.0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sol_keys = response.getJSONArray("sol_keys");
                    ArrayList<String> key_list = new ArrayList<>();
                    for (int i = 0; i<  sol_keys.length(); i++) {
                        key_list.add(sol_keys.getString(i));
                    }
                    String current_sol_key = key_list.get(0);
                    JSONObject current_sol = response.getJSONObject(current_sol_key);

                    String season_text = current_sol.getString("Season");
                    String n_season_text = current_sol.getString("Northern_season");
                    String s_season_text = current_sol.getString("Southern_season");

                    JSONObject pressure = current_sol.getJSONObject("PRE");
                    String avg_pressure_text = pressure.getString("av");
                    String min_pressure_text = pressure.getString("mn");
                    String max_pressure_text = pressure.getString("mx");

                    // Update text
                    sol_textview.setText(String.format(getString(R.string.sol_text), current_sol_key));
                    season_textview.setText(season_text.toUpperCase());
                    n_season_textview.setText(String.format(getString(R.string.north_season), n_season_text));
                    s_season_textview.setText(String.format(getString(R.string.south_season), s_season_text));
                    avg_pressure_textview.setText(String.format(getString(R.string.avg_pressure), avg_pressure_text));
                    min_pressure_textview.setText(String.format(getString(R.string.min_pressure), min_pressure_text));
                    max_pressure_textview.setText(String.format(getString(R.string.max_pressure), max_pressure_text));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) { error.printStackTrace(); }
        });
        volley.getRequestQueue().add(jsonObjectRequest);
    }
}