package com.ronen.sagy.firevest.activities.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.Utils;
import com.ronen.sagy.firevest.activities.fragments.swipe_view.CardContainer;
import com.ronen.sagy.firevest.activities.fragments.swipe_view.CardModel;
import com.ronen.sagy.firevest.activities.fragments.swipe_view.SimpleCardStackAdapter;
import com.ronen.sagy.firevest.custom_widgets.swiper.SwipeFlingAdapterView;
import com.ronen.sagy.firevest.entities.SwipedList;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.ChatList;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SwipeFeedFragment extends Fragment {
    SimpleCardStackAdapter adapter;
    CardModel cardModel;
    private CardContainer cardContainer;
    private static final String TAG = "";
    private View rootLayout;
    private FloatingActionButton fabLike, fabSkip;
    private DatabaseViewModel databaseViewModel;
    private ArrayList<ChatList> userList;  //list of all other users with chat record
    private ArrayList<Users> mUSer;
    private AppDatabase database;
    private List<SwipedList> list = new ArrayList<>();
    ArrayList<Users> al;
    ArrayAdapter<Users> arrayAdapter;
    //    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private String currentUserId;
    int i = 0;
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
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication()))
                .get(DatabaseViewModel.class);

        database = AppDatabase.getInstance(rootLayout.getContext());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        list.clear();
        try {
            list.addAll(database.swipedDao().getAll());
        } catch (SQLiteException e) {

            Log.d(TAG, "onCreateView: " + e.toString());
        }
        mUSer = new ArrayList<>();
        userList = new ArrayList<>();

        return rootLayout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startUpsLists();

        cardContainer = (CardContainer) view.findViewById(R.id.layoutview);

        Resources r = getResources();

        adapter = new SimpleCardStackAdapter(view.getContext(), databaseViewModel, currentUserId, this);

//        mSwipeView = view.findViewById(R.id.swipeView);
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


        fabSkip.setOnClickListener(v -> {

//            if (!mUSer.isEmpty()) {
//                animateFab(fabSkip);
//                mSwipeView.doSwipe(false);
//                Users located_user = mUSer.get(mUSer.size() - 1);
//
//            }
        });

        fabLike.setOnClickListener(v -> {
//            if (!mUSer.isEmpty()) {
//                animateFab(fabLike);
////                mSwipeView.doSwipe(true);
//                long tsLong = System.currentTimeMillis();
//                String timeStamp = Long.toString(tsLong);
//                databaseViewModel.addChatDb(mUSer.get(mUSer.size() - 1).getId(), currentUserId, "Hey, I would like to invest!", timeStamp);
//                databaseViewModel.successAddChatDb.observe(this, new Observer<Boolean>() {
//                    @Override
//                    public void onChanged(Boolean aBoolean) {
//                        if (aBoolean) {
//                            // Toast.makeText(MessageActivity.this, "Sent.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity(), "Message can't be sent.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                Users located_user = mUSer.get(mUSer.size() - 1);
//                database.swipedDao().insertAll(located_user.getEmailId(),
//                        located_user.getId(), located_user.getUsername(), "Hey, I would like to invest!");
////            mUSer.remove(mUSer.size() - 1);
//            }
        });


    }

    private boolean hasInLocal(Users user) {
        //todo add get
        for (SwipedList swipedListItem : list) {
            if (swipedListItem.uidEmail.equals(user.getEmailId()))
                return true;
        }
        return false;
    }

    private void startUpsLists() {
        databaseViewModel.fetchingUserDataCurrent();
        databaseViewModel.fetchUserCurrentData.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {

            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
//                currentUserId = users.getId();
            }
        });

        databaseViewModel.fetchUserByNameAll();
        databaseViewModel.fetchUserNames.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);

                    assert user != null;
                    if (!(user.getEmailId() == null)
                    ) {
                        if (!currentUserId.equals(user.getId()) && !hasInLocal(user) && !user.getTypeOfUser().toLowerCase().equals("investor")) {
                            mUSer.add(user);
                        }
                    }
                    //                        mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//                    al.addAll(mUSer);
                    for (Users profile : mUSer) {
                        Drawable drawable = null;
                        cardModel = new CardModel(profile.getUsername(),
                                profile.getInvestmentStageOrCapital() + ", " + profile.getFieldOfWork(),
                                profile.getImageUrl(), profile.getId());
                        adapter.add(cardModel);

                    }
                    cardContainer.setAdapter(adapter);

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
