package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_MyApplication);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Product Search");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.refresh();
    }
}