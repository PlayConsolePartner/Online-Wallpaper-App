package com.onex.trending_wallpapers_2021;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.onex.trending_wallpapers_2021.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.IOException;


public class FullScreenWallpaper extends AppCompatActivity {
    String originalUrl="";
    PhotoView photoView;
    ProgressBar progressBar;
    int checkImage=0;
    private InterstitialAd mInterstitialAd;
    private String inter_unit_id;
    Google_Ads google_ads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);

        initialize();

        Intent intent =  getIntent();
        originalUrl = intent.getStringExtra("originalUrl");
        Glide.with(this)
                .load(originalUrl)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(FullScreenWallpaper.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                checkImage=1;
                return false;
            }
        }).into(photoView);

    }



    private void initialize() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        photoView = findViewById(R.id.photoView);
        progressBar=findViewById(R.id.progressBar);
        inter_unit_id= getResources().getString(R.string.interstitial_unit_id);

        google_ads= new Google_Ads(this);
        google_ads.loadInterstitial();
    }

    public void SetWallpaperEvent(View view) {

        if(checkImage==1){

            if (ContextCompat.checkSelfPermission(FullScreenWallpaper.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(FullScreenWallpaper.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FullScreenWallpaper.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                ActivityCompat.requestPermissions(FullScreenWallpaper.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                Toast.makeText(FullScreenWallpaper.this, "Need Permission to access storage for Setting Wallpaper...", Toast.LENGTH_SHORT).show();
            }
            else{
                google_ads.showInterstitial(this);

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else {
            Toast.makeText(this, "Image not loaded yet!", Toast.LENGTH_LONG).show();
        }

    }

    public void DownloadWallpaperEvent(View view) {

        if(checkImage ==1){

            if (ContextCompat.checkSelfPermission(FullScreenWallpaper.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(FullScreenWallpaper.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FullScreenWallpaper.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                ActivityCompat.requestPermissions(FullScreenWallpaper.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                Toast.makeText(FullScreenWallpaper.this, "Need Permission to access storage for Downloading Image", Toast.LENGTH_SHORT).show();
            } else {
               google_ads.showInterstitial(this);

                Toast.makeText(FullScreenWallpaper.this, "Downloading Image...", Toast.LENGTH_SHORT).show();

                AltexImageDownloader.writeToDisk(this, originalUrl, "IMAGES");
            }
        }
        else{
            Toast.makeText(this, "Image not loaded yet!", Toast.LENGTH_SHORT).show();
        }

    }


}