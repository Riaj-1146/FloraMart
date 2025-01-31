package com.example.flora_mart;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeletePlantActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private ImageView imageViewPlant;
    private Button buttonDelete;
    private Button buttonSearch;
    private TextView textViewPlantId, textViewPrice, textViewCategory;

    private DatabaseHelper databaseHelper;
    private byte[] plantImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_plant);

        // Initialize views
        editTextName = findViewById(R.id.text_view_plant_name);
        textViewPlantId = findViewById(R.id.text_view_plant_id);
        imageViewPlant = findViewById(R.id.image_view_plant);
        textViewPrice = findViewById(R.id.text_view_price);
        textViewCategory = findViewById(R.id.text_view_category);

        buttonDelete = findViewById(R.id.button_delete);
        buttonSearch = findViewById(R.id.button_search);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set click listeners
        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> deleteProduct());
    }

    // Search plant by name and display details
    private void searchProduct() {
        String plantName = editTextName.getText().toString().trim();
        if (plantName.isEmpty()) {
            Toast.makeText(this, "Please enter a plant name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getPlantByName(plantName);
        if (cursor != null && cursor.moveToFirst()) {
            int plantId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_PRICE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_CATEGORY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_IMAGE));

            // Display the plant details
            textViewPlantId.setText("Plant ID: " + plantId);
            textViewPrice.setText("Price: " + price);
            textViewCategory.setText("Category: " + category);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewPlant.setImageBitmap(bitmap);
                plantImageByteArray = image;
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Plant not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete the plant based on its name
    private void deleteProduct() {
        String plantName = editTextName.getText().toString().trim();
        if (plantName.isEmpty()) {
            Toast.makeText(this, "Please enter the plant name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getPlantByName(plantName);
        if (cursor != null && cursor.moveToFirst()) {
            // Call deletePlant instead of deletePlantById
            databaseHelper.deletePlant(plantName);
            cursor.close();

            Toast.makeText(this, "Plant deleted successfully", Toast.LENGTH_SHORT).show();
            textViewPlantId.setText("");  // Clear the ID
            textViewPrice.setText("");    // Clear price
            textViewCategory.setText(""); // Clear category
            imageViewPlant.setImageResource(R.drawable.demo_icon); // Reset image
        } else {
            Toast.makeText(this, "Plant not found", Toast.LENGTH_SHORT).show();
        }
    }
}
