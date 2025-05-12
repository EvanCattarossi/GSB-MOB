package com.example.applimobile;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsultationFicheActivity extends AppCompatActivity {

    private TableLayout tableForfait;
    private TableLayout tableHorsForfait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_fiche);

        tableForfait = findViewById(R.id.tableFraisForfait);
        tableHorsForfait = findViewById(R.id.tableFraisHorsForfait);

        // Ex: https://api.mondomaine.com/fiches/2025-02
        String moisFiche = "2025-02";
        String url = "https://votreapi.com/api/fiches/" + moisFiche;

        fetchFicheFrais(url);
    }

    private void fetchFicheFrais(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray forfaitises = response.getJSONArray("fraisForfaitises");
                        JSONArray horsForfait = response.getJSONArray("fraisHorsForfait");

                        // Affiche les frais forfaitisés
                        for (int i = 0; i < forfaitises.length(); i++) {
                            JSONObject item = forfaitises.getJSONObject(i);
                            String type = item.getString("type");
                            int quantite = item.getInt("quantite");
                            double montantUnitaire = item.getDouble("montantUnitaire");
                            double total = item.getDouble("total");

                            addRowToTable(tableForfait, type, String.valueOf(quantite),
                                    String.format("%.2f €", montantUnitaire),
                                    String.format("%.2f €", total));
                        }

                        // Affiche les frais hors forfait
                        for (int i = 0; i < horsForfait.length(); i++) {
                            JSONObject item = horsForfait.getJSONObject(i);
                            String date = item.getString("date");
                            String libelle = item.getString("libelle");
                            double montant = item.getDouble("montant");

                            addRowToTable(tableHorsForfait, date, libelle,
                                    String.format("%.2f €", montant));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(this, "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Erreur de connexion à l'API", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonRequest);
    }

    private void addRowToTable(TableLayout table, String... columns) {
        TableRow row = new TableRow(this);
        for (String col : columns) {
            TextView tv = new TextView(this);
            tv.setText(col);
            tv.setPadding(8, 8, 8, 8);
            row.addView(tv);
        }
        table.addView(row);
    }
}