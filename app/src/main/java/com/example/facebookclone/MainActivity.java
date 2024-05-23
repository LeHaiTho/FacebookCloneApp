package com.example.facebookclone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.facebookclone.Fragment.ChatFragment;
import com.example.facebookclone.Fragment.HomeFragment;
import com.example.facebookclone.Fragment.MoreFragment;
import com.example.facebookclone.Fragment.NotificationFragment;
import com.example.facebookclone.Fragment.NotificationTabFragment;
import com.example.facebookclone.Fragment.SearchFragment;
import com.example.facebookclone.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNav.setItemIconTintList(null);

        setSupportActionBar(binding.toolbarP);
        MainActivity.this.setTitle("Trang cá nhân");
        binding.toolbarP.setVisibility(View.GONE);
        replaceFragment(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.homeId){
                    binding.toolbarP.setVisibility(View.GONE);
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.searchId) {
                    binding.toolbarP.setVisibility(View.GONE);
                    replaceFragment(new SearchFragment());
//                } else if (item.getItemId() == R.id.notificationId) {
//                    binding.toolbarP.setVisibility(View.GONE);
//                    replaceFragment(new NotificationTabFragment());
                } else if (item.getItemId() == R.id.profileId) {
                    binding.toolbarP.setVisibility(View.VISIBLE);
                    replaceFragment(new MoreFragment());
                }else if (item.getItemId() == R.id.chatId) {
                    binding.toolbarP.setVisibility(View.GONE);
                    replaceFragment(new ChatFragment());
                }
                return true;
            }
        });
//
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settingId){
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}