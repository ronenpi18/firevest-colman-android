package com.ronen.sagy.firevest.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.adapters.UserFragmentAdapter;
import com.ronen.sagy.firevest.services.model.ChatList;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;
import com.ronen.sagy.firevest.viewModel.LogInViewModel;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChatNewFragment extends Fragment {
    private Context context;
    private UserFragmentAdapter userAdapter;
    private ArrayList<Users> mUsers;
    private String currentUserId;
    private ArrayList<ChatList> userList;  //list of all other users with chat record
    private DatabaseViewModel databaseViewModel;
    private LogInViewModel logInViewModel;
    private RecyclerView recyclerView_chat_fragment;
    RelativeLayout relative_layout_chat_fragment;

    public ChatNewFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        init(view);
        fetchAllChat();
        getTokens();


        return view;
    }

    public void getTokens() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String mToken = task.getResult();
                updateToken(mToken); //updating token in firebase database

            }
        });
    }

    private void fetchAllChat() {
        databaseViewModel.fetchingUserDataCurrent();
        databaseViewModel.fetchUserCurrentData.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
                currentUserId = users.getId();
            }
        });

        databaseViewModel.getChaListUserDataSnapshot(currentUserId);
        databaseViewModel.getChaListUserDataSnapshot.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatList chatList = dataSnapshot1.getValue(ChatList.class);
                    userList.add(chatList);
                }

                chatLists();
            }
        });
    }

    private void chatLists() {
        databaseViewModel.fetchUserByNameAll();
        databaseViewModel.fetchUserNames.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);
                    for (ChatList chatList : userList) {
                        assert users != null;
                        if (users.getId().equals(chatList.getId())) {
                            if(!mUsers.contains(users))
                                mUsers.add(users);
                        }
                    }
                }
                if(mUsers.size()<1){
                    relative_layout_chat_fragment.setVisibility(View.VISIBLE);
                }else {
                    relative_layout_chat_fragment.setVisibility(View.GONE);
                }

                userAdapter = new UserFragmentAdapter(mUsers, context, true);
                recyclerView_chat_fragment.setAdapter(userAdapter);
            }
        });
    }


    private void updateToken(String token) {
        logInViewModel.updateToken(token);
    }


    private void init(View view) {
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication()))
                .get(DatabaseViewModel.class);

        logInViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication()))
                .get(LogInViewModel.class);

        relative_layout_chat_fragment = view.findViewById(R.id.relative_layout_chat_fragment);
        recyclerView_chat_fragment = view.findViewById(R.id.recycler_view_chat_fragment);
        recyclerView_chat_fragment.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_chat_fragment.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView_chat_fragment.addItemDecoration(dividerItemDecoration);
        mUsers = new ArrayList<>();
        userList = new ArrayList<>();

    }
}
