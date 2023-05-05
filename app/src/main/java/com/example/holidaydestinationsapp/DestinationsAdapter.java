package com.example.holidaydestinationsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsViewHolder> {

    Context context;
    List<ItemDestination> itemDestinationList;

    public DestinationsAdapter(Context context, List<ItemDestination> itemDestinationList) {
        this.context = context;
        this.itemDestinationList = itemDestinationList;
    }

    @NonNull
    @Override
    public DestinationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DestinationsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_destination_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationsViewHolder holder, int position) {
        holder.nameHotel.setText(itemDestinationList.get(position).getHotelName());
        holder.addressHotel.setText(itemDestinationList.get(position).getHotelAddress());
        holder.hotelImage.setImageResource(itemDestinationList.get(position).getImageHotel());

        // Set lat and lgt values for the item
        holder.setLatLgt(itemDestinationList.get(position).getLat(), itemDestinationList.get(position).getLgt());
    }


    @Override
    public int getItemCount() {
        return itemDestinationList.size();
    }
}
