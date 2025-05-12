package com.example.applimobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

class RefuserFichesActivity extends AppCompatActivity {

    private EditText inputCommentaireRefus;
    private Button btnValiderRefus;

    private static final String URL_REFUSER_FICHE = "http://192.168.0.24/gsb-api/fiches/refus.php"; //

    private int idFiche; // On suppose que l'ID de la fiche à refuser est transmis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refus_commentaire); // adapte si ton XML a un autre nom

        inputCommentaireRefus = findViewById(R.id.inputCommentaireRefus);
        btnValiderRefus = findViewById(R.id.btnValiderRefus);

        // Récupérer l'ID de la fiche à refuser passé depuis l'autre activité
        idFiche = getIntent().getIntExtra("idFiche", -1);

        btnValiderRefus.setOnClickListener(v -> validerRefus());
    }

    private void validerRefus() {
        String commentaire = inputCommentaireRefus.getText().toString().trim();

        if (TextUtils.isEmpty(commentaire)) {
            Toast.makeText(this, "Veuillez saisir un commentaire.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idFiche == -1) {
            Toast.makeText(this, "Erreur fiche inconnue.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject params = new JSONObject();
            params.put("idFiche", idFiche);
            params.put("commentaire", commentaire);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL_REFUSER_FICHE,
                    response -> {
                        Toast.makeText(this, "Fiche refusée avec succès !", Toast.LENGTH_LONG).show();
                        finish(); // Ferme l'écran et revient
                    },
                    error -> {
                        Toast.makeText(this, "Erreur refus fiche : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                public byte[] getBody() {
                    return params.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show();
        }
    }
}
