package com.example.applimobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

class DetailFicheFraisActivity extends AppCompatActivity {

    private TextView textKM, textNuitee, textRepas;
    private LinearLayout containerHorsForfait;
    private Button btnModifierFiche;

    private static final String URL_DETAIL_FICHE = "http://192.168.0.24/gsb-api/fiches/get_by_id.php"; // <-- Mets ton vrai URL ici
    private int idFiche; // On suppose que tu passes l'ID de la fiche à cette activité

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_frais); // adapte si besoin

        textKM = findViewById(R.id.textView);
        textNuitee = findViewById(R.id.textView);
        textRepas = findViewById(R.id.inputRepas);
        containerHorsForfait = findViewById(R.id.btnAjouterHorsForfait);
        btnModifierFiche = findViewById(R.id.btnModifierFiche);

        idFiche = getIntent().getIntExtra("idFiche", -1);

        if (idFiche != -1) {
            chargerDetailsFiche();
        } else {
            Toast.makeText(this, "Erreur: ID fiche non trouvé", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnModifierFiche.setOnClickListener(v -> {
            Intent intent = new Intent(DetailFicheFraisActivity.this, ModifierFicheActivity.class);
            intent.putExtra("idFiche", idFiche);
            startActivity(intent);
        });
    }

    private void chargerDetailsFiche() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_DETAIL_FICHE + "?id=" + idFiche, // passe l'id de la fiche en paramètre
                null,
                response -> afficherDetails(response),
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erreur chargement fiche", Toast.LENGTH_LONG).show();
                }
        );

        queue.add(request);
    }

    private void afficherDetails(JSONObject fiche) {
        try {
            // Récupérer les frais forfaitisés
            double km = fiche.optDouble("km", 0);
            double nuitee = fiche.optDouble("nuitee", 0);
            double repas = fiche.optDouble("repas", 0);

            textKM.setText("KM : " + km + " x 0,62 € = " + String.format("%.2f", km * 0.62) + " €");
            textNuitee.setText("Nuitée : " + nuitee + " x 80,00 € = " + String.format("%.2f", nuitee * 80) + " €");
            textRepas.setText("Repas : " + repas + " x 25,00 € = " + String.format("%.2f", repas * 25) + " €");

            // Supprimer tous les anciens frais hors forfait
            containerHorsForfait.removeAllViews();

            // Frais hors forfait
            JSONArray horsForfaits = fiche.optJSONArray("hors_forfait");

            if (horsForfaits != null) {
                for (int i = 0; i < horsForfaits.length(); i++) {
                    JSONObject hf = horsForfaits.getJSONObject(i);

                    String date = hf.optString("date", "??");
                    String libelle = hf.optString("libelle", "??");
                    double montant = hf.optDouble("montant", 0);

                    TextView textView = new TextView(this);
                    textView.setText(date + " | " + libelle + " | " + String.format("%.2f", montant) + " €");
                    containerHorsForfait.addView(textView);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur d'affichage fiche", Toast.LENGTH_LONG).show();
        }
    }
}
