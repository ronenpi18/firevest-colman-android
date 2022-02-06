package com.ronen.sagy.firevest.activities;

// android is deprecated, studio suggested to use androidx https://developer.android.com/jetpack/androidx

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.fragments.AccountFragment;
import com.ronen.sagy.firevest.activities.fragments.ActivityFragment;
import com.ronen.sagy.firevest.activities.fragments.ProfileFragment;
import com.ronen.sagy.firevest.activities.fragments.SwipeFeedFragment;
import com.ronen.sagy.firevest.adapters.ViewPagerAdapter;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.UserDao;
import com.ronen.sagy.firevest.services.model.Users;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private Context mContext;
    private ViewPager viewPager;
    NavHostFragment navHostFragment;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        mContext = this;
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());
            navView.setOnNavigationItemSelectedListener(this);
        }

//        ArrayList<Fragment> fragList = new ArrayList<>();
//        fragList.add(new AccountFragment());
//        fragList.add(new SwipeFeedFragment());
//        fragList.add(new ActivityFragment());
//        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getSupportFragmentManager());
//        viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
//        bnv.setOnNavigationItemSelectedListener(this);
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
                Toast.makeText(MainActivity.this, "Logged in @ MainActivity:69", Toast.LENGTH_SHORT).show();
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