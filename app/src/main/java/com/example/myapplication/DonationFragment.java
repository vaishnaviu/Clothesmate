package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonationFragment extends Fragment {
    private ListView listView;
    private TextView dateView, idView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donation, container, false);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview2);

        final ArrayList<Event> donationList = new ArrayList<>();
        DonationEventAdapter donationEventAdapter = new DonationEventAdapter(getActivity(), R.layout.list_item1, donationList);
        listView.setAdapter(donationEventAdapter);

        DatabaseReference inventoryRef = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference donationRef = FirebaseDatabase.getInstance().getReference().child("ourtest");

        donationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, LocalDateTime> latestTimestamps = new HashMap<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String timestampString = snapshot1.getKey();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime timestamp = LocalDateTime.parse(timestampString, formatter);

                    String idString = snapshot1.child("id").getValue().toString();
                    latestTimestamps.put(idString, timestamp);
                }

                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime twelveMonthsAgo = currentTime.minus(12, ChronoUnit.MONTHS);

                for (Map.Entry<String, LocalDateTime> entry : latestTimestamps.entrySet()) {
                    String idString = entry.getKey();
                    LocalDateTime latestTimestamp = entry.getValue();

                    if (latestTimestamp.isBefore(twelveMonthsAgo)) {
                        inventoryRef.child(idString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String typeString = snapshot.child("Type").getValue(String.class);
                                    Event newEvent = new Event(idString, latestTimestamp.toString(), typeString);
                                    donationList.add(newEvent);
                                    donationEventAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                donationEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}