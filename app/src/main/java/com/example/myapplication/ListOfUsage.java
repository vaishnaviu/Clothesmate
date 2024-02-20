package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListOfUsage extends AppCompatActivity {
    private ListView listView;
    private Button inventory;
    private Button usage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_usage);
        inventory = findViewById(R.id.Inventory);
        usage = findViewById(R.id.usage);
        listView = findViewById(R.id.listview2);

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInventoryActivity();
            }
        });


        final ArrayList<String> list = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

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
                    String DateString = snapshot1.getKey();
                    String IdString = snapshot1.child("id").getValue().toString();
                    String txt = "Date:" + DateString + "\nId:"+IdString;
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 String string = list.get(i);
                 Intent intent = new Intent(ListOfUsage.this,UsageDetail.class);
                 intent.putExtra("detail",string);
                 startActivity(intent);

            }
        });






    }

    private void openInventoryActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}