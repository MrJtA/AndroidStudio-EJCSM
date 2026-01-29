package com.example.estadosactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBGestor extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "actividad.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "registros";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_FECHA = "fecha";

    public DBGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertarRegistro(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean eliminarUsuarios() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, null, null);
        return result > 0;
    }
    public Cursor obtenerRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }


}