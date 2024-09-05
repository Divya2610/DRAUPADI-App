package com.example.draupadi;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class emergency_phone_nos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone_nos); // Replace with your layout file name

        // Find buttons by their IDs
        Button btnNationalHelpline = findViewById(R.id.btn_national_helpline);
        Button btnPolice = findViewById(R.id.btn_police);
        Button btnWomenHelpline = findViewById(R.id.btn_women_helpline);
        Button btnChildHelpline = findViewById(R.id.btn_child_helpline);

        // Set click listeners for each button
        btnNationalHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber("112");
            }
        });

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber("100");
            }
        });

        btnWomenHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber("1091");
            }
        });

        btnChildHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber("1098");
            }
        });
    }

    // Helper function to initiate a phone call
    private void dialNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
