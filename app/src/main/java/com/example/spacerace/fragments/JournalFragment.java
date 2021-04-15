package com.example.spacerace.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.spacerace.MainActivity;
import com.example.spacerace.R;
import com.example.spacerace.api.VolleySingleton;
import com.example.spacerace.database.Note;
import com.example.spacerace.database.NoteDB;
import com.example.spacerace.helper.SwipeDetector;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Holds all the user's Notes and displays the Astronomy Pic of the Day from NASA's API
 * @author Zachary Allard
 */
public class JournalFragment extends Fragment {

    public static ArrayList<Note> notes;
    public static NoteAdapter adapter;
    public static SimpleDateFormat isoFormatter;

    public SwipeDetector swipeDetector;

    public ImageView apod_imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        swipeDetector = new SwipeDetector(getActivity());
        apod_imageview = view.findViewById(R.id.imageView);
        isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", getResources().getConfiguration().locale);
        // Add a note when fab is clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navView.setVisibility(View.GONE);
                Bundle arguments = new Bundle();
                arguments.putBoolean("newNote", true);
                MainActivity.navController.navigate(R.id.navigation_edit, arguments);
            }
        });

        adapter = new NoteAdapter(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        // Listen for the ViewPager swipes that are eaten by the NestedScrollView
        recyclerView.setOnTouchListener(new SwipeDetector(getActivity()));

        notes = getAllNotes(getContext());
        // Add a placeholder note if empty
        if(notes.isEmpty()){
            NoteDB db = new NoteDB(getContext());
            Note note = new Note("Hello, World!", "Lorem Ipsum", isoFormatter.format(new Date()));
            notes.add(note);
            db.addNote(note);
            adapter.notifyDataSetChanged();
            db.closeDB();
        }
        // Set scale type before displaying
        if(sharedPreferences.getBoolean(getString(R.string.setting_key_scale_type), false))
            apod_imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        else
            apod_imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        updateNasaImage("i68uCMxS1T7fZjiMEbW3E0qEc0tMBxQaCxYZ08ZL");

        return view;
    }

    /**
     * Get all Notes from the database and return them
     * @return The ArrayList of all Notes
     * @author Zachary Allard
     */
    public static ArrayList<Note> getAllNotes(Context context){
        ArrayList<Note> notes;
        NoteDB db = new NoteDB(context);
        notes = db.getAllNotes();
        db.closeDB();
        return notes;
    }

    /**
     * Pulls the daily image from the API
     * @param apiKey The API key to query the APOD API with
     * @author Zachary Allard
     */
    private void updateNasaImage(String apiKey){
        VolleySingleton volley = VolleySingleton.getInstance(getContext());
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;
        String resolution;
        // Choose resolution based on preferences
        if(sharedPreferences.getBoolean(getString(R.string.setting_key_resolution), false))
            resolution = "hdurl";
        else resolution = "url";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        String imageURL = "";
                        try {
                            imageURL = response.getString(resolution);
                            Picasso.get().load(imageURL).into(apod_imageview);
                            //Log.d("Volley", imageURL);
                        }catch (Exception e){
                            Log.e("Volley", "Error parsing url.");
                            // Placeholder image
                            Picasso.get().load(R.drawable.spacerace_logo).into(apod_imageview);
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) { error.printStackTrace(); }
        });
        volley.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Note Adapter
     * @author Zachary Allard
     */
    public static class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{
        Context context;
        Activity activity;
        SimpleDateFormat dateFormatter;
        SharedPreferences sharedPreferences;

        public NoteAdapter(Context context){
            this.context = context;
            this.activity = (Activity) context;
            this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", context.getResources().getConfiguration().locale);
        }

        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
            sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
            return new NoteViewHolder(view, context);
        }

        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            // Set border based on preferences
            MaterialCardView cv = holder.itemView.findViewById(R.id.card);
            if(sharedPreferences.getBoolean(activity.getString(R.string.setting_key_border), false))
                cv.setStrokeWidth((int)activity.getResources().getDimension(R.dimen.note_border_width));
            else cv.setStrokeWidth(0);

            holder.title.setText(notes.get(position).getTitle());
            holder.body.setText(notes.get(position).getBody());
            String date = notes.get(position).getDate();
            try{
                date = dateFormatter.parse(date).toString();
            }catch (ParseException e){
                Log.d("Date", "Error parsing date.");
                e.printStackTrace();
            }
            // Check date format from preferences
            if(sharedPreferences.getBoolean(activity.getString(R.string.setting_key_date_format), false))
                date = "Stardate: " + (new Random().nextInt(9001)+1) + "." + (new Random().nextInt(99)+1);
            holder.date.setText(date);
            holder.itemView.setOnTouchListener(new SwipeDetector((Activity) context));
            // Open EditFragment
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle arguments = new Bundle();
                    arguments.putBoolean("newNote", false);
                    arguments.putInt("id", notes.get(position).getId());
                    arguments.putString("title", notes.get(position).getTitle());
                    arguments.putString("body", notes.get(position).getBody());
                    MainActivity.navView.setVisibility(View.GONE);
                    MainActivity.navController.navigate(R.id.action_navigation_journal_to_editFragment, arguments);
                }
            });
            // Delete on long click
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setMessage("Are you sure you want to delete " + holder.title.getText() + "?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    NoteDB db = new NoteDB(context);
                                    db.deleteNote(notes.get(position).getId());
                                    notes.remove(position);
                                    JournalFragment.adapter.notifyItemRemoved(position);
                                    db.closeDB();
                                }
                            })
                            .show();
                    return true;
                }
            });
        }

        public int getItemCount() { return notes.size(); }
    }

    /**
     * Note View Holder
     * @author Zachary Allard
     */
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView body;
        public TextView date;
        Context context;

        public NoteViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.title);
            this.body  = (TextView)itemView.findViewById(R.id.body);
            this.date  = (TextView)itemView.findViewById(R.id.date);
            this.context = context;
        }
    }
}