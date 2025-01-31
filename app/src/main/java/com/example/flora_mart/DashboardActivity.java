package com.example.flora_mart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPlant;
    private DatabaseHelper databaseHelper;
    private PlantAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerViewPlant = findViewById(R.id.recycler_view_plant);
        databaseHelper = new DatabaseHelper(this);

        recyclerViewPlant.setLayoutManager(new LinearLayoutManager(this));
        displayPlants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayPlants();
    }

    private void displayPlants() {
        try {
            Cursor cursor = databaseHelper.getAllPlants();
            List<Plant> plantList = new ArrayList<>();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_QUANTITY));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_IMAGE));

                plantList.add(new Plant(id, name, category, price, quantity, imageBytes));
            }


            recyclerViewPlant.setAdapter(plantAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
