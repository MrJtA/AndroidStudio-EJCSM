package com.example.pruebasrecursosandroid;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ToggleButton boton;
    private CameraManager cameraManager;
    private String cameraId;
    private Vibrator vibrator;
    private static final long VIBRATION_DURATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        boton = findViewById(R.id.toggle_flashlight);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error: No se pudo acceder a la cámara/linterna.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void toggleFlashLight(View view) {
        boolean isChecked = ((ToggleButton) view).isChecked();
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect effect = VibrationEffect.createOneShot(
                        VIBRATION_DURATION,
                        VibrationEffect.DEFAULT_AMPLITUDE
                );
                vibrator.vibrate(effect);
            } else {
                // Método obsoleto para versiones anteriores
                vibrator.vibrate(VIBRATION_DURATION);
            }
        }
        if (cameraId != null) {
            try {
                cameraManager.setTorchMode(cameraId, isChecked);

                if (isChecked) {
                    Toast.makeText(this, "Linterna encendida", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Linterna apagada", Toast.LENGTH_SHORT).show();
                }
            } catch (CameraAccessException e) {
                Toast.makeText(this, "Error al controlar la linterna.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraManager != null && cameraId != null) {
            try {
                cameraManager.setTorchMode(cameraId, false);
                Toast.makeText(this, "Linterna apagada al salir.", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}