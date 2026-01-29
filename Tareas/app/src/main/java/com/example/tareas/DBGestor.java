package com.example.tareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBGestor extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tareas.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLA_TAREAS = "tareas";
    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_COMPLETADA = "completada";

    public DBGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearTabla =
                "CREATE TABLE " + TABLA_TAREAS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TITULO + " TEXT NOT NULL, " +
                        COL_DESCRIPCION + " TEXT, " +
                        COL_COMPLETADA + " INTEGER DEFAULT 0)";
        db.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TAREAS);
        onCreate(db);
    }

    public void insertarTarea(String titulo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, titulo);
        values.put(COL_DESCRIPCION, descripcion);
        values.put(COL_COMPLETADA, 0);
        db.insert(TABLA_TAREAS, null, values);
    }

    public void completarTarea(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COMPLETADA, 1);
        db.update(TABLA_TAREAS, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void actualizarTarea(int id, String titulo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, titulo);
        values.put(COL_DESCRIPCION, descripcion);
        db.update(TABLA_TAREAS, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Bundle obtenerDatosDeTarea(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT titulo, descripcion FROM " + TABLA_TAREAS + " WHERE id=?",
                new String[]{String.valueOf(id)}
        );
        if (c.moveToFirst()) {
            Bundle datos = new Bundle();
            datos.putString("titulo", c.getString(0));
            datos.putString("descripcion", c.getString(1));
            c.close();
            return datos;
        }
        c.close();
        return null;
    }

    public ArrayList<Map<String, String>> obtenerTareasCompletadas() {
        ArrayList<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, titulo, descripcion FROM tareas WHERE completada = 1", null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> tarea = new HashMap<>();
                tarea.put("id", cursor.getString(0));
                tarea.put("titulo", cursor.getString(1));
                tarea.put("descripcion", cursor.getString(2));
                lista.add(tarea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }


    public boolean eliminarTarea(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLA_TAREAS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public Cursor obtenerTareas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLA_TAREAS, null);
    }
}
