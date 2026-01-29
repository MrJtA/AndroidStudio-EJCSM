package com.example.ejemplobbdd;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre;
    private TextView tvResultados;
    private DBGestor dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        tvResultados = findViewById(R.id.tvResultados);
        dbHelper = new DBGestor(this);

        Button btnInsertar = findViewById(R.id.btnInsertar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnMostrar = findViewById(R.id.btnMostrar);

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                if (!nombre.isEmpty()) {
                    boolean insertado = dbHelper.insertarUsuario(nombre);
                    if (insertado) {
                        Toast.makeText(MainActivity.this, "Usuario insertado", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Error al insertar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "El campo está vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idTexto = etNombre.getText().toString().trim();

                if (!idTexto.isEmpty()) {
                    int id = Integer.parseInt(idTexto);
                    String nombreUsuario = dbHelper.obtenerUsuario(id);
                    if (nombreUsuario != null) {
                        boolean eliminado = dbHelper.eliminarUsuario(id);
                        if (eliminado) {
                            Toast.makeText(MainActivity.this,
                                    "El usuario con id=" + id + " llamado " + nombreUsuario + " se ha eliminado correctamente",
                                    Toast.LENGTH_LONG).show();
                            etNombre.setText("");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No existe un usuario con ese ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Debe ingresar un ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.obtenerUsuarios();
                if (cursor.getCount() == 0) {
                    tvResultados.setText("No hay registros");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                while (cursor.moveToNext()) {
                    stringBuilder.append("ID: ").append(cursor.getInt(0)).append(", ");
                    stringBuilder.append("Nombre: ").append(cursor.getString(1)).append("\n");
                }
                tvResultados.setText(stringBuilder.toString());
            }
        });
    }
}