package com.onex.trending_wallpapers_2021;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Exit_Screen extends Native_Ad_Class {
  Button btn_exit, btn_RateUs;
  ImageButton btn_cancel;

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
        btn_cancel=findViewById(R.id.btn_cancel);

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

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}