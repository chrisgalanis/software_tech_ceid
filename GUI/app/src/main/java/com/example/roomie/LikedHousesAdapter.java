

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
        void onHouseClick(House house);
    }

    private Context context;
    private List<House> houseList;
    private OnHouseClickListener listener;

    public LikedHousesAdapter(Context context, List<House> houseList, OnHouseClickListener listener) {
        this.context = context;
        this.houseList = houseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        House house = houseList.get(position);
        holder.houseTitle.setText("Live with " + house.ownerName);
        holder.housePrice.setText("€" + house.rent + "/month");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHouseClick(house);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView houseTitle, housePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            houseTitle = itemView.findViewById(R.id.tvItemAddress);
            housePrice = itemView.findViewById(R.id.tvItemRent);
        }
    }
}
