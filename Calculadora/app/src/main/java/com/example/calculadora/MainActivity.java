package com.example.calculadora;

import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("#.##");
    TextView caja1;
    TextView caja2;
    View buttonC;
    View buttonIgual;
    double resultadoGlobal = 0.0;
    String operacionPendiente = "";
    double valorActual = 0;
    boolean esNuevoNumero = true;
    boolean errorActivo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caja1 = findViewById(R.id.text);
        caja2 = findViewById(R.id.text2);
        buttonC = findViewById(R.id.buttonC);
        buttonIgual = findViewById(R.id.buttonIgual);
        caja1.setText("0");
    }

    public void reset(View boton) {
        boton.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        resultadoGlobal = 0;
        valorActual = 0;
        esNuevoNumero = true;
        caja2.setText("");
        caja1.setText("0");
        errorActivo = false;
    }

    public void borrar(View boton) {
        if (errorActivo) {
            reset(buttonC);
            return;
        }
        boton.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        String cadenaOriginal = caja1.getText().toString();
        String cadenaSinUltimo;
        if (cadenaOriginal.length() > 1) {
            cadenaSinUltimo = cadenaOriginal.substring(0, cadenaOriginal.length() - 1);
            caja1.setText(cadenaSinUltimo);
        } else {
            caja1.setText("0");
            esNuevoNumero = true;
        }
    }

    public void numero(View boton) {
        boton.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        Button btn_numero = (Button) boton;
        String nuevoDigito = btn_numero.getText().toString();
        String textoActual = caja1.getText().toString();
        if (errorActivo) {
            reset(buttonC);
            textoActual = caja1.getText().toString();
        }
        if (nuevoDigito.equals(".")) {
            if (!textoActual.contains(".")) {
                caja1.setText(textoActual + nuevoDigito);
                esNuevoNumero = false;
            }
            return;
        }
        if (esNuevoNumero || textoActual.equals("0")) {
            caja1.setText(nuevoDigito);
            esNuevoNumero = false;
        } else {
            caja1.setText(textoActual + nuevoDigito);
        }
    }
    public void operadorPendiente(String operador) {
        if (errorActivo) return;

        switch (operacionPendiente) {
            case "+":
                resultadoGlobal += valorActual;
                break;
            case "-":
                resultadoGlobal -= valorActual;
                break;
            case "x":
                resultadoGlobal *= valorActual;
                break;
            case "/":
                if (valorActual != 0) {
                    resultadoGlobal /= valorActual;
                } else {
                    caja1.setText("Error");
                    resultadoGlobal = 0;
                    errorActivo = true;
                }
                break;
            case "%":
                resultadoGlobal %= valorActual;
                break;
            case "^":
                resultadoGlobal = Math.pow(resultadoGlobal, valorActual);
                break;
            case "log":
                if (valorActual > 0) {
                    resultadoGlobal = Math.log10(resultadoGlobal);
                } else {
                    caja1.setText("Error");
                    errorActivo = true;
                }
                break;
            case "ln":
                if (valorActual > 0) {
                    resultadoGlobal = Math.log(resultadoGlobal);
                } else {
                    caja1.setText("Error");
                    errorActivo = true;
                }
                break;
        }
    }

    public void operador(View boton) {
        boton.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        Button btn_operador = (Button) boton;
        String nuevoOperador = btn_operador.getText().toString();
        if (errorActivo) {
            reset(buttonC);
        }
        try {
            valorActual = Double.parseDouble(caja1.getText().toString());
        } catch (NumberFormatException e) {
            valorActual = 0;
        }
        if (operacionPendiente.isEmpty()) {
            resultadoGlobal = valorActual;
        } else {
            operadorPendiente(operacionPendiente);
        }
        if (errorActivo) return;
        operacionPendiente = nuevoOperador;
        if (nuevoOperador.equals("log") || nuevoOperador.equals("ln")) {
            caja2.setText(nuevoOperador + "(" + df.format(resultadoGlobal) + ")");
            caja1.setText(df.format(resultadoGlobal));
            operacionPendiente = nuevoOperador;
            esNuevoNumero = true;
        } else {
            caja2.setText(df.format(resultadoGlobal) + " " + nuevoOperador);
            caja1.setText("0");
            esNuevoNumero = true;
        }
    }

    public void calcular(View boton) {
        boton.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        if (errorActivo) {
            reset(buttonC);
        }
        try {
            valorActual = Double.parseDouble(caja1.getText().toString());
        } catch (NumberFormatException e) {
            valorActual = 0;
        }
        operadorPendiente(operacionPendiente);
        if (errorActivo) return;
        caja1.setText(df.format(resultadoGlobal));
        caja2.setText("");
        operacionPendiente = "";
        esNuevoNumero = true;
        valorActual = 0;
    }

}