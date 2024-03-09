package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ListOfUsageFragment extends Fragment {
    private ListView listView;

    private TextView noYearDateView, idView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_usage, container, false);
        Toolbar toolbar = view.findViewById(R.id.new_toolbar);
        toolbar.setTitle("Usage");
        return view;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview2);
        noYearDateView = getView().findViewById(R.id.txtNoYearDate);
        idView = getView().findViewById(R.id.txtId);

        final ArrayList<Event> list = new ArrayList();
        EventAdapter eventAdapter = new EventAdapter(getActivity(), R.layout.list_item, list);
        listView.setAdapter(eventAdapter);

        HashMap<String,String> typeIdMap = new HashMap<>();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference1.addValueEventListener(new ValueEventListener() {
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
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> latestTimestamps = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    latestTimestamps.add(snapshot1.getKey());
                }

                Collections.sort(latestTimestamps, Collections.reverseOrder()); //sort keys in reverse order

                list.clear(); // clear current list before repopulating

                for(String timestamp : latestTimestamps){
                    String idString = "";
                    DataSnapshot dateObj = snapshot.child(timestamp);
                    String dateString = timestamp;

                    if (dateObj.child("id").getValue() != null) {
                        idString = dateObj.child("id").getValue().toString();
                    } else {
                        System.out.println("null idString");
                    }

                    int newUse=0;
                    if(dateObj.child("newUse").getValue()!=null){
                        newUse = dateObj.child("newUse").getValue(Integer.class);
                    }else{
                        System.out.println("null newUse");
                    }

                    String typeString = typeIdMap.get(idString);
                    Event newEvent = new Event(idString, dateString, newUse, typeString);
                    newEvent.setType(typeString);
                    int status;
                    System.out.println("Event type:" + typeString);
                    //String txt = "Date:" + DateString + "\nId:"+IdString;
                    if(dateObj.child("status").getValue()!=null){
                        status = ((Long) dateObj.child("status").getValue()).intValue();
                        if(status==1){
                            list.add(newEvent);
                        }
                    }else{
                        System.out.println("no status available");
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Event usageEvent = list.get(i);
                 usageEvent.setNewUseFalse();
                 FirebaseDatabase.getInstance().getReference().child("ourtest").child(usageEvent.getDateTime()).child("newUse").setValue(0);



                 Intent intent = new Intent(getActivity(),UsageDetail.class);
                 intent.putExtra("DateTime",usageEvent.getDateTime());
                 intent.putExtra("Id",usageEvent.getId());
                 intent.putExtra("Type",usageEvent.getType());
                 /*if(usageEvent.getWeather()!=null){

                 }*/
                //intent.putExtra("Weather",usageEvent.getWeather());
                 startActivity(intent);

            }
        });






    }



}