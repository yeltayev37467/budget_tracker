package com.iitu_yv.budgettracker;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button goToExpensesButton;
    private Button goToCategoriesButton;

    private  Button analyzeExpensesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        goToExpensesButton = findViewById(R.id.goToExpensesButton);
        goToCategoriesButton = findViewById(R.id.goToCategoriesButton);
        analyzeExpensesButton = findViewById(R.id.analyzeExpensesButton);

        String profileName = getIntent().getStringExtra("profileName");
        welcomeText.setText("Welcome, " + profileName);

        goToExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExpensesActivity.class);
                startActivity(intent);
            }
        });

        goToCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        analyzeExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AnalysisActivity.class);
                startActivity(intent);
            }
        });
    }
}
