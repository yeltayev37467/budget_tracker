package com.iitu_yv.budgettracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class CategoriesActivity extends AppCompatActivity {

    private EditText inputCategoryName;
    private EditText inputMonthlyBudget;
    private Button addCategoryButton;
    private TextView categorySummary;

    private HashMap<String, Double> categoryMap;
    private static final String FILE_NAME = "categories.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        inputCategoryName = findViewById(R.id.inputCategoryName);
        inputMonthlyBudget = findViewById(R.id.inputMonthlyBudget);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        categorySummary = findViewById(R.id.categorySummary);

        categoryMap = new HashMap<>();

        // Load categories from the CSV file
        loadCategoriesFromFile();

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = inputCategoryName.getText().toString();
                String budgetText = inputMonthlyBudget.getText().toString();
                if (!categoryName.isEmpty() && !budgetText.isEmpty()) {
                    try {
                        double budget = Double.parseDouble(budgetText);
                        categoryMap.put(categoryName, budget);
                        saveCategoriesToFile(); // Save the updated categories
                        updateCategorySummary();
                        inputCategoryName.setText("");
                        inputMonthlyBudget.setText("");
                    } catch (NumberFormatException e) {
                        inputMonthlyBudget.setError("Please enter a valid number");
                    }
                }
            }
        });
    }

    private void updateCategorySummary() {
        StringBuilder summary = new StringBuilder();
        for (String category : categoryMap.keySet()) {
            summary.append(category).append(" - ₸").append(categoryMap.get(category)).append("\n");
        }
        categorySummary.setText(summary.toString());
    }

    private void saveCategoriesToFile() {
        File file = new File(getFilesDir(), FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            for (String category : categoryMap.keySet()) {
                writer.write(category + "," + categoryMap.get(category) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCategoriesFromFile() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return; // No file exists, nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String categoryName = parts[0];
                    double budget = Double.parseDouble(parts[1]);
                    categoryMap.put(categoryName, budget);
                }
            }
            updateCategorySummary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
