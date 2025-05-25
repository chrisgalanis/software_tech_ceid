// com/example/roomie/ImageSliderAdapter.java
package com.example.roomie;

import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageSliderAdapter
        extends RecyclerView.Adapter<ImageSliderAdapter.VH> {

    private final List<String> urls;
    public ImageSliderAdapter(List<String> urls) { this.urls = urls; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        ImageView iv = new ImageView(p.getContext());
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new VH(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Glide.with(h.iv).load(urls.get(pos)).into(h.iv);
    }

    @Override public int getItemCount() { return urls.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView iv;
        VH(@NonNull View v){ super(v); iv=(ImageView)v; }
    }
}
