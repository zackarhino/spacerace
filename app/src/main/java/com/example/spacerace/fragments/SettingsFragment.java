package com.example.spacerace.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.spacerace.MainActivity;
import com.example.spacerace.R;

public class SettingsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button twitter = view.findViewById(R.id.twitter);
        Button instagram = view.findViewById(R.id.instagram);
        Button facebook = view.findViewById(R.id.facebook);
        Button youtube = view.findViewById(R.id.youtube);

        // Social Media Intents
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://twitter.com/zackarhino");
                i.setData(uri);
                startActivity(i);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://instagram.com/zackarhino");
                i.setData(uri);
                startActivity(i);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.facebook.com/zack.allard.77/");
                i.setData(uri);
                startActivity(i);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/channel/UC631OTF6OPuvELOKWtiflGg");
                i.setData(uri);
                startActivity(i);
            }
        });

        return view;
    }
}