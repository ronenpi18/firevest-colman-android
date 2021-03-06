package com.ronen.sagy.firevest.activities.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.adapters.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityFragment} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {


    private Context mContext;
    private ViewPager viewPager;
    private View rootLayout;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_activity, container, false);
        mContext = getContext();
        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new ChatFragment(mContext));
//        fragList.add(new FeedFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getActivity().getSupportFragmentManager());
        viewPager = rootLayout.findViewById(R.id.view_pager_frag);
        viewPager.setAdapter(pagerAdapter);

        return rootLayout;
    }

//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.layout_chat:
//                viewPager.setCurrentItem(0);
//                chatText.setTextColor(getResources().getColor(R.color.colorPrimary));
//                feedText.setTextColor(getResources().getColor(R.color.light_gray));
//                break;
//            case R.id.layout_feed:
//                viewPager.setCurrentItem(1);
//                chatText.setTextColor(getResources().getColor(R.color.light_gray));
//                feedText.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//
//        }
//    }
}
