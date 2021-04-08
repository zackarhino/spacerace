package com.example.spacerace.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spacerace.MainActivity;
import com.example.spacerace.R;
import com.example.spacerace.database.Note;
import com.example.spacerace.database.NoteDB;
import com.example.spacerace.helper.SwipeDetector;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class JournalFragment extends Fragment {

    private static ArrayList<Note> notes;
    private static NoteAdapter adapter;

    public static FragmentManager fm;

    private static RequestQueue requestQueue;
    public final SwipeDetector swipeDetector = new SwipeDetector(getActivity());

    public ImageView apod_imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoteDB db = new NoteDB(getContext());
        notes = db.getAllDishes();
        db.closeDB();
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);
        // Add a note when fab is clicked
        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDB db = new NoteDB(getContext());
                Note note = new Note("TestTitle", "Lorem Ipsum", Calendar.getInstance().getTime().toString());
                notes.add(note);
                db.addNote(note);
                adapter.notifyDataSetChanged();
                db.closeDB();
            }
        });

        requestQueue = getRequestQueue(getContext());
        apod_imageview = view.findViewById(R.id.imageView);
        fm = getActivity().getSupportFragmentManager();
        MainActivity.fab.setText(getResources().getString(R.string.fab_new_note));
        updateNasaImage("i68uCMxS1T7fZjiMEbW3E0qEc0tMBxQaCxYZ08ZL");

        adapter = new NoteAdapter(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new SwipeDetector(getActivity()));
        return view;
    }

    /**
     *
     * @param apiKey The API key to query the APOD API with
     */
    private void updateNasaImage(String apiKey){
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        String imageURL = "";
                        try {
                            imageURL = response.getString("url");
                            Picasso.get().load(imageURL).into(apod_imageview);
                            //Log.d("Volley", imageURL);
                        }catch (Exception e){
                            Log.e("Volley", "Error parsing url.");
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) { }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static RequestQueue getRequestQueue(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{
        public NoteAdapter(Context context){
        }

        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
            MainActivity.fab.show();
            return new NoteViewHolder(view);
        }

        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            holder.title.setText(notes.get(position).getTitle());
            holder.body.setText(notes.get(position).getBody());
            holder.date.setText(notes.get(position).getDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Click!", "Clicked on " + holder.title.getText());
                    //fm.beginTransaction().add(R.id.nav_host_fragment, new EditFragment(), null).addToBackStack(null).commit();
                }
            });
            holder.itemView.setOnTouchListener(new SwipeDetector((Activity) holder.itemView.getContext()));
        }

        public int getItemCount() {
            return notes.size();
        }
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView body;
        public TextView date;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.body  = (TextView)itemView.findViewById(R.id.body);
            this.date  = (TextView)itemView.findViewById(R.id.date);
        }
    }
}