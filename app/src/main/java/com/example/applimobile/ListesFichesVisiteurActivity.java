package com.example.applimobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListesFichesVisiteurActivity extends AppCompatActivity {

    private ListView listeFiches;
    private ArrayList<String> fichesList;
    private ArrayAdapter<String> adapter;

    private static final String URL_LISTE_FICHES = "http://192.168.0.24/gsb-api/fiches/get_by_id.php"; // Mets ici ton URL correcte !

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiches); // adapte ici le nom de ton fichier XML

        listeFiches = findViewById(R.id.listeFiches);
        fichesList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fichesList);
        listeFiches.setAdapter(adapter);

        chargerFiches();
    }

    private void chargerFiches() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_LISTE_FICHES,
                null,
                response -> afficherFiches(response),
                error -> Toast.makeText(this, "Erreur de chargement : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(jsonArrayRequest);
    }

    private void afficherFiches(JSONArray fiches) {
        try {
            fichesList.clear(); // Nettoie d'abord l'ancienne liste

            for (int i = 0; i < fiches.length(); i++) {
                JSONObject fiche = fiches.getJSONObject(i);

                String mois = fiche.optString("mois", "??");
                String annee = fiche.optString("annee", "??");
                String montantValide = fiche.optString("montantValide", "0");
                String etat = fiche.optString("etat", "en attente");

                // Construction d'un affichage pour chaque fiche
                String ficheText = "Mois: " + mois + "/" + annee + " | Montant: " + montantValide + "€ | État: " + etat;
                fichesList.add(ficheText);
            }

            adapter.notifyDataSetChanged(); // Notifier que la liste a changé pour rafraîchir l'affichage

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de traitement des fiches", Toast.LENGTH_LONG).show();
        }
    }
}
