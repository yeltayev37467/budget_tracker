package com.iitu_yv.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText inputProfileName;
    private Button createProfileButton;
    private LinearLayout existingProfilesLayout;

    private static final String PROFILES_FILE_NAME = "profiles.csv";
    private ArrayList<String> profilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputProfileName = findViewById(R.id.inputProfileName);
        createProfileButton = findViewById(R.id.createProfileButton);
        existingProfilesLayout = findViewById(R.id.existingProfilesLayout);

        profilesList = new ArrayList<>();

        // Load existing profiles from the file
        loadProfilesFromFile();

        // Dynamically create buttons for existing profiles
        displayExistingProfiles();

        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileName = inputProfileName.getText().toString();
                if (!profileName.isEmpty()) {
                    if (!profilesList.contains(profileName)) {
                        profilesList.add(profileName);
                        saveProfilesToFile(); // Save the new profile
                        addProfileButton(profileName); // Add a button dynamically
                        inputProfileName.setText("");
                    } else {
                        inputProfileName.setError("Profile already exists!");
                    }
                } else {
                    inputProfileName.setError("Name cannot be empty!");
                }
            }
        });
    }

    private void loadProfilesFromFile() {
        File file = new File(getFilesDir(), PROFILES_FILE_NAME);
        if (!file.exists()) {
            return; // No file exists, nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                profilesList.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProfilesToFile() {
        File file = new File(getFilesDir(), PROFILES_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            for (String profile : profilesList) {
                writer.write(profile + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayExistingProfiles() {
        for (String profileName : profilesList) {
            addProfileButton(profileName);
        }
    }

    private void addProfileButton(String profileName) {
        Button profileButton = new Button(this);
        profileButton.setText("Enter as " + profileName);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("profileName", profileName);
                startActivity(intent);
            }
        });

        // Add the button to the layout
        existingProfilesLayout.addView(profileButton);
    }
}
