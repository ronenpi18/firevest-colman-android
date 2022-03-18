package com.ronen.sagy.firevest.entities;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.Users;


public class TinderCard {

    private ImageView profileImageView;

    private TextView nameAgeTxt;

    private TextView locationNameTxt;
    private AppDatabase database;

    private Users mProfile;
    private Context mContext;

    public TinderCard(Context context, Users profile) {
        mContext = context;
        mProfile = profile;
        database = AppDatabase.getInstance(mContext);

    }

    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getUsername() + ", " + mProfile.getInvestmentStageOrCapital());
        locationNameTxt.setText(mProfile.getFieldOfWork());
    }

//    @SwipeInState
//    private void onSwipeInState(){
//        Log.d("EVENT", "onSwipeInState");
////        database.swipedDao().insertAll(mProfile.getEmailId(),
////                mProfile.getId(), mProfile.getUsername(), "Hey, I would like to invest!");
//    }
//
//    @SwipeOutState
//    private void onSwipeOutState(){
//        Log.d("EVENT", "onSwipeOutState");
//
//        database.swipedDao().insertAll(mProfile.getEmailId(),
//                mProfile.getId(), mProfile.getUsername(), "");
//    }
}