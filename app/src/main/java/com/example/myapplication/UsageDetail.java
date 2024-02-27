package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsageDetail extends AppCompatActivity {
    private Button btnweather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_detail);
        btnweather = findViewById(R.id.btnWeather);

        TextView dateView = findViewById(R.id.DateTimeDetail);
        TextView idView = findViewById(R.id.IdDetail);
        TextView weatherView = findViewById(R.id.WeatherDetail);
        EditText weatherEdit = findViewById(R.id.WeatherEdit);


        Bundle bundle = getIntent().getExtras();
        dateView.setText(bundle.getString("DateTime"));
        idView.setText(bundle.getString("Id"));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(bundle.getString("DateTime")).child("Weather").getValue()!=null){
                    String result = ((String) snapshot.child(bundle.getString("DateTime")).child("Weather").getValue()).toString();
                    System.out.print(result);
                    weatherView.setText(result);
                };


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userWeatherInput = weatherEdit.getText().toString();
                System.out.println(userWeatherInput);
                FirebaseDatabase.getInstance().getReference().child("ourtest").child(bundle.getString("DateTime")).child("Weather").setValue(userWeatherInput);
            }
        });




    }
}