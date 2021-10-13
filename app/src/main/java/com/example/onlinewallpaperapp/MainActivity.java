package com.example.onlinewallpaperapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;
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

    }

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
}