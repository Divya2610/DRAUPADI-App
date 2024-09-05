package com.example.draupadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class report_page extends AppCompatActivity {

    private EditText etLocation, etDescription;
    private GridLayout gridLayoutIssues;
    private Button btnSubmit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page); // Ensure this matches your layout file name

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");

        // Find views by their IDs
        etLocation = findViewById(R.id.et_location);
        etDescription = findViewById(R.id.et_description);
        gridLayoutIssues = findViewById(R.id.gridLayoutIssues);
        btnSubmit = findViewById(R.id.btn_submit);

        // Set onClickListener for the submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }

    private void submitReport() {
        // Get input values
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Collect selected issues
        StringBuilder issues = new StringBuilder();
        for (int i = 0; i < gridLayoutIssues.getChildCount(); i++) {
            View view = gridLayoutIssues.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    issues.append(checkBox.getText().toString()).append(", ");
                }
            }
        }
        // Remove trailing comma
        if (issues.length() > 0) {
            issues.setLength(issues.length() - 2);
        }

        // Create a Report object
        Report report = new Report(location, issues.toString(), description);

        // Push the report data to Firebase
        databaseReference.push().setValue(report).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Show success message
                Toast.makeText(report_page.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();

                // Redirect to the home page
                startActivity(new Intent(report_page.this, MainActivity.class));
                finish(); // Close this activity
            } else {
                // Show failure message
                Toast.makeText(report_page.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Report class
    public static class Report {
        public String location;
        public String issues;
        public String description;

        public Report() {
            // Default constructor required for calls to DataSnapshot.getValue(Report.class)
        }

        public Report(String location, String issues, String description) {
            this.location = location;
            this.issues = issues;
            this.description = description;
        }
    }
}
