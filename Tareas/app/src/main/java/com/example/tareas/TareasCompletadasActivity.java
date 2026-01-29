package com.example.tareas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class TareasCompletadasActivity extends AppCompatActivity {

    Button btnMostrarTodasTareas;
    ListView lvListaTareas;
    private SimpleAdapter adaptador;
    private ArrayList<Map<String, String>> listaTareas;
    DBGestor dbGestor;
    private float pulsaX, pulsaY;
    private static final float CANTIDAD_DESPLAZAMIENTO = 100f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_completadas);
        dbGestor = new DBGestor(this);
        lvListaTareas = findViewById(R.id.lvListaTareas);
        btnMostrarTodasTareas = findViewById(R.id.btnMostrarTodasTareas);
        btnMostrarTodasTareas.setOnClickListener(v -> finish());
        cargarTareasCompletadas();
        adaptador = new SimpleAdapter(
                this,
                listaTareas,
                android.R.layout.simple_list_item_2,
                new String[]{"titulo", "descripcion"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        lvListaTareas.setAdapter(adaptador);

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
                                Toast.makeText(TareasCompletadasActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                                cargarTareasCompletadas();
                                adaptador.notifyDataSetChanged();
                            }
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void cargarTareasCompletadas() {
        listaTareas = new ArrayList<>();
        ArrayList<Map<String, String>> completadas = dbGestor.obtenerTareasCompletadas();
        listaTareas.clear();
        listaTareas.addAll(completadas);
    }
}
