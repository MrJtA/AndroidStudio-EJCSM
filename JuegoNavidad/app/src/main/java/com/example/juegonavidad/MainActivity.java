package com.example.juegonavidad;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout layoutContainer;
    private TextView tvPuntos, tvTiempo;
    private Button btnEmpezar, btnVolver;
    private int puntos = 0;
    private Handler handler = new Handler();
    private boolean juegoActivo = false;
    private String dificultad;
    private long tiempoSpawneo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContainer = findViewById(R.id.layoutContainer);
        tvPuntos = findViewById(R.id.tvPuntos);
        tvTiempo = findViewById(R.id.tvTiempo);
        btnEmpezar = findViewById(R.id.btnEmpezar);
        btnVolver = findViewById(R.id.btnVolver);
        dificultad = getIntent().getStringExtra("dificultad");
        if (dificultad == null) {
            dificultad = "facil";
        }
        if (dificultad.equals("facil")) {
            tiempoSpawneo = 300;
        } else if (dificultad.equals("media")) {
            tiempoSpawneo = 225;
        } else if (dificultad.equals("dificil")) {
            tiempoSpawneo = 150;
        }

        btnEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuentaAtras();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DificultadesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (juegoActivo) {
                    puntos -= 10;
                    if (puntos < 0) puntos = 0;
                    tvPuntos.setText("Puntos: " + puntos);
                }
            }
        });

    }

    private void cuentaAtras() {
        btnEmpezar.setVisibility(View.INVISIBLE);
        btnVolver.setVisibility(View.GONE);
        new CountDownTimer(3500, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTiempo.setText("Tiempo: " + (millisUntilFinished / 1000));
            }
            public void onFinish() {
                empezarPartida();
            }
        }.start();
    }

    private void empezarPartida() {
        puntos = 0;
        tvPuntos.setText("Puntos: 0");
        juegoActivo = true;
        crearPersonajes();
        new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTiempo.setText("Tiempo: " + millisUntilFinished/1000);
            }
            public void onFinish() {
                juegoActivo = false;
                tvTiempo.setText("¡Se acabó!");
                handler.removeCallbacksAndMessages(null);
                layoutContainer.removeAllViews();
                btnEmpezar.setVisibility(View.VISIBLE);
                btnVolver.setVisibility(View.VISIBLE);
                String fecha = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();
                DBGestor gestor = new DBGestor(MainActivity.this);
                gestor.insertarPuntuacion(puntos, dificultad, fecha);
            }
        }.start();
    }

    private void crearPersonajes() {
        if (!juegoActivo) return;
        crearPersonaje(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                crearPersonajes();
            }
        }, tiempoSpawneo);
    }

    public void crearPersonaje(View view) {

        final ImageView personaje = new ImageView(this);
        personaje.setImageResource(R.drawable.carti_navidad); // Imagen inicial de "thread"
        personaje.setLayoutParams(new RelativeLayout.LayoutParams(300, 300));

        // Posición aleatoria
        Random random = new Random();
        personaje.setX(random.nextInt(layoutContainer.getWidth() - 300));
        personaje.setY(random.nextInt(layoutContainer.getHeight() - 300));

        // Añadir la imagen al layout
        layoutContainer.addView(personaje);

        long tiempoDeVida = 0;
        if (dificultad.equals("facil")) {
            tiempoDeVida = 2500;
        }
        else if (dificultad.equals("media")) {
            tiempoDeVida = 1750;
        }
        else {
            tiempoDeVida = 1000;
        }

        final Runnable eliminarPersonaje = new Runnable() {
            @Override
            public void run() {
                if (layoutContainer.indexOfChild(personaje) != -1) {
                    layoutContainer.removeView(personaje);
                }
            }
        };

        // Programar el hilo
        handler.postDelayed(eliminarPersonaje, tiempoDeVida);

        // Acción al hacer clic en la imagen
        personaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (juegoActivo) {
                    handler.removeCallbacks(eliminarPersonaje); // Detener la eliminación programada
                    layoutContainer.removeView(personaje); // Eliminar la imagen
                    puntos += 10;
                    tvPuntos.setText("Puntos: " + puntos);
                }
            }
        });
    }

}