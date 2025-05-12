package com.example.applimobile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
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

public class ListeUtilisateursTableau extends AppCompatActivity {

    private LinearLayout tableauContainer;
    private static final String URL_LISTE_UTILISATEURS = "http://192.168.0.24/gsb-api/utilisateurs/get_all.php"; // Mets ici ton URL correcte de ton API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_utilisateurs_tableau);

        tableauContainer = findViewById(R.id.tableauContainer);

        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_LISTE_UTILISATEURS,
                null,
                response -> afficherUtilisateurs(response),
                error -> Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }

    private void afficherUtilisateurs(JSONArray utilisateurs) {
        try {
            for (int i = 0; i < utilisateurs.length(); i++) {
                JSONObject user = utilisateurs.getJSONObject(i);

                String nom = user.optString("nom", "Nom ?");
                String prenom = user.optString("prenom", "Prénom ?");
                String email = user.optString("email", "Email ?");
                String role = user.optString("role", "Rôle ?");

                tableauContainer.addView(creerLigne(nom, prenom, email, role));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur affichage utilisateurs", Toast.LENGTH_LONG).show();
        }
    }

    private LinearLayout creerLigne(String nom, String prenom, String email, String role) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 16, 8, 16);

        row.addView(creerCell(nom, 100));
        row.addView(creerCell(prenom, 100));
        row.addView(creerCell(email, 180));
        row.addView(creerCell(role, 120));

        return row;
    }

    private TextView creerCell(String text, int widthDp) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, getResources().getDisplayMetrics()),
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return tv;
    }
}
