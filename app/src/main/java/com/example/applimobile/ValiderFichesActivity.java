package com.example.applimobile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

class ValiderFichesActivity extends AppCompatActivity {

    private LinearLayout containerLayout;
    private static final String URL_FICHES_ACTION = "http://192.168.0.24/gsb-api/getFichesVisiteur.php"; // adapte ton URL ici

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valider_fiches_comptables); // adapte le nom de ton fichier XML

        containerLayout = (LinearLayout) ((ScrollView) findViewById(R.id.scrollView)).getChildAt(0);

        chargerFiches();
    }

    private void chargerFiches() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_FICHES_ACTION,
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

                // Créer une ligne horizontale
                LinearLayout ligne = new LinearLayout(this);
                ligne.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                ligne.setOrientation(LinearLayout.HORIZONTAL);
                ligne.setPadding(8, 8, 8, 8);

                // Ajouter Nom
                ligne.addView(creerCellule(nom, 120));
                // Ajouter Mois
                ligne.addView(creerCellule(mois, 100));
                // Ajouter Montant
                ligne.addView(creerCellule(montantValide + " €", 100));
                // Ajouter Actions
                ligne.addView(creerActionCellule(nom));

                // Ajouter cette ligne au container principal
                containerLayout.addView(ligne);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur affichage fiches", Toast.LENGTH_LONG).show();
        }
    }

    private TextView creerCellule(String texte, int largeurDp) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (largeurDp * getResources().getDisplayMetrics().density),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(params);
        textView.setText(texte);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private LinearLayout creerActionCellule(String nom) {
        LinearLayout actionsLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (200 * getResources().getDisplayMetrics().density),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        actionsLayout.setLayoutParams(params);
        actionsLayout.setOrientation(LinearLayout.HORIZONTAL);
        actionsLayout.setGravity(Gravity.CENTER);

        // Bouton Rembourser
        Button btnRembourser = new Button(this);
        btnRembourser.setText("Rembourser");
        btnRembourser.setTextColor(Color.WHITE);
        btnRembourser.setTextSize(12);
        btnRembourser.setBackgroundResource(R.drawable.rounded_green_button);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(90, 40);
        btnParams.setMargins(0, 0, 8, 0);
        btnRembourser.setLayoutParams(btnParams);

        btnRembourser.setOnClickListener(v -> {
            Toast.makeText(this, "Fiche remboursée pour " + nom, Toast.LENGTH_SHORT).show();
            // Tu peux ici appeler ton API pour rembourser la fiche
        });

        // Bouton Refuser
        Button btnRefuser = new Button(this);
        btnRefuser.setText("Refuser");
        btnRefuser.setTextColor(Color.WHITE);
        btnRefuser.setTextSize(12);
        btnRefuser.setBackgroundResource(R.drawable.rounded_red_button);
        btnRefuser.setLayoutParams(new LinearLayout.LayoutParams(90, 40));

        btnRefuser.setOnClickListener(v -> {
            Toast.makeText(this, "Fiche refusée pour " + nom, Toast.LENGTH_SHORT).show();
            // Tu peux ici appeler ton API pour refuser la fiche
        });

        actionsLayout.addView(btnRembourser);
        actionsLayout.addView(btnRefuser);

        return actionsLayout;
    }
}
