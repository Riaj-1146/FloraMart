package com.example.flora_mart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "rural_agri_boost.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_PLANTS = "plants";

    // Common column
    public static final String COLUMN_ID = "id";

    // Columns for plants table
    public static final String COLUMN_PLANT_NAME = "plant_name";
    public static final String COLUMN_PLANT_IMAGE = "plant_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create plants table
        String CREATE_PLANTS_TABLE = "CREATE TABLE " + TABLE_PLANTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLANT_NAME + " TEXT,"
                + COLUMN_PLANT_IMAGE + " BLOB)";
        db.execSQL(CREATE_PLANTS_TABLE);
    }

    // Insert plant into the database
    public void insertPlant(String plantName, byte[] plantImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLANT_NAME, plantName);
        values.put(COLUMN_PLANT_IMAGE, plantImage);
        db.insert(TABLE_PLANTS, null, values);
        db.close();
    }

    // Get all plants
    public Cursor getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID + " AS _id", COLUMN_PLANT_NAME, COLUMN_PLANT_IMAGE};
        return db.query(TABLE_PLANTS, projection, null, null, null, null, null);
    }

    public Cursor getPlantByName(String plantName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLANTS + " WHERE " + COLUMN_PLANT_NAME + " = ?", new String[]{plantName});
    }

    public void updatePlant(int plantId, String plantName, byte[] plantImageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLANT_NAME, plantName);
        values.put(COLUMN_PLANT_IMAGE, plantImageByteArray);

        db.update(TABLE_PLANTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(plantId)});
        db.close();
    }

    public void deletePlant(String plantName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLANTS, COLUMN_PLANT_NAME + " = ?", new String[]{plantName});
        db.close();
    }
}
