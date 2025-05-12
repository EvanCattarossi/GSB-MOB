package com.example.applimobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AjoutUtilisateurActivity extends AppCompatActivity {

    EditText inputNom, inputPrenom, inputEmail, inputPassword;
    Spinner spinnerRole;
    Button btnCreerUtilisateur;

    private static final String URL_AJOUT_UTILISATEUR = "http://192.168.0.24/gsb-api/utilisateurs/create.php"; // Mets ici ton URL API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_utilisateur);

        inputNom = findViewById(R.id.inputNom);
        inputPrenom = findViewById(R.id.inputPrenom);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnCreerUtilisateur = findViewById(R.id.btnCreerUtilisateur);

        // Adapter pour les rôles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"visiteur", "comptable", "administrateur"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnCreerUtilisateur.setOnClickListener(v -> ajouterUtilisateur());
    }

    private void ajouterUtilisateur() {
        String nom = inputNom.getText().toString().trim();
        String prenom = inputPrenom.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject params = new JSONObject();
            params.put("nom", nom);
            params.put("prenom", prenom);
            params.put("email", email);
            params.put("password", password);
            params.put("role", role);

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_AJOUT_UTILISATEUR,
                    params,
                    response -> {
                        boolean success = response.optBoolean("success", false);
                        if (success) {
                            Toast.makeText(this, "Utilisateur ajouté avec succès", Toast.LENGTH_LONG).show();
                            finish(); // Ferme la page après succès
                        } else {
                            String error = response.optString("error", "Erreur inconnue");
                            Toast.makeText(this, "Erreur: " + error, Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            );

            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show();
        }
    }
}
