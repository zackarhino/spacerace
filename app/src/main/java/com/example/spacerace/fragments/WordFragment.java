package com.example.spacerace.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spacerace.R;
import com.example.spacerace.Words;

import java.util.ArrayList;
import java.util.Random;

public class WordFragment extends Fragment {

    enum Categories {
        Space, Nerdy, Fun, All
    }

    Random r = new Random();

    Spinner word_groups;
    TextView word_textview;
    Button big_bang_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initial Word
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);

        word_textview = view.findViewById(R.id.word);
        word_groups = view.findViewById(R.id.word_groups);
        big_bang_button = view.findViewById(R.id.button);

        // Setup spinner
        word_groups.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.word_groups, R.layout.support_simple_spinner_dropdown_item));

        // Setup Button
        big_bang_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word;
                switch (word_groups.getSelectedItem().toString()){
                    case "Space Words":
                        word = getRandomWord(Categories.Space);
                        break;
                    case "Nerdy Words":
                        word = getRandomWord(Categories.Nerdy);
                        break;
                    case "Fun Words":
                        word = getRandomWord(Categories.Fun);
                        break;
                    case "All Words":
                        word = getRandomWord(Categories.All);
                        break;
                    default:
                        word = "I AM ERROR";
                }
                word_textview.setText(word);
            }
        });

        return view;
    }

    /**
     * Pick a word from all available words in Word and return it
     * @return The random word
     */
    public String getRandomWord(Categories category){
        ArrayList<String> wordList;
        switch (category){
            case Space:
                wordList = Words.SPACE_WORDS;
                break;
            case Nerdy:
                wordList = Words.NERDY_WORDS;
                break;
            case Fun:
                wordList = Words.FUN_WORDS;
                break;
            default:
                wordList = Words.getAllWords();
        }
        return wordList.get(r.nextInt(wordList.size()));
    }
}