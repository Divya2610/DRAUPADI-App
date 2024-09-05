package com.example.draupadi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class add_friend extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST = 1001;
    private EditText phoneEditText;
    private CheckBox sosCheckBox;
    private Button addSosContactButton;
    private TextView selectedContactName;
    private LinearLayout contactsLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        phoneEditText = findViewById(R.id.input_phone_number);
        sosCheckBox = findViewById(R.id.sos_checkbox);
        addSosContactButton = findViewById(R.id.add_sos_contact_button);
        selectedContactName = findViewById(R.id.selectedContactName);
        contactsLayout = findViewById(R.id.contact_list_layout); // Ensure ID matches XML

        ImageView contactEmoji = findViewById(R.id.contact_emoji);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("contacts");

        // Handle contact picker
        contactEmoji.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PICKER_REQUEST);
            } else {
                openContactPicker();
            }
        });

        // Save contact in Firebase
        addSosContactButton.setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();
            String name = selectedContactName.getText().toString().trim();
            boolean isSos = sosCheckBox.isChecked();

            if (!phoneNumber.isEmpty() && !name.isEmpty()) {
                // Save contact to Firebase
                String contactId = databaseReference.push().getKey();
                Contact contact = new Contact(contactId, name, phoneNumber, isSos);
                if (contactId != null) {
                    databaseReference.child(contactId).setValue(contact).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(add_friend.this, "Contact Saved!", Toast.LENGTH_SHORT).show();
                            retrieveContacts();  // Update frontend with newly added contact
                        } else {
                            Toast.makeText(add_friend.this, "Failed to save contact.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(add_friend.this, "Phone Number or Name is empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Retrieve contacts from Firebase on page load
        retrieveContacts();
    }

    // Open the contact picker
    private void openContactPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_REQUEST);
    }

    // Handle the result from the contact picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String contactName = cursor.getString(nameIndex);
                String contactNumber = cursor.getString(numberIndex);

                // Display the contact in EditText and TextView
                phoneEditText.setText(contactNumber);
                selectedContactName.setText(contactName);
                selectedContactName.setVisibility(View.VISIBLE); // Make visible
                cursor.close();
            }
        }
    }

    // Retrieve stored contacts from Firebase and display them in the frontend
    private void retrieveContacts() {
        // Clear existing views in the layout before adding new ones
        contactsLayout.removeAllViews();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    if (contact != null) {
                        View contactView = getLayoutInflater().inflate(R.layout.contact_item, null);
                        TextView contactNameView = contactView.findViewById(R.id.contactName);
                        TextView contactNumberView = contactView.findViewById(R.id.contactNumber);

                        contactNameView.setText(contact.getName());
                        contactNumberView.setText(contact.getPhone());

                        // Add this contact view to the layout
                        contactsLayout.addView(contactView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(add_friend.this, "Failed to load contacts.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
