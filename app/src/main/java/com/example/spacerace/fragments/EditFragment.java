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
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_BODY = "body";
    private static final String ARG_DATE = "date";

    private int id;
    private String title;
    private String body;
    private String date;

    private boolean isNewNote;

    public EditText titleEditText;
    public EditText bodyEditText;

    public ExtendedFloatingActionButton fab;

    public EditFragment(boolean newNote) {
        this.isNewNote = newNote;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title The title of the note
     * @param body The note's text
     * @param date The date the note was updated
     * @return A new instance of fragment EditFragment.
     */
    public static EditFragment newInstance(int id, String title, String body, String date) {
        EditFragment fragment = new EditFragment(false);
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BODY, body);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
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
        titleEditText.setFocusable(true);
        bodyEditText.setFocusable(true);

        titleEditText.setText(this.title);
        bodyEditText.setText(this.body);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("View", "Detached EditFragment");
        save(false);
    }

    /**
     * Save a note using the details in the view
     * @param close Whether to navigate away
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
     * This is a bit hacky but it's otherwise a logistical nightmare
     */
    private void close(){
        titleEditText.setFocusable(false);
        bodyEditText.setFocusable(false);

        MainActivity.navView.setVisibility(View.VISIBLE);

        MainActivity.fm.popBackStack();
        MainActivity.setBottomNavPosition(0, true);
    }
}