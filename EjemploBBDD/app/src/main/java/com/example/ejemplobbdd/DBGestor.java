package com.example.ejemplobbdd;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBGestor extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    private static final String TABLE_NAME = "usuarios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";

    // Constructor
    public DBGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla SQL
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para insertar datos
    public boolean insertarUsuario(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Método para obtener todos los usuarios
    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Método para eliminar un usuario por ID
    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    public String obtenerUsuario(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            cursor.close();
            return nombre;
        }
        return null;
    }

}