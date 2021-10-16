package com.example.onlinewallpaperapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    AdView banner_adview;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;
    private MeowBottomNavigation bnv_Main;
    EditText searchET;
    String mediumUrl;
    Boolean isScrolling  = false;
    int currentItems,totalItems,scrollOutItems;
    String url ="https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query=greenery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        RecViewfun();

        getWallpaperData();

        editSearch();

        bottomNavMenu();

        navbarInvibile();

        //banner ad
        Google_Ads google_ads=new Google_Ads(this);
        google_ads.bannerLoad(banner_adview);

    }

//    private void Load_banner_ad() {
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        banner_adview = findViewById(R.id.banner_adview);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        banner_adview.loadAd(adRequest);
//    }

    private void RecViewfun() {
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this,wallpaperModelList, progressBar);

        recyclerView.setAdapter(wallpaperAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling= true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems==totalItems)){
                    isScrolling = false;
                    getWallpaperData();
                }
            }
        });
    }

    private void initialize() {
        searchET=findViewById(R.id.searchET);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);
        banner_adview=findViewById(R.id.banner_adview);
        bnv_Main=findViewById(R.id.bnv_Main);
    }

    private void editSearch() {
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query= "nature";
                    url = "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query;
                    wallpaperModelList.clear();
                    getWallpaperData();
                    return true;
                }
                return false;
            }
        });
    }

    public void getWallpaperData(){

        StringRequest request = new StringRequest(Request.Method.GET,url ,
                new Response.Listener<String>() {
            @Override
                public void onResponse(String response) {

                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray= jsonObject.getJSONArray("photos");

                        int length = jsonArray.length();

                        for(int i=0;i<length;i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");

                            JSONObject objectImages = object.getJSONObject("src");

                            String originalUrl = objectImages.getString("portrait");
                             mediumUrl = objectImages.getString("portrait");


                            WallpaperModel wallpaperModel = new WallpaperModel(id,originalUrl,mediumUrl);
                            wallpaperModelList.add(wallpaperModel);

                        }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        }catch (JSONException e){
                            Log.d("Tag", e.toString());
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001b25c9b076ad54c01b6b57b005285240a");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    public void searchBtnEvent(View view) {

        if (searchET.getText().toString().equals("")) {
            Toast.makeText(this, "Type something like nature, texture etc", Toast.LENGTH_SHORT).show();
        } else {

            try {
                String query = searchET.getText().toString().toLowerCase();

                url = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query;
                wallpaperModelList.clear();
                getWallpaperData();
            } catch (Exception e) {
                Log.d("tag", e.toString());
            }
        }


    }

    public void refreshBtnEvent(View view){
        try{
            String query= "greenery";
            url = "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query;
            wallpaperModelList.clear();
            getWallpaperData();
            searchET.getText().clear();
        }catch (Exception e){
            Log.d("tag", e.toString());
        }
    }

    private void bottomNavMenu() {
        bnv_Main.add(new MeowBottomNavigation.Model(1, R.drawable.ic_privacy_policy));
        bnv_Main.add(new MeowBottomNavigation.Model(2, R.drawable.ic_rate_us));
        bnv_Main.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_share_24));


        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {

                    case 1:
                        Uri uri = Uri.parse("https://www.google.com");
                     Intent   intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;

                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ getApplicationContext().getPackageName())));

                        break;

                    case 3:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            String shareMessage = "\nTry this wonderful application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;



                }
                return null;
            }
        });
    }

    private void navbarInvibile() {
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d("TAG","onVisibilityChanged: Keyboard visibility changed");
                        if(isOpen){
                            Log.d("TAG", "onVisibilityChanged: Keyboard is open");
                            bnv_Main.setVisibility(View.GONE);
                            Log.d("TAG", "onVisibilityChanged: NavBar got Invisible");
                        }else{
                            Log.d("TAG", "onVisibilityChanged: Keyboard is closed");
                            bnv_Main.setVisibility(View.VISIBLE);
                            Log.d("TAG", "onVisibilityChanged: NavBar got Visible");
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(this, Exit_Screen.class);
        startActivity(intent);
    }
}