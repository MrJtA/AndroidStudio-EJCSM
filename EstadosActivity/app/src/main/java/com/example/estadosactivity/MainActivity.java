package com.example.estadosactivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button boton;
    private TextView registros;
    private ArrayList<String> listaRegistros = new ArrayList<>();
    private DBGestor dbGestor;
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_FECHA = "fecha";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        String msg = "onCreate: La actividad acaba de crearse.";
        Log.d(TAG, msg);
        setContentView(R.layout.activity_main);
        boton = findViewById(R.id.button);
        registros = findViewById(R.id.registros);
        registros.setMovementMethod(new ScrollingMovementMethod());
        listaRegistros.add(msg);
        dbGestor = new DBGestor(this);
        boton.setOnClickListener(v -> {
            dbGestor.eliminarUsuarios();
            cargarLista();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void cargarLista() {
        registros.setText("");
        Cursor cursor = dbGestor.obtenerRegistros();
        if (cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            int nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE);
            int fechaIndex = cursor.getColumnIndex(COLUMN_FECHA);
            if (nombreIndex != -1 && fechaIndex != -1) {
                do {
                    String nombreRegistro = cursor.getString(nombreIndex);
                    String fechaRegistro = cursor.getString(fechaIndex);
                    sb.append("[").append(fechaRegistro).append("] ").append(nombreRegistro).append("\n\n");
                } while (cursor.moveToNext());
                registros.setText(sb.toString());
            }
        }
        cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String msg = "onCreate: La actividad va a hacerse visible.";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String msg = "onResume: La actividad ha pasado a primer plano (ahora es interactiva).";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String msg = "onPause: Otra actividad ocupa el primer plano (a punto de detenerse o dejar de ser visible).";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

    @Override
    protected void onStop() {
        super.onStop();
        String msg = "onStop: La actividad ya no es visible para el usuario.";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String msg = "onRestart: La actividad vuelve a ser reiniciada tras de haber sido detenida.";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String msg = "onDestroy: La actividad está siendo destruida.";
        Log.d(TAG, msg);
        dbGestor.insertarRegistro(msg);
        cargarLista();
    }

}