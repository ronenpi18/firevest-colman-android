package com.ronen.sagy.firevest.activities.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.Utils;
import com.ronen.sagy.firevest.entities.SwipedList;
import com.ronen.sagy.firevest.entities.TinderCard;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.ChatList;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SwipeFeedFragment extends Fragment {


    private static final String TAG = "";
    private View rootLayout;
    private FloatingActionButton fabLike, fabSkip;
    private DatabaseViewModel databaseViewModel;
    private ArrayList<ChatList> userList;  //list of all other users with chat record
    private ArrayList<Users> mUSer;
    private AppDatabase database;
    private List<SwipedList> list = new ArrayList<>();

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private String currentUserId;

    private View zeroStateLayout;
    private ImageView zeroStateCenteredImg, imgAnim1, imgAnim2;
    private Handler handlerAnimation;

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

        database = AppDatabase.getInstance(rootLayout.getContext());
        list.clear();
        try {
            list.addAll(database.swipedDao().getAll());
        } catch (SQLiteException e) {

            Log.d(TAG, "onCreateView: "+e.toString());
        }
        mUSer = new ArrayList<>();
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
        zeroStateCenteredImg = view.findViewById(R.id.imgCentered);
        imgAnim1 = view.findViewById(R.id.imgAnim1);
        imgAnim2 = view.findViewById(R.id.imgAnim2);
        handlerAnimation = new Handler();

        Glide.with(getActivity().getBaseContext())
                .load(R.drawable.logo_firevest)
                .apply(new RequestOptions().circleCrop())
                .into(zeroStateCenteredImg);

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

        mSwipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fabSkip.setOnClickListener(v -> {
            if (!mUSer.isEmpty()) {
                animateFab(fabSkip);
                mSwipeView.doSwipe(false);
                Users located_user = mUSer.get(mUSer.size() - 1);

            }
        });

        fabLike.setOnClickListener(v -> {
            if (!mUSer.isEmpty()) {
                animateFab(fabLike);
                mSwipeView.doSwipe(true);
                long tsLong = System.currentTimeMillis();
                String timeStamp = Long.toString(tsLong);
                databaseViewModel.addChatDb(mUSer.get(mUSer.size() - 1).getId(), currentUserId, "Hey, I would like to invest!", timeStamp);
                databaseViewModel.successAddChatDb.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            // Toast.makeText(MessageActivity.this, "Sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Message can't be sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Users located_user = mUSer.get(mUSer.size() - 1);
                database.swipedDao().insertAll(located_user.getEmailId(),
                        located_user.getId(), located_user.getUsername(), "Hey, I would like to invest!");
//            mUSer.remove(mUSer.size() - 1);
            }
        });


    }

    private boolean hasInLocal(Users user) {
        //todo add get
        for (SwipedList swipedList : list) {
            if (swipedList.uidEmail.equals(user.getEmailId()))
                return true;
        }
        return false;
    }

    private void startUpsLists() {
        databaseViewModel.fetchingUserDataCurrent();
        databaseViewModel.fetchUserCurrentData.observe(this, new Observer<DataSnapshot>() {

            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
                currentUserId = users.getId();
            }
        });

        databaseViewModel.fetchUserByNameAll();
        databaseViewModel.fetchUserNames.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);

                    assert user != null;
                    if (!(user.getEmailId() == null)
                    ) {
                        if (!currentUserId.equals(user.getId()) && !hasInLocal(user)) {
                            mUSer.add(user);
                        }
                    }
                    for (Users profile : mUSer) {
                        mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
                    }
//                    userFragmentAdapter = new UserFragmentAdapter(mUSer, context, false);
//                    recyclerView.setAdapter(userFragmentAdapter);

                }
            }
        });

    }


    private Runnable runnableAnim = new Runnable() {
        @Override
        public void run() {
            imgAnim1.animate().scaleX(4f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim1.setScaleX(1f);
                    imgAnim1.setScaleY(1f);
                    imgAnim1.setAlpha(1f);

                }
            });
            imgAnim2.animate().scaleX(4f).alpha(0f).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim2.setScaleX(1f);
                    imgAnim2.setScaleY(1f);
                    imgAnim2.setAlpha(1f);

                }
            });
            handlerAnimation.postDelayed(runnableAnim, 1500);
        }
    };

    private void animateFab(final FloatingActionButton fab) {
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

}
