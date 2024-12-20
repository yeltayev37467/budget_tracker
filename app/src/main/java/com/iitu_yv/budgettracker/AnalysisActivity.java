package com.iitu_yv.budgettracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {

    private TextView analysisSummary;
    private Button analyzeButton;

    private HashMap<String, Double> categoryMap;
    private HashMap<String, Double> expenseMap;

    private static final String CATEGORIES_FILE_NAME = "categories.csv";
    private static final String EXPENSES_FILE_NAME = "expenses.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        analysisSummary = findViewById(R.id.analysisSummary);
        analyzeButton = findViewById(R.id.analyzeButton);

        categoryMap = new HashMap<>();
        expenseMap = new HashMap<>();

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reload categories and expenses from files before analysis
                categoryMap.clear();
                expenseMap.clear();
                loadCategoriesFromFile();
                loadExpensesFromFile();
                displayAnalysis();
            }
        });
    }

    private void displayAnalysis() {
        StringBuilder analysis = new StringBuilder();
        double totalBudget = 0;
        double totalExpenses = 0;

        // Calculate remaining budget for each category
        for (Map.Entry<String, Double> category : categoryMap.entrySet()) {
            String categoryName = category.getKey();
            double categoryBudget = category.getValue();
            double categoryExpenses = expenseMap.getOrDefault(categoryName, 0.0);

            double remaining = categoryBudget - categoryExpenses;
            analysis.append(categoryName)
                    .append(" - Budget: ₸")
                    .append(categoryBudget)
                    .append("\n")
                    .append("Expenses: ₸")
                    .append(categoryExpenses)
                    .append("\n")
                    .append("Remaining: ₸")
                    .append(remaining)
                    .append("\n\n");

            totalBudget += categoryBudget;
            totalExpenses += categoryExpenses;
        }

        // Calculate total remaining budget
        double totalRemaining = totalBudget - totalExpenses;
        analysis.append("Total Budget: ₸").append(totalBudget).append("\n")
                .append(", Total Expenses: ₸").append(totalExpenses).append("\n")
                .append(", Total Remaining: ₸").append(totalRemaining);

        analysisSummary.setText(analysis.toString());
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

    private void loadExpensesFromFile() {
        File file = new File(getFilesDir(), EXPENSES_FILE_NAME);
        if (!file.exists()) {
            return; // No file exists, nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Example expense format: "ExpenseName - 100 ₸ (CategoryName)"
                int amountStart = line.indexOf("-") + 2;
                int amountEnd = line.indexOf("₸");
                int categoryStart = line.indexOf("(") + 1;
                int categoryEnd = line.indexOf(")");

                if (amountStart > 0 && amountEnd > amountStart && categoryStart > 0 && categoryEnd > categoryStart) {
                    double amount = Double.parseDouble(line.substring(amountStart, amountEnd).trim());
                    String categoryName = line.substring(categoryStart, categoryEnd).trim();

                    double currentExpense = expenseMap.getOrDefault(categoryName, 0.0);
                    expenseMap.put(categoryName, currentExpense + amount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
