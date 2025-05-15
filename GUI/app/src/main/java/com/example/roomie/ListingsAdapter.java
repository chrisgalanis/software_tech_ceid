package com.example.roomie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListingsAdapter
        extends RecyclerView.Adapter<ListingsAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(House house);
    }

    private final List<House> houses;
    private final OnItemClickListener listener;

    public ListingsAdapter(List<House> houses,
                           OnItemClickListener listener) {
        this.houses   = houses;
        this.listener = listener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_house_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        House house = houses.get(position);
        holder.tvAddr.setText(house.address);
        holder.tvRent.setText("€" + (int)house.rent + "/month");

        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(house)
        );
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvAddr, tvRent;
        VH(@NonNull View itemView) {
            super(itemView);
            tvAddr = itemView.findViewById(R.id.tvItemAddress);
            tvRent = itemView.findViewById(R.id.tvItemRent);
        }
    }
}
