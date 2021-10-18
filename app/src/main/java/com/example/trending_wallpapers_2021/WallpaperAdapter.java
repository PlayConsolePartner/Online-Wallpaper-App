package com.example.trending_wallpapers_2021;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trending_wallpapers_2021.R;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperViewHolder> {

    private Context context;
    private List<WallpaperModel> wallpaperModelList;
    ProgressBar progressBar;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModelList, ProgressBar progressBar) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
        this.progressBar=progressBar;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(wallpaperModelList.get(position).getMediumUrl()).into(holder.imageView);

        progressBar.setVisibility(View.GONE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,FullScreenWallpaper.class)
                        .putExtra("originalUrl",wallpaperModelList.get(position).getOriginalUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

}
class WallpaperViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageViewItem);
    }
}