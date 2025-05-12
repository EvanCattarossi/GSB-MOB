package com.example.applimobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ModifierFicheActivity extends AppCompatActivity {

    EditText editKM, editNL, editREP;
    EditText editDate1, editLibelle1, editMontant1;
    EditText editDate2, editLibelle2, editMontant2;
    Button btnEnregistrer;

    private static final String URL_MODIFIER_FICHE = "http://192.168.0.24/gsb-api/fiches/update.php";
    private int idFiche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_fiche);

        editKM = findViewById(R.id.editKM);
        editNL = findViewById(R.id.editNL);
        editREP = findViewById(R.id.editREP);

        editDate1 = findViewById(R.id.editDate1);
        editLibelle1 = findViewById(R.id.editLibelle1);
        editMontant1 = findViewById(R.id.editMontant1);

        editDate2 = findViewById(R.id.editDate2);
        editLibelle2 = findViewById(R.id.editLibelle2);
        editMontant2 = findViewById(R.id.editMontant2);

        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        idFiche = getIntent().getIntExtra("idFiche", -1);
        if (idFiche == -1) {
            Toast.makeText(this, "Erreur: ID fiche introuvable", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnEnregistrer.setOnClickListener(v -> enregistrerModifications());
    }

    private void enregistrerModifications() {
        String km = editKM.getText().toString().trim();
        String nl = editNL.getText().toString().trim();
        String rep = editREP.getText().toString().trim();

        if (TextUtils.isEmpty(km) || TextUtils.isEmpty(nl) || TextUtils.isEmpty(rep)) {
            Toast.makeText(this, "Veuillez remplir les frais forfaitisés", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject params = new JSONObject();
            params.put("idFiche", idFiche);
            params.put("km", km);
            params.put("nuitee", nl);
            params.put("repas", rep);

            // Frais hors forfait (en tableau)
            JSONArray horsForfaitArray = new JSONArray();

            JSONObject hf1 = new JSONObject();
            hf1.put("date", editDate1.getText().toString().trim());
            hf1.put("libelle", editLibelle1.getText().toString().trim());
            hf1.put("montant", editMontant1.getText().toString().trim());
            horsForfaitArray.put(hf1);

            JSONObject hf2 = new JSONObject();
            hf2.put("date", editDate2.getText().toString().trim());
            hf2.put("libelle", editLibelle2.getText().toString().trim());
            hf2.put("montant", editMontant2.getText().toString().trim());
            horsForfaitArray.put(hf2);

            params.put("hors_forfait", horsForfaitArray);

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_MODIFIER_FICHE,
                    params,
                    response -> {
                        boolean success = response.optBoolean("success", false);
                        if (success) {
                            Toast.makeText(this, "Modifications enregistrées avec succès !", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String error = response.optString("error", "Erreur inconnue");
                            Toast.makeText(this, "Erreur: " + error, Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Erreur réseau", Toast.LENGTH_LONG).show();
                    }
            );

            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show();
        }
    }
}
