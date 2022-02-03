package com.ronen.sagy.firevest.activities;

// android is deprecated, studio suggested to use androidx https://developer.android.com/jetpack/androidx

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private Context mContext;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        mContext = this;

        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new AccountFragment());
        fragList.add(new SwipeFeedFragment());
        fragList.add(new ActivityFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        bnv.setOnNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.account:
                viewPager.setCurrentItem(0);
                break;
            case R.id.fire:
                viewPager.setCurrentItem(1);
                break;
            case R.id.chat:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }
}