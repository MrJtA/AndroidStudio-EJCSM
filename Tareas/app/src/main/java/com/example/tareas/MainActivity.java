package com.example.tareas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private EditText etNuevoTitulo, etNuevaTarea;
    private Button btnAgregarTarea, btnMostrarTareasCompletadas;
    private ListView lvListaTareas;
    private SimpleAdapter adaptador;
    private ArrayList<Map<String, String>> listaTareas;
    private DBGestor dbGestor;
    private float pulsaX, pulsaY;
    private static final float CANTIDAD_DESPLAZAMIENTO = 100f;
    private Integer tareaSeleccionadaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etNuevoTitulo = findViewById(R.id.etNuevoTitulo);
        etNuevaTarea = findViewById(R.id.etNuevaTarea);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        btnMostrarTareasCompletadas = findViewById(R.id.btnMostrarTareasCompletadas);
        lvListaTareas = findViewById(R.id.lvListaTareas);
        dbGestor = new DBGestor(this);
        listaTareas = new ArrayList<>();
        adaptador = new SimpleAdapter(
                this,
                listaTareas,
                android.R.layout.simple_list_item_2,
                new String[]{"titulo", "descripcion"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        lvListaTareas.setAdapter(adaptador);
        cargarTareasDesdeDB();

        btnAgregarTarea.setOnClickListener(v -> {
            String titulo = etNuevoTitulo.getText().toString().trim();
            String descripcion = etNuevaTarea.getText().toString().trim();

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(MainActivity.this, "Completa los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tareaSeleccionadaId != null) {
                dbGestor.actualizarTarea(tareaSeleccionadaId, titulo, descripcion);
                Toast.makeText(MainActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                tareaSeleccionadaId = null;
                btnAgregarTarea.setText("Agregar");
            } else {
                dbGestor.insertarTarea(titulo, descripcion);
                Toast.makeText(MainActivity.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
            }
            etNuevoTitulo.setText("");
            etNuevaTarea.setText("");
            cargarTareasDesdeDB();
        });

        btnMostrarTareasCompletadas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TareasCompletadasActivity.class);
            startActivity(intent);
        });

        lvListaTareas.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> tarea = listaTareas.get(position);
            int idTarea = Integer.parseInt(tarea.get("id"));
            dbGestor.completarTarea(idTarea);
            Toast.makeText(MainActivity.this, "Tarea completada", Toast.LENGTH_SHORT).show();
            cargarTareasDesdeDB();
        });

        lvListaTareas.setOnItemLongClickListener((parent, view, position, id) -> {
            Map<String, String> tarea = listaTareas.get(position);
            tareaSeleccionadaId = Integer.parseInt(tarea.get("id"));
            etNuevoTitulo.setText(tarea.get("titulo"));
            etNuevaTarea.setText(tarea.get("descripcion"));
            btnAgregarTarea.setText("Actualizar");
            Toast.makeText(MainActivity.this, "Tarea en edición", Toast.LENGTH_SHORT).show();
            return true;
        });

        lvListaTareas.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pulsaX = event.getX();
                        pulsaY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float dx = event.getX() - pulsaX;
                        float dy = Math.abs(event.getY() - pulsaY);
                        if (dx > CANTIDAD_DESPLAZAMIENTO && dx > dy) {
                            int position = lvListaTareas.pointToPosition((int) pulsaX, (int) pulsaY);
                            if (position != AdapterView.INVALID_POSITION) {
                                Map<String, String> tarea = listaTareas.get(position);
                                int id = Integer.parseInt(tarea.get("id"));
                                dbGestor.eliminarTarea(id);
                                Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                                cargarTareasDesdeDB();
                            }
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarTareasDesdeDB() {
        listaTareas.clear();
        Cursor cursor = dbGestor.obtenerTareas();
        if (cursor != null && cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("id");
            int idxTitulo = cursor.getColumnIndex("titulo");
            int idxDescripcion = cursor.getColumnIndex("descripcion");
            int idxCompletada = cursor.getColumnIndex("completada");
            do {
                if (cursor.getInt(idxCompletada) == 0) { // solo pendientes
                    Map<String, String> tarea = new HashMap<>();
                    tarea.put("id", cursor.getString(idxId));
                    tarea.put("titulo", cursor.getString(idxTitulo));
                    tarea.put("descripcion", cursor.getString(idxDescripcion));
                    listaTareas.add(tarea);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        adaptador.notifyDataSetChanged();
    }
}
