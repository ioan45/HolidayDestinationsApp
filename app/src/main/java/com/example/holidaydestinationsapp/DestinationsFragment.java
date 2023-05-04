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
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));
        itemDestinationList.add(new ItemDestination("test 1", "address 1", R.drawable.blank_profile_picture));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new DestinationsAdapter(this.getActivity().getApplicationContext(), itemDestinationList));

        return rootView;
    }
}