package com.example.group_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.group_project.LoginActivity;
import com.example.group_project.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

//    private Button btnLogout;
    private Button btnSettings;
    private ImageView ivProfilePicture;
    private TextView tvBio;
    private TextView tvUsername;

    public ProfileFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btnLogout = view.findViewById(R.id.btnLogout);
        btnSettings = view.findViewById(R.id.btnSettings);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        tvBio = view.findViewById(R.id.tvBio);
        tvUsername = view.findViewById(R.id.tvUsername);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer, settingsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
