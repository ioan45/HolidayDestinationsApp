package com.example.holidaydestinationsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DestinationsFragment extends Fragment {
    public DestinationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_destinations, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewDestinations);

        List<ItemDestination> itemDestinationList = new ArrayList<ItemDestination>();
        itemDestinationList.add(new ItemDestination("The Ritz-Carlton", "New York City - 50 Central Park South, New York, NY 10019", R.drawable.blank_profile_picture, 100, 100));
        itemDestinationList.add(new ItemDestination("Burj Al Arab Jumeirah", "Dubai - Jumeira Road, Umm Suqeim 3, Dubai, United Arab Emirates", R.drawable.blank_profile_picture, 200, 250));
        itemDestinationList.add(new ItemDestination("Belmond Hotel Caruso", "Ravello, Italy - Piazza San Giovanni del Toro, 2, 84010 Ravello SA, Italy", R.drawable.blank_profile_picture, 300, 900));
        itemDestinationList.add(new ItemDestination("Aman", "Tokyo, Japan - 1-5-6 Otemachi, Chiyoda-ku, Tokyo 100-0004, Japan", R.drawable.blank_profile_picture, 400, 800));
        itemDestinationList.add(new ItemDestination("Atlantis, The Palm", "Dubai - Crescent Rd - Dubai - United Arab Emirates", R.drawable.blank_profile_picture, 500, 700));
        itemDestinationList.add(new ItemDestination("Rosewood", "London, United Kingdom - 252 High Holborn, Holborn, London WC1V 7EN, United Kingdom", R.drawable.blank_profile_picture, 600, 600));
        itemDestinationList.add(new ItemDestination("Hotel Plaza Athénée,", " Paris, France - 25 Avenue Montaigne, 75008 Paris, France", R.drawable.blank_profile_picture, 700, 500));
        itemDestinationList.add(new ItemDestination("The Peninsula", "Chicago, United States - 108 E Superior St, Chicago, IL 60611, United States", R.drawable.blank_profile_picture, 800, 400));
        itemDestinationList.add(new ItemDestination("Mandarin Oriental", "Bangkok, Thailand - 48 Oriental Avenue, Bangkok 10500, Thailand", R.drawable.blank_profile_picture, 900, 300));
        itemDestinationList.add(new ItemDestination("The St. Regis Bali Resort", " Indonesia - Kawasan Pariwisata, Nusa Dua, Lot S6, PO Box 44, Nusa Dua, Bali, 80363, Indonesia", R.drawable.blank_profile_picture, 100, 200));


        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new DestinationsAdapter(this.getActivity().getApplicationContext(), itemDestinationList));

        return rootView;
    }
}