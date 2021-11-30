package com.example.group_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();


    private BottomNavigationView bottomNavigationView;
    SwipeRefreshLayout swipeLayout;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//        final Fragment composeFragment = new ComposeFragment();
//        final Fragment postsFragment = new PostsFragment();
//        final Fragment profileFragment = new ProfileFragment();
        final Fragment settingsFragment = new SettingsFragment();

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Loading new posts...", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // delay in millis
            }
        });

        swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment fragment = settingsFragment;
                        switch (menuItem.getItemId()) {
                            case R.id.action_home:
                                Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                                //fragment = postsFragment;
                                break;
                            case R.id.action_compose:
                                Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
//                                fragment = composeFragment;
                                break;
                            case R.id.settings:
                                Toast.makeText(MainActivity.this, "Settings!", Toast.LENGTH_SHORT).show();
                                fragment = settingsFragment;
                                break;
                            case R.id.action_logout:
                                goLoginActivity();
                            case R.id.action_profile:
                            default:
                                Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();

                                // TODO: Xingguo add your profile Fragment here
//                                fragment = profileFragment;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });

        // set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    // to navigate back to login after logging out
    private void goLoginActivity() {
        // new Intent(<current context>, <target future context>)
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish(); // so that we can't go back to MainActivity screen by hitting the back button
    }
}