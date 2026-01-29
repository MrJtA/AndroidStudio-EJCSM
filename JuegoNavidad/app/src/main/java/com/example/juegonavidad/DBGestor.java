package com.example.juegonavidad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBGestor extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "partidas.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLA_RANKING = "ranking";
    public static final String COL_ID = "id";
    public static final String COL_PUNTOS = "puntos";
    public static final String COL_DIFICULTAD = "dificultad";
    public static final String COL_FECHA = "fecha";

    public DBGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearTabla =
                "CREATE TABLE " + TABLA_RANKING + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_PUNTOS + " INTEGER, " +
                        COL_DIFICULTAD + " TEXT, " +
                        COL_FECHA + " TEXT)";
        db.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_RANKING);
        onCreate(db);
    }

    public void insertarPuntuacion(int puntos, String dificultad, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PUNTOS, puntos);
        values.put(COL_DIFICULTAD, dificultad);
        values.put(COL_FECHA, fecha);
        db.insert(TABLA_RANKING, null, values);
        db.close();
    }

    public Cursor obtenerRanking() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLA_RANKING + " ORDER BY " + COL_PUNTOS + " DESC", null);
    }

    public void borrarRanking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_RANKING);
        db.close();
    }

}