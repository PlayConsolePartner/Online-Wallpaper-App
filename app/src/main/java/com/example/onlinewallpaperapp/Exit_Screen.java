package com.example.onlinewallpaperapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Exit_Screen extends Native_Ad_Class {
  Button btn_exit, btn_RateUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_screen);

        initialize();

        //Native ad callback
        refresh_native_Ad(this);

        //Exit & Rate us buttons
        buttonClick();
    }

    private void initialize() {
        btn_exit=findViewById(R.id.btn_exit);
        btn_RateUs=findViewById(R.id.btn_RateUs);

    }

    private void buttonClick() {

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                moveTaskToBack(true);
            }
        });

        btn_RateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getApplicationContext().getPackageName())));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}