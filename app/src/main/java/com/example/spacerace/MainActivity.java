package com.example.spacerace;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.spacerace.fragments.JournalFragment;
import com.example.spacerace.fragments.SettingsFragment;
import com.example.spacerace.fragments.WeatherFragment;
import com.example.spacerace.fragments.WordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Only allow night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_journal, R.id.navigation_weather, R.id.navigation_word, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        
        // Viewpager to help navigate the bottom nav
        viewPager = findViewById(R.id.fragmentPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            public void onPageSelected(int position) {
                navView.getMenu().getItem(0).setChecked(false);
                navView.getMenu().getItem(position).setChecked(true);
                Log.d("page",""+position);
                navView.setSelectedItemId(navView.getMenu().getItem(position).getItemId()); }
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
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_weather:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_word:
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.navigation_settings:
                            viewPager.setCurrentItem(3);
                            break;
                    }
                    return false;
                });
    }
    // NOTE: The viewpager code was modified from https://github.com/coderminion/Android-Bottom-navigation-with-Viewpager-Fragments
    /**
     * Initializes the ViewPager adapter and fragments
     * @param viewPager The ViewPager to configure
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        JournalFragment journalFragment = new JournalFragment();
        WeatherFragment weatherFragment = new WeatherFragment();
        WordFragment wordFragment = new WordFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        adapter.addFragment(journalFragment);
        adapter.addFragment(weatherFragment);
        adapter.addFragment(wordFragment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
    }
    // ViewPager adapter
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) { super(manager); }
        @NotNull public Fragment getItem(int position) { return mFragmentList.get(position); }
        public int getCount() { return 4; }
        public void addFragment(Fragment fragment) { mFragmentList.add(fragment); }
    }
}
