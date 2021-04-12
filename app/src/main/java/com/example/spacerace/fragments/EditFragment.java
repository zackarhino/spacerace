package com.example.spacerace.fragments;

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

import java.util.Calendar;

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

    public EditFragment() {
        // Required empty public constructor
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
        EditFragment fragment = new EditFragment();
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
//        MainActivity.fab.show();
//        MainActivity.fab.setText(getResources().getString(R.string.fab_save));
//        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Hide the soft keyboard
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                save();
//                getActivity().getSupportFragmentManager().popBackStack();
//            }
//        });

        EditText title = view.findViewById(R.id.title);
        EditText body = view.findViewById(R.id.body);

        title.setText(this.title);
        body.setText(this.body);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        save();
        Log.d("View", "Detached EditFragment");
    }

    private void save(){
        NoteDB db = new NoteDB(getContext());
        Note note = new Note(id, title, body, Calendar.getInstance().getTime().toString());
        db.updateNote(note);
        db.closeDB();
        JournalFragment.adapter.notifyDataSetChanged();
    }
}