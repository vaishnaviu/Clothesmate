package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfUsageFragment extends Fragment {
    private ListView listView;

    private TextView dateView, idView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_usage, container, false);
        return view;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview2);
        dateView = getView().findViewById(R.id.txtDate);
        idView = getView().findViewById(R.id.txtId);



        final ArrayList<Event> list = new ArrayList();
        EventAdapter eventAdapter = new EventAdapter(getActivity(), R.layout.list_item, list);
        listView.setAdapter(eventAdapter);

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ourtest");
        //DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                System.out.println("test1");
                System.out.println(snapshot.getChildren());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //Information info = snapshot1.getValue(Information.class);
                    //String txt = "Id:"+info.getId()+"Freq:"+info.getFrequency();
                    //String IdString = snapshot1.child("Id").getValue().toString();
                    //String DateString = snapshot1.child("Occasion").getValue().toString();
                    //String txt = " Object:" + snapshot1.getKey() + " Id:" + IdString + " Occasion:" + DateString;
                    String dateString = snapshot1.getKey();
                    String idString = snapshot1.child("id").getValue().toString();
                    Event newEvent = new Event(idString,dateString);
                    //String txt = "Date:" + DateString + "\nId:"+IdString;
                    list.add(newEvent);
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
                 Intent intent = new Intent(getActivity(),UsageDetail.class);
                 intent.putExtra("DateTime",usageEvent.getDateTime());
                 intent.putExtra("Id",usageEvent.getId());
                 /*if(usageEvent.getWeather()!=null){

                 }*/
                //intent.putExtra("Weather",usageEvent.getWeather());
                 startActivity(intent);

            }
        });






    }



}