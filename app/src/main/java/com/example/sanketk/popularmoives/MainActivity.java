package com.example.sanketk.popularmoives;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null)
        {
            return;
        }
        init();
    }

    private void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainActivityFragment()).commit();
    }



}
