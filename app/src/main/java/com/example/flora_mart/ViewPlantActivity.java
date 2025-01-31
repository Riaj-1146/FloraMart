package com.example.flora_mart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPlantActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPlant;
    private DatabaseHelper databaseHelper;
    private PlantAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);

        recyclerViewPlant = findViewById(R.id.recycler_view_plant);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);
        databaseHelper = new DatabaseHelper(this);

        recyclerViewPlant.setLayoutManager(new LinearLayoutManager(this));
        displayPlants();

        buttonUpdate.setOnClickListener(v -> handleUpdate(null));
        buttonDelete.setOnClickListener(v -> handleDelete(null));
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
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_IMAGE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_QUANTITY));

                plantList.add(new Plant(id, name, category, price, quantity, imageBytes));
            }

            plantAdapter = new PlantAdapter(this, plantList, new PlantAdapter.OnPlantClickListener() {
                @Override
                public void onUpdateClick(Plant plant) {
                    handleUpdate(plant); // Update plant when clicked
                }

                @Override
                public void onDeleteClick(Plant plant) {
                    handleDelete(plant); // Delete plant when clicked
                }
            });
            recyclerViewPlant.setAdapter(plantAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdate(Plant plant) {
        if (plant == null) {
            Toast.makeText(this, "Please select a plant to update.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ViewPlantActivity.this, UpdatePlantActivity.class);
        intent.putExtra("PLANT_ID", plant.getId());
        intent.putExtra("PLANT_NAME", plant.getName());
        intent.putExtra("PLANT_IMAGE", plant.getImageBytes());
        intent.putExtra("PLANT_CATEGORY", plant.getCategory());
        intent.putExtra("PLANT_PRICE", plant.getPrice());
        intent.putExtra("PLANT_QUANTITY", plant.getQuantity());
        startActivity(intent);
    }

    private void handleDelete(Plant plant) {
        if (plant == null) {
            Toast.makeText(this, "Please select a plant to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.deletePlant(plant.getName());
        displayPlants();
        Toast.makeText(this, plant.getName() + " deleted successfully", Toast.LENGTH_SHORT).show();
    }
}