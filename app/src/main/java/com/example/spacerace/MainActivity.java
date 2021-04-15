package com.example.spacerace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.spacerace.fragments.EditFragment;
import com.example.spacerace.fragments.JournalFragment;
import com.example.spacerace.fragments.SettingsFragment;
import com.example.spacerace.fragments.WeatherFragment;
import com.example.spacerace.fragments.WordFragment;
import com.example.spacerace.helper.SwipeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity
 * @author Zachary Allard
 */
public class MainActivity extends AppCompatActivity {

    public static ViewPager viewPager;

    public static NavController navController;
    public static BottomNavigationView navView;
    public static int bottomNavPosition = 0;

    public static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Only allow night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // ViewPager to help navigate the bottom nav
        viewPager = findViewById(R.id.fragmentPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            public void onPageSelected(int position) {
                navView.setSelectedItemId(navView.getMenu().getItem(position).getItemId());
                navController.navigate(navView.getMenu().getItem(position).getItemId());
            }
            public void onPageScrollStateChanged(int state) { }
        });

        // Set adapter, etc.
        setupViewPager(viewPager);
        NavigationUI.setupWithNavController(navView, navController);

        // Syncs the ViewPager with the bottom nav
        navView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_journal:
                            setBottomNavPosition(0, false);
                            break;
                        case R.id.navigation_weather:
                            setBottomNavPosition(1, false);
                            break;
                        case R.id.navigation_word:
                            setBottomNavPosition(2, false);
                            break;
                        case R.id.navigation_settings:
                            setBottomNavPosition(3, false);
                            break;
                    }
                    return false;
                });
        invalidateOptionsMenu();
    }

    /**
     * Disables the back button on EditFragment to prevent accidental data loss
     * @author Zachary Allard
     */
    @Override
    public void onBackPressed() {
        // If an EditFragment is visible
        if(findViewById(R.id.edit) != null)
            Log.d("Edit", "Pressed back in EditFragment");
        else
            super.onBackPressed();
    }

    /**
     * Sets the value of position and updates the components
     * @param index Bottom Nav Position (0-indexed)
     * @param navigate Whether to navigate to the fragment at index
     * @author Zachary Allard
     */
    public static void setBottomNavPosition(int index, boolean navigate){
        bottomNavPosition = index;
        viewPager.setCurrentItem(index);
        if(navigate){
            navView.setSelectedItemId(navView.getMenu().getItem(index).getItemId());
            navController.navigate(navView.getMenu().getItem(index).getItemId());
        }
    }
    /**
     * NOTE: The viewpager code was modified from https://github.com/coderminion/Android-Bottom-navigation-with-Viewpager-Fragments
     * Initializes the ViewPager adapter and fragments
     * @param viewPager The ViewPager to configure
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JournalFragment());
        adapter.addFragment(new WeatherFragment());
        adapter.addFragment(new WordFragment());
        adapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }
    // ViewPager adapter
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) { super(manager); }
        @NotNull public Fragment getItem(int position) { return mFragmentList.get(position); }
        public int getCount() { return mFragmentList.size(); }
        public void addFragment(Fragment fragment) { mFragmentList.add(fragment); }
    }
}
