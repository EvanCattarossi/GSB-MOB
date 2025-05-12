package com.example.applimobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardVisiteurActivity extends AppCompatActivity {

    private Button btnNewFiche, btnViewFiches, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_visiteur); // nom du layout à adapter si différent

        // Liaison avec les éléments du layout
        btnNewFiche = findViewById(R.id.btnNewFiche);
        btnViewFiches = findViewById(R.id.btnViewFiches);
        btnLogout = findViewById(R.id.btnLogout);

        // Navigation vers l’activité de saisie de fiche
        btnNewFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardVisiteurActivity.this, RenseignerFicheActivity.class);
                startActivity(intent);
            }
        });

        // Navigation vers l’activité de consultation de fiches
        btnViewFiches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardVisiteurActivity.this, AfficherFicheActivity.class);
                startActivity(intent);
            }
        });

        // Action de déconnexion (exemple simple : retour à MainActivity ou LoginActivity)
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardVisiteurActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // vide le stack
                startActivity(intent);
                finish();
            }
        });
    }
}
