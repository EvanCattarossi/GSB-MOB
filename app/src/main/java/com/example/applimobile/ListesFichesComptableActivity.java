package com.example.applimobile;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListesFichesComptableActivity extends AppCompatActivity {

    private LinearLayout containerFiches;
    private static final String URL_FICHES_PAR_VISITEUR = "http://192.168.0.24/gsb-api/fiches/get_all.php"; //URL ici !

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiches_comptable); //  fichier XML

        containerFiches = findViewById(R.id.containerFiches);

        chargerFiches();
    }

    private void chargerFiches() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_FICHES_PAR_VISITEUR,
                null,
                this::afficherFiches,
                error -> Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }

    private void afficherFiches(JSONArray fiches) {
        try {
            for (int i = 0; i < fiches.length(); i++) {
                JSONObject fiche = fiches.getJSONObject(i);

                String nom = fiche.optString("nom", "Nom ?");
                String mois = fiche.optString("mois", "??/??");
                String montantValide = fiche.optString("montantValide", "0");
                String etat = fiche.optString("etat", "Inconnu");

                // Crée une ligne horizontale (LinearLayout)
                LinearLayout ligne = new LinearLayout(this);
                ligne.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                ligne.setOrientation(LinearLayout.HORIZONTAL);
                ligne.setPadding(8, 8, 8, 8);

                // Ajoute 4 TextView : Nom, Mois, Montant, Etat
                ligne.addView(creerCellule(nom));
                ligne.addView(creerCellule(mois));
                ligne.addView(creerCellule(montantValide + " €"));
                ligne.addView(creerCellule(etat));

                containerFiches.addView(ligne);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur d'affichage des fiches", Toast.LENGTH_LONG).show();
        }
    }

    private TextView creerCellule(String texte) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(params);
        textView.setText(texte);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
