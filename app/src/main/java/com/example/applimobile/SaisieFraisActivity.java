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

import org.json.JSONObject;

public class SaisieFraisActivity extends AppCompatActivity {

    EditText inputDateFiche, inputKm, inputNuitee, inputRepas;
    EditText inputDateHorsForfait, inputLibelleHorsForfait, inputMontantHorsForfait;
    Button btnAjouterHorsForfait, btnSoumettre;

    RequestQueue requestQueue; // Volley
    private static final String URL_FICHE_FRAIS = "http://192.168.0.24/gsb-api/create.php"; // <-- adapte l'URL de ton API pour envoyer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_frais);

        inputDateFiche = findViewById(R.id.inputDateFiche);
        inputKm = findViewById(R.id.inputKm);
        inputNuitee = findViewById(R.id.inputNuitee);
        inputRepas = findViewById(R.id.inputRepas);

        inputDateHorsForfait = findViewById(R.id.inputDateHorsForfait);
        inputLibelleHorsForfait = findViewById(R.id.inputLibelleHorsForfait);
        inputMontantHorsForfait = findViewById(R.id.inputMontantHorsForfait);

        btnAjouterHorsForfait = findViewById(R.id.btnAjouterHorsForfait);
        btnSoumettre = findViewById(R.id.btnSoumettre);

        requestQueue = Volley.newRequestQueue(this); // initialiser la file Volley

        btnAjouterHorsForfait.setOnClickListener(v -> ajouterFraisHorsForfait());
        btnSoumettre.setOnClickListener(v -> soumettreFicheFrais());
    }

    private void ajouterFraisHorsForfait() {
        String dateHF = inputDateHorsForfait.getText().toString().trim();
        String libelleHF = inputLibelleHorsForfait.getText().toString().trim();
        String montantHF = inputMontantHorsForfait.getText().toString().trim();

        if (TextUtils.isEmpty(dateHF) || TextUtils.isEmpty(libelleHF) || TextUtils.isEmpty(montantHF)) {
            Toast.makeText(this, "Veuillez remplir tous les champs hors forfait", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("type", "hors_forfait");
            data.put("date", dateHF);
            data.put("libelle", libelleHF);
            data.put("montant", montantHF);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_FICHE_FRAIS,
                    data,
                    response -> {
                        Toast.makeText(this, "Frais hors forfait ajouté avec succès !", Toast.LENGTH_SHORT).show();
                        inputDateHorsForfait.setText("");
                        inputLibelleHorsForfait.setText("");
                        inputMontantHorsForfait.setText("");
                    },
                    error -> {
                        Toast.makeText(this, "Erreur ajout frais hors forfait: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show();
        }
    }

    private void soumettreFicheFrais() {
        String dateFiche = inputDateFiche.getText().toString().trim();
        String km = inputKm.getText().toString().trim();
        String nuitee = inputNuitee.getText().toString().trim();
        String repas = inputRepas.getText().toString().trim();

        if (TextUtils.isEmpty(dateFiche) || TextUtils.isEmpty(km)
                || TextUtils.isEmpty(nuitee) || TextUtils.isEmpty(repas)) {
            Toast.makeText(this, "Veuillez remplir tous les frais forfaitisés", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("type", "forfait");
            data.put("date", dateFiche);
            data.put("km", km);
            data.put("nuitee", nuitee);
            data.put("repas", repas);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_FICHE_FRAIS,
                    data,
                    response -> {
                        Toast.makeText(this, "Fiche soumise avec succès !", Toast.LENGTH_SHORT).show();
                        inputDateFiche.setText("");
                        inputKm.setText("");
                        inputNuitee.setText("");
                        inputRepas.setText("");
                    },
                    error -> {
                        Toast.makeText(this, "Erreur soumission fiche: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show();
        }
    }
}
