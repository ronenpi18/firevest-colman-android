package com.ronen.sagy.firevest.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ronen.sagy.firevest.R;


public class ProfileInfoFragment extends Fragment {

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    public static ProfileInfoFragment createInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }
}