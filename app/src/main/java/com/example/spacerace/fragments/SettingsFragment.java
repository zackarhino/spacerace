package com.example.spacerace.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.spacerace.R;

/**
 * Various settings, credits, etc.
 * @author Zachary Allard
 */
public class SettingsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button twitter = view.findViewById(R.id.twitter);
        Button instagram = view.findViewById(R.id.instagram);
        Button facebook = view.findViewById(R.id.facebook);
        Button youtube = view.findViewById(R.id.youtube);

        ImageView settings_icon = view.findViewById(R.id.settings);

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

        settings_icon.setOnClickListener(new SettingsListener(getContext()));

        return view;
    }

    /**
     * Display a Toast when changing a setting
     * @param settingName The name of the setting being changed
     * @param settingValue The value the setting has been changed to
     */
    public void displaySettingsChangeToast(String settingName, String settingValue){
        Toast.makeText(getContext(), settingName + " has been set to " + settingValue, Toast.LENGTH_SHORT).show();
    }

    public class SettingsListener implements View.OnClickListener{
        Context context;

        public SettingsListener(Context context){
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    boolean setting_border = sharedPreferences.getBoolean(getString(R.string.setting_key_border), false);
                    boolean setting_scale_type = sharedPreferences.getBoolean(getString(R.string.setting_key_scale_type), false);
                    boolean setting_resolution = sharedPreferences.getBoolean(getString(R.string.setting_key_resolution), false);
                    boolean setting_date_format = sharedPreferences.getBoolean(getString(R.string.setting_key_date_format), false);
                    switch (menuItem.getItemId()){
                        case R.id.menu_border:
                            if(setting_border)
                                displaySettingsChangeToast("Note border", "OFF");
                            else
                                displaySettingsChangeToast("Note border", "ON");
                            edit.putBoolean(getString(R.string.setting_key_border), !setting_border);
                            break;
                        case R.id.menu_scale_type:
                            if(setting_scale_type)
                                displaySettingsChangeToast("Scale type", "FILL");
                            else
                                displaySettingsChangeToast("Scale type", "FIT");
                            edit.putBoolean(getString(R.string.setting_key_scale_type), !setting_scale_type);
                            break;
                        case R.id.menu_resolution:
                            if(setting_resolution)
                                displaySettingsChangeToast("Image Resolution", "STANDARD");
                            else
                                displaySettingsChangeToast("Image Resolution", "HD");
                            edit.putBoolean(getString(R.string.setting_key_resolution), !setting_resolution);
                            break;
                        case R.id.menu_date_format:
                            if(setting_date_format)
                                displaySettingsChangeToast("Date format", "NORMAL");
                            else
                                displaySettingsChangeToast("Date format", "STARDATE");
                            edit.putBoolean(getString(R.string.setting_key_date_format), !setting_date_format);
                            break;
                        default:
                            Log.e("Settings", "Invalid menu option.");
                    }
                    edit.apply();
                    return true;
                }
            });

            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.END);
            popupMenu.show();
        }
    }
}