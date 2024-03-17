package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
//import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DonationFragment extends Fragment implements DonationEventAdapter.CheckBoxListener {

    private ListView listView;
    private Button btnDonate, btnSelect, btnClear;
    private DonationEventAdapter donationEventAdapter;
    private List<Event> donationList;
    private DatabaseReference inventoryRef;
    private DatabaseReference donationRef;
    Map<String, LocalDateTime> latestTimestamps = new HashMap<>();
    private List<String> donationTimestampsAndIds = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        Toolbar toolbar = view.findViewById(R.id.new_toolbar);
        toolbar.setTitle("Donation Suggestion");

        return view;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview2);
        btnDonate = getView().findViewById(R.id.btnDonate);
        btnSelect = getView().findViewById(R.id.btnSelect);
        btnClear = getView().findViewById(R.id.btnClear);

        donationList = new ArrayList<>();
        donationEventAdapter = new DonationEventAdapter(getActivity(), R.layout.list_item1, donationList);
        donationEventAdapter.setCheckBoxListener(this);
        listView.setAdapter(donationEventAdapter);

        inventoryRef = FirebaseDatabase.getInstance().getReference().child("Inventory");
        donationRef = FirebaseDatabase.getInstance().getReference().child("ourtest");

        donationRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latestTimestamps.clear();
                donationTimestampsAndIds.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String timestampString = snapshot1.getKey();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime timestamp = LocalDateTime.parse(timestampString, formatter);

                    String idString = "";

                    if (snapshot1.child("id").getValue()!=null){
                        idString = snapshot1.child("id").getValue().toString();
                    }
                    else{
                        System.out.println("NULL idString");
                    }
                    latestTimestamps.put(idString, timestamp);
                    donationTimestampsAndIds.add(timestampString + "_" + idString);
                }
                System.out.println("latest Time stamps:" + latestTimestamps);

                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime twelveMonthsAgo = currentTime.minus(12, ChronoUnit.MONTHS);

                donationList.clear(); // Clear the list before repopulating

                for (Map.Entry<String, LocalDateTime> entry : latestTimestamps.entrySet()) {
                    String idString = entry.getKey();
                    LocalDateTime latestTimestamp = entry.getValue();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTimestamp = latestTimestamp.format(formatter);

                    if (latestTimestamp.isBefore(twelveMonthsAgo)) {
                        inventoryRef.child(idString).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() && snapshot.child("status").getValue(Integer.class) == 1) {
                                    String typeString = snapshot.child("Type").getValue(String.class);
                                    Event newEvent = new Event(idString, formattedTimestamp, typeString);
                                    donationList.add(newEvent);
                                    donationEventAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                }
                donationEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllCheckboxes(true);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllCheckboxes(false);
            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anyCheckboxSelected()) {
                    for (Event event : donationList) {
                        if (event.isSelected()) {
                            String itemId = event.getId();
                            // Update the status in the inventory reference
                            inventoryRef.child(itemId).child("status").setValue(0);
                            for (String entry : donationTimestampsAndIds) {
                                // Split each entry into timestamp and ID
                                String[] parts = entry.split("_");
                                if (parts.length == 2) {
                                    String dateString = parts[0];
                                    String idString = parts[1];
                                    if (idString.equals(itemId)) {
                                        // Update status to 0 for the matched ID and timestamp
                                        System.out.println("Updating status to 0 for: " + itemId + " " + dateString);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("ourtest")
                                                .child(dateString)
                                                .child("status")
                                                .setValue(0);
                                    }
                                }
                            }
                        }
                    }

                    // Notify the adapter after donation
                    donationEventAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Selected items have been donated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please select at least one item to donate.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onCheckBoxChanged(Event event, boolean isChecked) {
        event.setStatus(isChecked ? 1 : 0);
        event.setSelected(isChecked);
        donationEventAdapter.notifyDataSetChanged();
    }


    private void selectAllCheckboxes(boolean isSelected) {
        for (Event event : donationList) {
            event.setStatus(isSelected ? 1 : 0);
            event.setSelected(isSelected);
        }
        donationEventAdapter.notifyDataSetChanged();
    }

    private boolean anyCheckboxSelected() {
        for (Event event : donationList) {
            if (event.getStatus() == 1) {
                return true; // At least one checkbox is selected
            }
        }
        return false; // No checkbox is selected
    }
}