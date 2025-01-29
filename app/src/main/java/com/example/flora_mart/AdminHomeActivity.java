package com.example.flora_mart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button btnInsertPlant = findViewById(R.id.btn_insert_plant);
        Button btnViewPlant = findViewById(R.id.btn_view_plant);

        btnInsertPlant.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, InsertPlantActivity.class);
            startActivity(intent);
        });

        btnViewPlant.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ViewPlantActivity.class);
            startActivity(intent);
        });
    }
}