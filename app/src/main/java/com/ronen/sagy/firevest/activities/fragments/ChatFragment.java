package com.ronen.sagy.firevest.activities.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.MainActivity;
import com.ronen.sagy.firevest.adapters.LikeAdapter;
import com.ronen.sagy.firevest.adapters.MessageListAdapter;
import com.ronen.sagy.firevest.adapters.UserFragmentAdapter;
import com.ronen.sagy.firevest.entities.Like;
import com.ronen.sagy.firevest.entities.MessageItem;
import com.ronen.sagy.firevest.services.model.ChatList;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;
import com.ronen.sagy.firevest.viewModel.LogInViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class ChatFragment extends Fragment {

    private Context context;
    private UserFragmentAdapter userAdapter;
    private ArrayList<Users> mUsers;
    private ArrayList<Users> searchedUsers;
    private String currentUserId;
    private ArrayList<ChatList> userList;  //list of all other users with chat record
    private DatabaseViewModel databaseViewModel;
    private LogInViewModel logInViewModel;
    private AppCompatEditText search_panel;
    private RecyclerView recyclerView_chat_fragment;
    TextView msgCount;
    View rootLayout;
    private static final String TAG = MainActivity.class.getSimpleName();

    public ChatFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_chat, container, false);
        databaseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(DatabaseViewModel.class);

        logInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(LogInViewModel.class);

//        relative_layout_chat_fragment = rootLayout.findViewById(R.id.relative_layout_chat_fragment);
//        recyclerView_chat_fragment = view.findViewById(R.id.recycler_view_chat_fragment);
        recyclerView_chat_fragment = rootLayout.findViewById(R.id.recycler_view_messages);
        msgCount = rootLayout.findViewById(R.id.text_count_messsage);
        recyclerView_chat_fragment.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_chat_fragment.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView_chat_fragment.addItemDecoration(dividerItemDecoration);
        mUsers = new ArrayList<>();
        searchedUsers = new ArrayList<>();
        userList = new ArrayList<>();

        search_panel = rootLayout.findViewById(R.id.search_panel);
        search_panel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!search_panel.getText().toString().equals("")) {
                    for (Users mUser : mUsers) {

                        if (mUser.getUsername().contains(s))
                            continue;
                        mUsers.remove(mUser);
                    }
                    userAdapter = new UserFragmentAdapter(mUsers, context, true);
                    recyclerView_chat_fragment.setAdapter(userAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (search_panel.getText().toString().startsWith(" ")) {
                    search_panel.setText("");
                    fetchAllChat();
//                    searchedUsers.clear();
//                    userAdapter = new UserFragmentAdapter(mUsers, context, true);
//                    recyclerView_chat_fragment.setAdapter(userAdapter);
                }
                if (search_panel.getText().toString().equals(""))
                    fetchAllChat();
            }
        });
        fetchAllChat();
        getTokens();
        return rootLayout;
    }

    private void updateToken(String token) {
        logInViewModel.updateToken(token);
    }
//
//    private void searchUsers(String searchText) {
//
//        if (!(searchText.isEmpty() && searchText.equals(""))) {
//            databaseViewModel.fetchSearchedUser(searchText);
//            databaseViewModel.fetchSearchUser.observe(this, new Observer<DataSnapshot>() {
//                @Override
//                public void onChanged(DataSnapshot dataSnapshot) {
//                    mUSer.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Users users = snapshot.getValue(Users.class);
//                        assert users != null;
//                        if (!users.getId().equals(currentUserId)) {
//                            mUSer.add(users);
//                        }
//
//                    }
//                    userFragmentAdapter = new UserFragmentAdapter(mUSer, context, false);
//                    recyclerView.setAdapter(userFragmentAdapter);
//
//                }
//            });
//        } else {
//            fetchingAllUserNAme();
//        }
//    }

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
                            if (!mUsers.contains(users))
                                mUsers.add(users);
                        }
                    }
                }
//                if(mUsers.size()<1){
//                    relative_layout_chat_fragment.setVisibility(View.VISIBLE);
//                }else {
//                    relative_layout_chat_fragment.setVisibility(View.GONE);
//                }

                userAdapter = new UserFragmentAdapter(mUsers, context, true);
                recyclerView_chat_fragment.setAdapter(userAdapter);
                msgCount.setText(String.valueOf(mUsers.size()));
            }
        });
    }

    private void fetchAllChat() {
        databaseViewModel.fetchingUserDataCurrent();
        databaseViewModel.fetchUserCurrentData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
                currentUserId = users.getId();
            }
        });

        databaseViewModel.getChaListUserDataSnapshot(currentUserId);
        databaseViewModel.getChaListUserDataSnapshot.observe(this, new Observer<DataSnapshot>() {
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


}
