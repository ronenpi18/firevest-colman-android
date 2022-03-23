package com.ronen.sagy.firevest.activities;

// android is deprecated, studio suggested to use androidx https://developer.android.com/jetpack/androidx

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private Context mContext;
    private ViewPager viewPager;
    NavHostFragment navHostFragment;
    AppDatabase db;
    private DatabaseViewModel databaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(DatabaseViewModel.class);

        mContext = this;
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        BottomNavigationView navViewStartUp = findViewById(R.id.bottom_navigation2);
//        BottomNavigationView navViewStartups = findViewById(R.id.bottom_navigation_startups);


        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());
            navView.setOnNavigationItemSelectedListener(this);
        }

    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
            if (firebaseUser != null) {
                try {
                    databaseViewModel.fetchingUserDataCurrent();
                databaseViewModel.fetchUserCurrentData.observe(
                        navHostFragment.getViewLifecycleOwner(), new Observer<DataSnapshot>() {
                            @Override
                            public void onChanged(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.getValue(Users.class);
                                assert users != null;
                                if (users.getTypeOfUser().toLowerCase().equals("startup")) {
                                    BottomNavigationView navView = findViewById(R.id.bottom_navigation);
                                    BottomNavigationView navViewStartUp = findViewById(R.id.bottom_navigation2);

                                    navViewStartUp.setVisibility(View.VISIBLE);

                                    if (navHostFragment != null) {
                                        NavigationUI.setupWithNavController(navViewStartUp, navHostFragment.getNavController());
                                        navViewStartUp.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
                                            @Override
                                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                switch (item.getItemId()) {
                                                    case R.id.account:
                                                        navHostFragment.getNavController().navigate(R.id.accountFragment);
                                                        break;
                                                    case R.id.chat:
                                                        navHostFragment.getNavController().navigate(R.id.activityFragment);
                                                        break;
                                                }
                                                return true;
                                            }
                                        });
                                    }
                                }
                            }
                        });
                } catch (Exception e)
                {
                }
            }
        }
    };

    //
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.account:
                navHostFragment.getNavController().navigate(R.id.accountFragment);
//                viewPager.setCurrentItem(0);
                break;
            case R.id.fire:
                navHostFragment.getNavController().navigate(R.id.swipeFeedFragment);
//                viewPager.setCurrentItem(1);
                break;
            case R.id.chat:
                navHostFragment.getNavController().navigate(R.id.activityFragment);
//                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}