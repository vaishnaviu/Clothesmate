package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfClothesFragment extends Fragment {



    private ListView listView;

    private TextView typeView, idView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_clothes, container, false);
        Toolbar toolbar = view.findViewById(R.id.new_toolbar);
        toolbar.setTitle("My Closet");
        return view;
    }
    @SuppressLint("MissingInflatedId")
    @Override

    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview);
        typeView = getView().findViewById(R.id.txtTypeClothes);
        idView = getView().findViewById(R.id.txtIdClothes);


        final ArrayList<Clothes> list = new ArrayList();
        ClothesAdapter clothesAdapter = new ClothesAdapter(getActivity(), R.layout.list_itemclothes, list);
        listView.setAdapter(clothesAdapter);

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                System.out.println(snapshot.getChildren());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String idString = snapshot1.getKey().toString();
                    String typeString = snapshot1.child("Type").getValue().toString();
                    int status;
                    Clothes newClothes = new Clothes(idString,typeString);
                    if(snapshot1.child("status").getValue()!=null){
                        status = ((Long) snapshot1.child("status").getValue()).intValue();
                        newClothes.setStatus(status);
                        if(status==1){
                            list.add(newClothes);
                        }
                    }
                }
                clothesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}