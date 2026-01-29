package com.example.juegonavidad;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RankingActivity extends AppCompatActivity {

    private TextView tvRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);
        tvRanking = findViewById(R.id.ranking);

        DBGestor gestor = new DBGestor(this);
        Cursor cursor = gestor.obtenerRanking();
        StringBuilder resultado = new StringBuilder();
        int contador = 0;
        if (cursor.moveToFirst()) {
            do {
                int pts = cursor.getInt(cursor.getColumnIndexOrThrow(DBGestor.COL_PUNTOS));
                String dif = cursor.getString(cursor.getColumnIndexOrThrow(DBGestor.COL_DIFICULTAD));
                String fec = cursor.getString(cursor.getColumnIndexOrThrow(DBGestor.COL_FECHA));
                resultado.append(contador + 1).append(". ")
                        .append(pts).append(" puntos (")
                        .append(dif).append(") - ")
                        .append(fec).append("\n\n");

                contador++;
            } while (cursor.moveToNext() && contador < 5);
        } else {
            resultado.append("Registra partidas para que aparezcan en el ranking.");
        }
        cursor.close();

        // Mostramos el texto en el TextView
        tvRanking.setText(resultado.toString());


        Button btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestor.borrarRanking();
                tvRanking.setText("Registra partidas para que aparezcan en el ranking.");
                Toast.makeText(RankingActivity.this, "Ranking borrado", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankingActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

    }

}