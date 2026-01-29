package com.example.juegonavidad;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private ImageView sonido;
    private ImageView sonidoMuted;
    private static MediaPlayer musica;
    private static boolean muteado = false;
    private boolean cambioPantalla = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        sonido = findViewById(R.id.imgSonido);
        sonidoMuted = findViewById(R.id.imgSonidoMuted);
        if (musica == null) {
            sonidoMuted.setVisibility(View.INVISIBLE);
            musica = MediaPlayer.create(this, R.raw.musica);
            musica.setLooping(true);
            musica.start();
        }

        Button btnJugar = findViewById(R.id.btnJugar);
        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioPantalla = true;
                Intent intent = new Intent(IntroActivity.this, DificultadesActivity.class);
                startActivity(intent);
            }
        });

        Button btnRanking = findViewById(R.id.btnRanking);
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioPantalla = true;
                Intent intent = new Intent(IntroActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        Button btnSonido = findViewById(R.id.btnSonido);
        btnSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteado = !muteado;
                if (muteado) {
                    sonido.setVisibility(View.INVISIBLE);
                    sonidoMuted.setVisibility(View.VISIBLE);
                    musica.pause();
                } else {
                    sonido.setVisibility(View.VISIBLE);
                    sonidoMuted.setVisibility(View.INVISIBLE);
                    musica.start();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musica != null) {
            musica.release();
            musica = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!cambioPantalla && musica != null && musica.isPlaying()) {
            musica.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cambioPantalla = false;
        if (musica != null) {
            if (muteado) {
                sonido.setVisibility(View.INVISIBLE);
                sonidoMuted.setVisibility(View.VISIBLE);
                musica.pause();
            } else {
                sonido.setVisibility(View.VISIBLE);
                sonidoMuted.setVisibility(View.INVISIBLE);
                musica.start();
            }
        }
    }

}