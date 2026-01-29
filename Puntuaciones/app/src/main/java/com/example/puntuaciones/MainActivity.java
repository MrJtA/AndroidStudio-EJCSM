package com.example.puntuaciones;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private TextView textViewResults;
    private static final String URL_STRING = "http://10.0.2.2/joaco/ranking.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResults = findViewById(R.id.textViewResults);

        // Ejecutar la tarea en segundo plano para obtener datos
        new FetchScoresTask().execute(URL_STRING);
    }

    private class FetchScoresTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception ignored) {
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Error")) {
                //se produjo un error y lo mostramos en el textView
                textViewResults.setText(result);
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                StringBuilder scoresText = new StringBuilder();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    int score = jsonObject.getInt("score");

                    scoresText.append("Usuario: ").append(username).append(", Puntuación: ").append(score).append("\n");
                }

                textViewResults.setText(scoresText.toString());

            } catch (Exception e) {
                textViewResults.setText("Error parsing JSON: " + e.getMessage());
            }
        }
    }
}