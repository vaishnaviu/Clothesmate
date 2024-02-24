package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfClothesFragment extends Fragment {



    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_clothes, container, false);
        return view;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    //public void onViewCreated(View view,Bundle savedInstanceState) {
        //super.onViewCreated(view,savedInstanceState);
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.listview);




        /*write to database*/
        //FirebaseDatabase.getInstance().getReference().child("Information").child("1").setValue("1");
        //FirebaseDatabase.getInstance().getReference().child("Information").child("2").setValue("2");

        /*write to database - for a specific object add new schema*/
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("1").child("color").setValue("yellow");
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("2").child("type").setValue("pants");
        //FirebaseDatabase.getInstance().getReference().child("AddSchema").child("2").child("color").setValue("red");
        //FirebaseDatabase.getInstance().getReference().child("Information").child("2").setValue("2");

        /*display*/

        final ArrayList<String> list = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item1, list);
        listView.setAdapter(adapter);

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ourtest");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                System.out.println("test1");
                System.out.println(snapshot.getChildren());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //Information info = snapshot1.getValue(Information.class);
                    //String txt = "Id:"+info.getId()+"Freq:"+info.getFrequency();
                    String IdString = snapshot1.child("Color").getValue().toString();
                    String DateString = snapshot1.child("Type").getValue().toString();
                    String txt = " Object:" + snapshot1.getKey() + " Color:" + IdString + " Type:" + DateString;
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notification();
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
        });

    }


    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        resultIntent.putExtra("fragment_to_load","ListOfUsageFragment");
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
    }
}