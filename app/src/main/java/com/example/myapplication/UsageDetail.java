package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class UsageDetail extends AppCompatActivity {
    private Button btnweather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_detail);
        btnweather = findViewById(R.id.btnWeather);

        TextView detailView = findViewById(R.id.UsageDetail);

        Bundle bundle = getIntent().getExtras();
        detailView.setText(bundle.getString("detail"));
    }
}