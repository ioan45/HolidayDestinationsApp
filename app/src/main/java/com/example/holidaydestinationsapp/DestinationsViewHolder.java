package com.example.holidaydestinationsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class DestinationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView hotelImage;
    TextView nameHotel, addressHotel;
    double lat, lgt;

    public DestinationsViewHolder(@NonNull View itemView) {
        super(itemView);

        hotelImage = itemView.findViewById(R.id.hotelImageView);
        nameHotel = itemView.findViewById(R.id.hotelName);
        addressHotel = itemView.findViewById(R.id.hotelAddress);

        // Set click listener on item view
        itemView.setOnClickListener(this);
    }

    // Set lat and lgt values for the item
    public void setLatLgt(double lat, double lgt) {
        this.lat = lat;
        this.lgt = lgt;
    }

    @Override
    public void onClick(View view) {
        // Pass lat and lgt values to the mapFragment
//        Fragment mapFragment = new MapsFragment();
//        Bundle args = new Bundle();
//        args.putDouble("lat", lat);
//        args.putDouble("lgt", lgt);
//        mapFragment.setArguments(args);
//        ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.homeFrameLayout, mapFragment)
//                .commit();
    }
}

