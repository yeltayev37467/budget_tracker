package com.iitu_yv.budgettracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ExpensesActivity extends AppCompatActivity {

    private EditText inputExpenseName;
    private EditText inputExpenseAmount;
    private Spinner categorySpinner;
    private Button addExpenseButton;
    private TextView expenseSummary;

    private ArrayList<String> expenseList;
    private HashMap<String, Double> categoryMap;

    private static final String EXPENSES_FILE_NAME = "expenses.csv";
    private static final String CATEGORIES_FILE_NAME = "categories.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        inputExpenseName = findViewById(R.id.inputExpenseName);
        inputExpenseAmount = findViewById(R.id.inputExpenseAmount);
        categorySpinner = findViewById(R.id.categorySpinner);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        expenseSummary = findViewById(R.id.expenseSummary);

        expenseList = new ArrayList<>();
        categoryMap = new HashMap<>();

        // Load categories and expenses
        loadCategoriesFromFile();
        loadExpensesFromFile();

        // Populate category spinner
        setupCategorySpinner();

        // Update expense summary
        updateExpenseSummary();

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputExpenseName.getText().toString();
                String amountText = inputExpenseAmount.getText().toString();
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                if (!name.isEmpty() && !amountText.isEmpty() && !selectedCategory.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountText);
                        String expenseEntry = name + " - " + amount + " ₸ " + " (" + selectedCategory + ")";
                        expenseList.add(expenseEntry);
                        saveExpensesToFile();
                        updateExpenseSummary();
                        inputExpenseName.setText("");
                        inputExpenseAmount.setText("");
                    } catch (NumberFormatException e) {
                        inputExpenseAmount.setError("Please enter a valid amount");
                    }
                }
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayList<String> categories = new ArrayList<>(categoryMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void updateExpenseSummary() {
        StringBuilder summary = new StringBuilder();
        for (String expense : expenseList) {
            summary.append(expense).append("\n");
        }
        expenseSummary.setText(summary.toString());
    }

    private void saveExpensesToFile() {
        File file = new File(getFilesDir(), EXPENSES_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            for (String expense : expenseList) {
                writer.write(expense + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExpensesFromFile() {
        File file = new File(getFilesDir(), EXPENSES_FILE_NAME);
        if (!file.exists()) {
            return; // No file exists, nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenseList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCategoriesFromFile() {
        File file = new File(getFilesDir(), CATEGORIES_FILE_NAME);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
