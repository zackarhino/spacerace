package com.example.spacerace.fragments;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.spacerace.MainActivity;
import com.example.spacerace.R;
import com.example.spacerace.database.Note;
import com.example.spacerace.database.NoteDB;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Calendar;
import java.util.Date;

/**
 * Edit Fragment for creating and updating Notes
 * @author Zachary Allard
 */
public class EditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NEW_NOTE = "newNote";
    private static final String ARG_TITLE = "title";
    private static final String ARG_BODY = "body";
    private static final String ARG_DATE = "date";

    private int id;
    private String title;
    private String body;
    private String date;

    // Determines whether to add a new note or update
    private boolean isNewNote;

    public EditText titleEditText;
    public EditText bodyEditText;

    public ExtendedFloatingActionButton fab;

    public EditFragment() {
        // Required Empty Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            isNewNote = getArguments().getBoolean(ARG_NEW_NOTE);
            title = getArguments().getString(ARG_TITLE);
            body = getArguments().getString(ARG_BODY);
            date = getArguments().getString(ARG_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(true);

                // Close the soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        titleEditText = view.findViewById(R.id.title);
        bodyEditText = view.findViewById(R.id.body);

        titleEditText.setText(this.title);
        bodyEditText.setText(this.body);

        return view;
    }

    /**
     * Save a note using the details in the view
     * @param close Whether to navigate away
     * @author Zachary Allard
     */
    private void save(boolean close){
        NoteDB db = new NoteDB(getContext());
        Note note = new Note(id, titleEditText.getText().toString(), bodyEditText.getText().toString(), JournalFragment.isoFormatter.format(new Date()));
        if(isNewNote)
            db.addNote(note);
        else
            db.updateNote(note);
        db.closeDB();
        if(close)
            close();
    }

    /**
     * Navigates away from the EditFragment and returns to Journal
     * @author Zachary Allard
     */
    private void close(){
        MainActivity.setBottomNavPosition(0, true);
        MainActivity.navView.setVisibility(View.VISIBLE);
    }
}