package com.smartpantry.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL)";

        db.execSQL(createPantryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());

        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> pantryItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT));

                pantryItems.add(new PantryItem(id, name, quantity, unit));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pantryItems;
    }

    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        PantryItem item = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT));

            item = new PantryItem(id, name, quantity, unit);
        }

        cursor.close();
        return item;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
