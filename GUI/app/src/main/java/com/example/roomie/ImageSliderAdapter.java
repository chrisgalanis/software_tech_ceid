package com.example.roomie;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class ImageSliderAdapter
        extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder> {

  private final Context context;
  private final List<String> imageUris;

  public ImageSliderAdapter(Context context, List<String> imageUris) {
    this.context   = context;
    this.imageUris = imageUris != null ? imageUris : Collections.emptyList();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(
          @NonNull ViewGroup parent, int viewType) {
    ImageView iv = new ImageView(context);
    iv.setLayoutParams(
            new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
    );
    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    return new ViewHolder(iv);
  }

  @Override
  public void onBindViewHolder(
          @NonNull ViewHolder holder, int position) {
    String uri = imageUris.get(position);
    Glide.with(context)
            .load(uri)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(holder.imageView);
  }

  @Override
  public int getItemCount() {
    return imageUris.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = (ImageView) itemView;
    }
  }
}
