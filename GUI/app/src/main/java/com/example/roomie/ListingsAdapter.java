// File: com/example/roomie/ListingsAdapter.java
package com.example.roomie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.VH> {

  public interface OnItemClickListener {
    void onItemClick(HouseListing listing);
  }

  private final List<HouseListing> listings;
  private final OnItemClickListener listener;

  public ListingsAdapter(List<HouseListing> listings, OnItemClickListener listener) {
    this.listings = listings;
    this.listener = listener;
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_house_card, parent, false);
    return new VH(v);
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    HouseListing listing = listings.get(position);
    // pull address & rent off the inner House
    holder.tvAddr.setText(listing.house.address);
    holder.tvRent.setText("€" + (int) listing.house.rent + "/month");
    // show the listing-level ownerName
    holder.tvOwner.setText(listing.ownerName);

    holder.itemView.setOnClickListener(v -> listener.onItemClick(listing));
  }

  @Override
  public int getItemCount() {
    return listings.size();
  }

  static class VH extends RecyclerView.ViewHolder {
    final TextView tvAddr, tvRent, tvOwner;

    VH(@NonNull View itemView) {
      super(itemView);
      tvAddr = itemView.findViewById(R.id.tvItemAddress);
      tvRent = itemView.findViewById(R.id.tvItemRent);
      tvOwner = itemView.findViewById(R.id.tvItemOwner);
    }
  }
}
