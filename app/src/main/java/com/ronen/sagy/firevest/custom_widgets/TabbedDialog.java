package com.ronen.sagy.firevest.custom_widgets;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.fragments.AboutYouFormFragment;
import com.ronen.sagy.firevest.activities.fragments.FinishSignUpFragment;
import com.ronen.sagy.firevest.activities.fragments.ProfileInfoFragment;
import com.ronen.sagy.firevest.adapters.TabbedDialogAdapter;

public class TabbedDialog extends DialogFragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.dialog_sample,container,false);

        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);

        TabbedDialogAdapter adapter = new TabbedDialogAdapter(getChildFragmentManager());
        adapter.addFragment("About you", AboutYouFormFragment.createInstance());
        adapter.addFragment("Profile", ProfileInfoFragment.createInstance());
        adapter.addFragment("Finish", FinishSignUpFragment.createInstance());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootview;
    }
}