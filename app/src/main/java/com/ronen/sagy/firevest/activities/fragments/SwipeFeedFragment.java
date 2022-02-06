package com.ronen.sagy.firevest.activities.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.Utils;
import com.ronen.sagy.firevest.adapters.UserFragmentAdapter;
import com.ronen.sagy.firevest.entities.Profile;
import com.ronen.sagy.firevest.entities.TinderCard;
import com.ronen.sagy.firevest.services.model.ChatList;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SwipeFeedFragment extends Fragment {


    private View rootLayout;
    private FloatingActionButton fabLike, fabSkip;
    private DatabaseViewModel databaseViewModel;
    private ArrayList<ChatList> userList;  //list of all other users with chat record
    private ArrayList<Users> mUsers;

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    public SwipeFeedFragment() {
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
        rootLayout = inflater.inflate(R.layout.fragment_swipe_feed, container, false);
        databaseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(DatabaseViewModel.class);
        mUsers = new ArrayList<>();
        userList = new ArrayList<>();

        return rootLayout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startUpsLists();
        mSwipeView = view.findViewById(R.id.swipeView);
        fabLike = view.findViewById(R.id.fabLike);
        fabSkip = view.findViewById(R.id.fabSkip);
        mContext = getActivity();

        int bottomMargin = Utils.dpToPx(100);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        for(Users profile : mUsers){
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
        }

        fabSkip.setOnClickListener(v -> {
            animateFab(fabSkip);
            mSwipeView.doSwipe(false);
        });

        fabLike.setOnClickListener(v -> {
            animateFab(fabLike);
            mSwipeView.doSwipe(true);
        });

    }

//    private void startUpsLists() {
//        databaseViewModel.fetchAllStartUps();
//        databaseViewModel.fetchStartUps.observe(this, new Observer<DataSnapshot>() {
//            @Override
//            public void onChanged(DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Users users = dataSnapshot1.getValue(Users.class);
//                    for (ChatList chatList : userList) {
//                        assert users != null;
//                        if (users.getId().equals(chatList.getId())) {
//                            if (!mUsers.contains(users))
//                                mUsers.add(users);
//                        }
//                    }
//                }
//                userAdapter = new UserFragmentAdapter(mUsers, context, true);
//                recyclerView_chat_fragment.setAdapter(userAdapter);
//            }
//        });
//    }
    private void startUpsLists() {
        databaseViewModel.fetchUserByNameAll();
        databaseViewModel.fetchUserNames.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);
                    if (users.getTypeOfUser().equals("startup")) {
                        if(!mUsers.contains(users))
                                mUsers.add(users);
                    }
//                    for (ChatList chatList : userList) {
//                        assert users != null;
//                        Toast.makeText(getContext(), chatList.getId(), Toast.LENGTH_SHORT).show();
//                        if (users.getTypeOfUser().equals("startup")) {
//                            if(!mUsers.contains(users))
//                                mUsers.add(users);
//                        }
//                    }
                }
                for(Users profile : mUsers){
                    mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
                }
//                userAdapter = new UserFragmentAdapter(mUsers, context, true);
//                recyclerView_chat_fragment.setAdapter(userAdapter);
            }
        });
    }

    private void animateFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

}
