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
        toolbar.setTitle("My Clothes");
        return view;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    //public void onViewCreated(View view,Bundle savedInstanceState) {
        //super.onViewCreated(view,savedInstanceState);
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview);
        typeView = getView().findViewById(R.id.txtTypeClothes);
        idView = getView().findViewById(R.id.txtIdClothes);





        /*write to database*/
        //FirebaseDatabase.getInstance().getReference().child("Information").child("1").setValue("1");
        //FirebaseDatabase.getInstance().getReference().child("Information").child("2").setValue("2");

        /*write to database - for a specific object add new schema*/
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("1").child("color").setValue("yellow");
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("2").child("type").setValue("pants");
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("2").child("color").setValue("red");
        //FirebaseDatabase.getInstance().getReference().child("Information").child("2").setValue("2");

        /*display*/

        final ArrayList<Clothes> list = new ArrayList();
        ClothesAdapter clothesAdapter = new ClothesAdapter(getActivity(), R.layout.list_itemclothes, list);
        listView.setAdapter(clothesAdapter);

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ourtest");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                //System.out.println("test1");
                System.out.println(snapshot.getChildren());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //Information info = snapshot1.getValue(Information.class);
                    //String txt = "Id:"+info.getId()+"Freq:"+info.getFrequency();
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

        /*reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notification();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
    //As we have implemented the newUse function, no notificaiton is needed.

   /* private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            if(isAdded()){
                NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

        }
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        resultIntent.putExtra("","ListOfUsageFragment");
        //PendingIntent resultPendingIntent = PendingIntent.getActivities(this,1, new Intent[]{resultIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(),1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "n")
                .setContentText("Code Sphere")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentText("New usage data is identified");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(999, builder.build());
    }*/
}