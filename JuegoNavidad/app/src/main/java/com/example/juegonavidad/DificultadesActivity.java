package com.example.juegonavidad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DificultadesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dificultades);

        Button btnFacil = findViewById(R.id.btnFacil);
        btnFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DificultadesActivity.this, MainActivity.class);
                intent.putExtra("dificultad", "facil");
                startActivity(intent);
            }
        });

        Button btnMedia = findViewById(R.id.btnMedia);
        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( DificultadesActivity.this, MainActivity.class);
                intent.putExtra("dificultad", "media");
                startActivity(intent);
            }
        });

        Button btnDificil = findViewById(R.id.btnDificil);
        btnDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DificultadesActivity.this, MainActivity.class);
                intent.putExtra("dificultad", "dificil");
                startActivity(intent);
            }
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DificultadesActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

    }

}