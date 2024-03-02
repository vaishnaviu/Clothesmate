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
        dateView = getView().findViewById(R.id.txtDate);
        idView = getView().findViewById(R.id.txtId);

        final ArrayList<Event> donationList = new ArrayList();
        DonationEventAdapter donationEventAdapter = new DonationEventAdapter(getActivity(), R.layout.list_item1, donationList);
        listView.setAdapter(donationEventAdapter);

        HashMap<String,String> typeIdMap = new HashMap<>();

        DatabaseReference inventoryRef = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference donationRef = FirebaseDatabase.getInstance().getReference().child("ourtest");
        inventoryRef.addValueEventListener(new ValueEventListener() {
        @Override

        public void onDataChange(@NonNull DataSnapshot snapshot) {
            typeIdMap.clear();
            for(DataSnapshot snapshot1:snapshot.getChildren()){
                String idString = snapshot1.getKey().toString();
                String typeString = snapshot1.child("Type").getValue().toString();
                typeIdMap.put(idString,typeString);
            }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        donationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear();
//                System.out.println("test1");
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String dateTimeString = snapshot1.getKey();
                    System.out.println("date time string" + dateTimeString);

                    String[] dateSplit = dateTimeString.split(" ");
                    String dateString = dateSplit[0];
                    System.out.println("Date: " + dateString);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime timestamp = LocalDateTime.parse(dateTimeString, formatter);

                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime sixMonthsAgo = currentTime.minus(6, ChronoUnit.MONTHS);

                    if (timestamp.isBefore(sixMonthsAgo)) {
                        String idString = snapshot1.child("id").getValue().toString();

                        inventoryRef.child(idString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String typeString = snapshot.child("Type").getValue(String.class);
                                    Event newEvent = new Event(idString,dateString, typeString);
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