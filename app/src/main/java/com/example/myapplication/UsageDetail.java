package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    String[] weatherItems = {"Sunny","Rainy","Windy","Cloudy","Snowy"};
    String[] occasionItems = {"Formal","Casual","Semi-formal"};

    AutoCompleteTextView autoCompleteTextViewWeather, autoCompleteTextViewOccasion;
    ArrayAdapter<String> adapterItemsWeather;
    ArrayAdapter<String> adapterItemsOccasion;
    String userWeatherInput;
    String userOccasionInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_detail);
        btnweather = findViewById(R.id.btnWeather);

        Toolbar toolbar = findViewById(R.id.new_toolbar);
        toolbar.setTitle("Usage Details");

        TextView dateView = findViewById(R.id.DateTimeDetail);
        TextView idView = findViewById(R.id.IdDetail);
        TextView weatherView = findViewById(R.id.WeatherDetail);
        TextView occasionView = findViewById(R.id.OccasionDetail);

        autoCompleteTextViewWeather = findViewById(R.id.auto_complete_weathertxt);
        autoCompleteTextViewOccasion = findViewById(R.id.auto_complete_occasiontxt);

        adapterItemsWeather = new ArrayAdapter<String>(this, R.layout.custom_dropdown_item_layout,weatherItems);
        adapterItemsOccasion = new ArrayAdapter<String>(this,R.layout.custom_dropdown_item_layout,occasionItems);

        autoCompleteTextViewWeather.setAdapter(adapterItemsWeather);
        autoCompleteTextViewOccasion.setAdapter(adapterItemsOccasion);

        Bundle bundle = getIntent().getExtras();
        dateView.setText(bundle.getString("DateTime"));
        idView.setText(bundle.getString("Id"));



        autoCompleteTextViewWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userWeatherInput = adapterView.getItemAtPosition(i).toString();


            }
        });

        autoCompleteTextViewOccasion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userOccasionInput = adapterView.getItemAtPosition(i).toString();


            }
        });




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ourtest");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(bundle.getString("DateTime")).child("weather").getValue()!=null){
                    String result = ((String) snapshot.child(bundle.getString("DateTime")).child("weather").getValue()).toString();
                    System.out.print(result);
                    weatherView.setText(result);
                };

                if(snapshot.child(bundle.getString("DateTime")).child("occasion").getValue()!=null){
                    String result = ((String) snapshot.child(bundle.getString("DateTime")).child("occasion").getValue()).toString();
                    System.out.print(result);
                    occasionView.setText(result);
                };


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ourtest").child(bundle.getString("DateTime")).child("weather").setValue(userWeatherInput);
                System.out.println(userWeatherInput);
                FirebaseDatabase.getInstance().getReference().child("ourtest").child(bundle.getString("DateTime")).child("occasion").setValue(userOccasionInput);
                System.out.println(userOccasionInput);
            }
        });




    }
}