package com.example.group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.group_project.fragments.ComposePostFragment;
import com.example.group_project.fragments.ProfileFragment;
import com.example.group_project.fragments.TodoListsFragment;
import com.example.group_project.fragments.PostsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView bottomNavigationView;
//    SwipeRefreshLayout swipeLayout;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        final Fragment postsFragment = new PostsFragment();
        final Fragment profileFragment = new ProfileFragment();
        final Fragment todoListsFragment = new TodoListsFragment();

//        swipeLayout = findViewById(R.id.swipe_container);
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Toast.makeText(getApplicationContext(), "Loading new posts...", Toast.LENGTH_LONG).show();
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeLayout.setRefreshing(false);
//                    }
//                }, 3000); // delay in millis
//            }
//        });
//
//        swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment fragment;
                        switch (menuItem.getItemId()) {
                            case R.id.action_logout:
                                goLoginActivity();
                            case R.id.action_todo_lists:
                                //Toast.makeText(MainActivity.this, "Lists!", Toast.LENGTH_SHORT).show();
                                fragment = todoListsFragment;
                                break;
                            case R.id.action_profile:
                                //Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                                fragment = profileFragment;
                                break;
                            case R.id.action_entries:
                            default:
                                //Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                                fragment = postsFragment;
                                break;
                        }

                        // navigate to the selected fragment
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });

        // set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_entries);
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