package com.example.flora_mart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Flora_Mart.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_PLANTS = "Plants_Information";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLANT_NAME = "plant_name";
    public static final String COLUMN_PLANT_IMAGE = "plant_image";
    public static final String COLUMN_PLANT_CATEGORY = "plant_category";
    public static final String COLUMN_PLANT_PRICE = "plant_price";
    public static final String COLUMN_PLANT_QUANTITY = "plant_quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANTS_TABLE = "CREATE TABLE " + TABLE_PLANTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PLANT_NAME + " TEXT," +
                COLUMN_PLANT_IMAGE + " BLOB," +
                COLUMN_PLANT_CATEGORY + " TEXT," +
                COLUMN_PLANT_PRICE + " REAL," +
                COLUMN_PLANT_QUANTITY + " INTEGER)";
        db.execSQL(CREATE_PLANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        onCreate(db);
    }

    public void insertPlant(String plantName, String category, double price, int quantity, byte[] plantImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLANT_NAME, plantName);
        values.put(COLUMN_PLANT_CATEGORY, category);
        values.put(COLUMN_PLANT_PRICE, price);
        values.put(COLUMN_PLANT_QUANTITY, quantity);
        values.put(COLUMN_PLANT_IMAGE, plantImage);

        db.insert(TABLE_PLANTS, null, values);
        db.close();
    }


    public Cursor getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID + " AS _id", COLUMN_PLANT_NAME, COLUMN_PLANT_IMAGE, COLUMN_PLANT_CATEGORY, COLUMN_PLANT_PRICE, COLUMN_PLANT_QUANTITY};
        return db.query(TABLE_PLANTS, projection, null, null, null, null, null);
    }

    public Cursor getPlantByName(String plantName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLANTS + " WHERE " + COLUMN_PLANT_NAME + " = ?", new String[]{plantName});
    }

    public boolean updatePlant(int plantId, String plantName, String category, double price, int quantity, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLANT_NAME, plantName);
        values.put(COLUMN_PLANT_CATEGORY, category);  // New column for category
        values.put(COLUMN_PLANT_PRICE, price);       // New column for price
        values.put(COLUMN_PLANT_QUANTITY, quantity); // New column for quantity
        values.put(COLUMN_PLANT_IMAGE, image);       // Image column

        // Update the plant in the database based on plantId
        int rowsAffected = db.update(TABLE_PLANTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(plantId)});

        return rowsAffected > 0;  // Return true if the update was successful
    }


    public void deletePlant(String plantName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLANTS, COLUMN_PLANT_NAME + " = ?", new String[]{plantName});
        db.close();
    }
}