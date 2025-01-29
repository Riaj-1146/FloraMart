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

public class DeletePlantActivity  extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private ImageView imageViewPlant;
    private Button buttonDelete;
    private Button buttonSearch;
    private TextView textViewPlantId;

    private DatabaseHelper databaseHelper;
    private byte[] plantImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_plant);

        editTextName = findViewById(R.id.text_view_plant_name);
        textViewPlantId = findViewById(R.id.text_view_plant_id);
        imageViewPlant = findViewById(R.id.image_view_plant);

        buttonDelete = findViewById(R.id.button_delete);
        buttonSearch = findViewById(R.id.button_search);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> deleteProduct());
    }

    private void searchProduct() {
        String plantName = editTextName.getText().toString().trim();
        if (plantName.isEmpty()) {
            Toast.makeText(this, "Please enter a plant name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getPlantByName(plantName);
        if (cursor != null && cursor.moveToFirst()) {
            int plantId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_IMAGE));

            textViewPlantId.setText("Plant ID: " + plantId);

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

    private void deleteProduct() {
        String plantName = editTextName.getText().toString().trim();
        databaseHelper.deletePlant(plantName);
    }
}