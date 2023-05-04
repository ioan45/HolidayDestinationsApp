package com.example.holidaydestinationsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DestinationsViewHolder extends RecyclerView.ViewHolder {

    ImageView hotelImage;
    TextView nameHotel, addressHotel;

    public DestinationsViewHolder(@NonNull View itemView) {
        super(itemView);

        hotelImage = itemView.findViewById(R.id.hotelImageView);
        nameHotel = itemView.findViewById(R.id.hotelName);
        addressHotel = itemView.findViewById(R.id.hotelAddress);
    }
}
