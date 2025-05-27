package com.example.roomie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class AuthenticateHouseAdapter
        extends RecyclerView.Adapter<AuthenticateHouseAdapter.ViewHolder> {

    public interface OnActionListener {
        void onApprove(HouseListing listing);
        void onDisapprove(HouseListing listing);
        void onViewDetail(HouseListing listing);
    }

    private final List<HouseListing> listings;
    private final OnActionListener listener;

    public AuthenticateHouseAdapter(List<HouseListing> listings,
                                    OnActionListener listener) {
        this.listings = listings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        HouseListing listing = listings.get(position);
        // Owner name
        h.tvOwnerName.setText("Listing of " + listing.ownerName);

        // Listing image (first photo from the House)
        if (listing.house.photoUrls != null && !listing.house.photoUrls.isEmpty()) {
            Glide.with(h.itemView)
                    .load(listing.house.photoUrls.get(0))
                    .centerCrop()
                    .into(h.ivListingImage);
        } else {
            h.ivListingImage.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // Owner avatar
        Glide.with(h.itemView)
                .load(listing.ownerAvatarUrl)
                .circleCrop()
                .into(h.ivOwnerProfile);

        h.btnApprove.setOnClickListener(v -> listener.onApprove(listing));
        h.btnDisapprove.setOnClickListener(v -> listener.onDisapprove(listing));
        h.btnView.setOnClickListener(v -> listener.onViewDetail(listing));
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivListingImage, ivOwnerProfile;
        TextView  tvOwnerName;
        Button    btnApprove, btnDisapprove, btnView;

        ViewHolder(View root) {
            super(root);
            ivListingImage = root.findViewById(R.id.ivListingImage);
            ivOwnerProfile = root.findViewById(R.id.ivOwnerProfile);
            tvOwnerName    = root.findViewById(R.id.tvOwnerName);
            btnApprove     = root.findViewById(R.id.btnApprove);
            btnDisapprove  = root.findViewById(R.id.btnDisapprove);
            btnView        = root.findViewById(R.id.btnView);
        }
    }
}
