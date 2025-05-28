package com.example.roomie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.function.Consumer;

public class AuthenticateHouseAdapter extends RecyclerView.Adapter<AuthenticateHouseAdapter.ViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<HouseListing> listings;
    private final Consumer<HouseListing> onApprove;
    private final Consumer<HouseListing> onDisapprove;
    private final Consumer<HouseListing> onView;

    /**
     * @param context           adapter context
     * @param dbHelper          database helper
     * @param listings          list of listings to show
     * @param onView            action when "View" pressed (required)
     * @param onApprove         action when "Approve" pressed, or null to hide buttons
     * @param onDisapprove      action when "Disapprove" pressed, or null to hide buttons
     */
    public AuthenticateHouseAdapter(
            Context context,
            DatabaseHelper dbHelper,
            List<HouseListing> listings,
            Consumer<HouseListing> onView,
            Consumer<HouseListing> onApprove,
            Consumer<HouseListing> onDisapprove) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.listings = listings;
        this.onView = onView;
        this.onApprove = onApprove;
        this.onDisapprove = onDisapprove;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view, onApprove != null && onDisapprove != null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HouseListing listing = listings.get(position);
        holder.tvOwnerName.setText("Listing of " + listing.ownerName);

        // Listing image
        if (listing.house.photoUrls != null && !listing.house.photoUrls.isEmpty()) {
            Glide.with(holder.itemView)
                    .load(listing.house.photoUrls.get(0))
                    .centerCrop()
                    .into(holder.ivListingImage);
        } else {
            holder.ivListingImage.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // Owner avatar
        Glide.with(holder.itemView)
                .load(listing.ownerAvatarUrl)
                .circleCrop()
                .into(holder.ivOwnerProfile);

        // View detail
        holder.btnView.setOnClickListener(v -> onView.accept(listing));

        // Approve / Disapprove if provided
        if (onApprove != null && holder.btnApprove != null) {
            holder.btnApprove.setOnClickListener(v -> onApprove.accept(listing));
        }
        if (onDisapprove != null && holder.btnDisapprove != null) {
            holder.btnDisapprove.setOnClickListener(v -> onDisapprove.accept(listing));
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivListingImage, ivOwnerProfile;
        TextView tvOwnerName;
        Button btnView;
        Button btnApprove, btnDisapprove;

        ViewHolder(View itemView, boolean showButtons) {
            super(itemView);
            ivListingImage = itemView.findViewById(R.id.ivListingImage);
            ivOwnerProfile = itemView.findViewById(R.id.ivOwnerProfile);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            btnView = itemView.findViewById(R.id.btnView);

            if (showButtons) {
                ViewStub stub = itemView.findViewById(R.id.stubApproveButtons);
                View inflated = stub.inflate();
                btnApprove = inflated.findViewById(R.id.btnApprove);
                btnDisapprove = inflated.findViewById(R.id.btnDisapprove);
            } else {
                btnApprove = null;
                btnDisapprove = null;
            }
        }
    }
}
