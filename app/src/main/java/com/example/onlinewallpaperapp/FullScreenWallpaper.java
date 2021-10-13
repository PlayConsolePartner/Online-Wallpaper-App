package com.example.onlinewallpaperapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


public class FullScreenWallpaper extends AppCompatActivity {
    String originalUrl="";
    PhotoView photoView;
    ProgressBar progressBar;
    int checkImage=0;
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
        photoView = findViewById(R.id.photoView);
        progressBar=findViewById(R.id.progressBar);
    }

    public void SetWallpaperEvent(View view) {

        if(checkImage==1){
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
            try {
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Image not loaded yet!", Toast.LENGTH_LONG).show();
        }

    }

    public void DownloadWallpaperEvent(View view) {

        if(checkImage ==1){
            DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(originalUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
            Toast.makeText(this, "Downloading Start", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Image not loaded yet!", Toast.LENGTH_SHORT).show();
        }

    }
}