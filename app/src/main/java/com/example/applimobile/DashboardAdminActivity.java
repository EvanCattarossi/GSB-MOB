package com.example.applimobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardAdminActivity extends AppCompatActivity {

    Button btnAjoutUtilisateurs, btnFichesTousRoles, btnStatistiquesRemboursement, btnDeconnexionAdmin;
    ImageView logoGSB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        // Liaison des vues
        logoGSB = findViewById(R.id.imageView5);
        btnAjoutUtilisateurs = findViewById(R.id.btnAjoutUtilisateurs);
        btnFichesTousRoles = findViewById(R.id.btnFichesTousRoles);
        btnDeconnexionAdmin = findViewById(R.id.btnDeconnexionAdmin);

        // Action bouton : Ajouter utilisateurs
        btnAjoutUtilisateurs.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdminActivity.this, AjoutUtilisateurActivity.class);
            startActivity(intent);
        });

        // Action bouton : Liste des utilisateurs
        btnFichesTousRoles.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdminActivity.this, ListeUtilisateursTableau.class);
            startActivity(intent);
        });



        // Action bouton : Déconnexion
        btnDeconnexionAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdminActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
