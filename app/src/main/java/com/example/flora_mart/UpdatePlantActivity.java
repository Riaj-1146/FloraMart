package com.example.flora_mart;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdatePlantActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private ImageView imageViewPlant;
    private Button buttonUpdate;
    private Button buttonSelectImage;
    private Button buttonSearch;
    private TextView textViewPlantId;

    private DatabaseHelper databaseHelper;
    private byte[] plantImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);

        editTextName = findViewById(R.id.edit_text_plant_name);
        imageViewPlant = findViewById(R.id.image_view_plant);
        buttonUpdate = findViewById(R.id.button_update);
        buttonSelectImage = findViewById(R.id.button_select_image);
        buttonSearch = findViewById(R.id.button_search);
        textViewPlantId = findViewById(R.id.text_view_plant_id);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateProduct());
    }

    private void searchProduct() {
        String plantName = editTextName.getText().toString().trim();
        if (plantName.isEmpty()) {
            Toast.makeText(this, "Please enter a plant name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getPlantByName(plantName);

        if (cursor != null && cursor.moveToFirst())
        {
            int plantId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLANT_IMAGE));

            textViewPlantId.setText("Plant ID: " + plantId);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewPlant.setImageBitmap(bitmap);
                plantImageByteArray = image;
            }
            cursor.close();
        }
        else
        {
            Toast.makeText(this, "Plant not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewPlant.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                plantImageByteArray = byteArrayOutputStream.toByteArray();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProduct() {

        String plantName = editTextName.getText().toString().trim();

        if (plantName.isEmpty())
        {
            Toast.makeText(this, "Please give plant name", Toast.LENGTH_SHORT).show();
            return;
        }

        String plantIdText = textViewPlantId.getText().toString();
        int plantId = Integer.parseInt(plantIdText.replaceAll("\\D+", ""));

        databaseHelper.updatePlant(plantId, plantName, plantImageByteArray);
    }
}