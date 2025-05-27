// File: com/example/roomie/LikedHousesAdapter.java
package com.example.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LikedHousesAdapter extends RecyclerView.Adapter<LikedHousesAdapter.ViewHolder> {

  public interface OnHouseClickListener {
    void onHouseClick(HouseListing listing);
  }

  private Context context;
  private List<HouseListing> listingList;
  private OnHouseClickListener listener;

  public LikedHousesAdapter(Context context,
                            List<HouseListing> listingList,
                            OnHouseClickListener listener) {
    this.context     = context;
    this.listingList = listingList;
    this.listener    = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
            .from(context)
            .inflate(R.layout.item_house_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    HouseListing listing = listingList.get(position);

    // now pulling from HouseListing instead of raw House
    holder.houseTitle.setText("Live with " + listing.ownerName);
    holder.housePrice.setText("€" + listing.house.rent + "/month");

    holder.itemView.setOnClickListener(v -> {
      if (listener != null) {
        listener.onHouseClick(listing);
      }
    });
  }

  @Override
  public int getItemCount() {
    return listingList.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView houseTitle, housePrice;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      houseTitle = itemView.findViewById(R.id.tvItemAddress);
      housePrice = itemView.findViewById(R.id.tvItemRent);
    }
  }
}
