package com.example.flora_mart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertPlantActivity extends AppCompatActivity {

    private ImageView img_image;
    private EditText et_plant_name, et_plant_category, et_plant_price, et_plant_quantity;
    private Button bt_choose_img, bt_insert_img;
    private DatabaseHelper databaseHelper;
    private byte[] imageByteArray;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_plant);

        img_image = findViewById(R.id.img_image);
        et_plant_name = findViewById(R.id.et_plant_name);
        et_plant_category = findViewById(R.id.et_plant_category);
        et_plant_price = findViewById(R.id.et_plant_price);
        et_plant_quantity = findViewById(R.id.et_plant_quantity);
        bt_choose_img = findViewById(R.id.bt_choose_img);
        bt_insert_img = findViewById(R.id.bt_insert_img);
        databaseHelper = new DatabaseHelper(this);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            img_image.setImageBitmap(imageBitmap);
                            imageByteArray = bitmapToByteArray(imageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        bt_choose_img.setOnClickListener(view -> chooseImage());
        bt_insert_img.setOnClickListener(view -> insertPlant());
    }

    private void chooseImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertPlant() {
        String name = et_plant_name.getText().toString().trim();
        String category = et_plant_category.getText().toString().trim();
        String priceText = et_plant_price.getText().toString().trim();
        String quantityText = et_plant_quantity.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || imageByteArray == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity format", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.insertPlant(name, category, price, quantity, imageByteArray);
        Toast.makeText(this, "Plant inserted successfully!", Toast.LENGTH_SHORT).show();


        et_plant_name.setText("");
        et_plant_category.setText("");
        et_plant_price.setText("");
        et_plant_quantity.setText("");
        img_image.setImageResource(R.drawable.demo_icon);
        imageByteArray = null;
    }
}
