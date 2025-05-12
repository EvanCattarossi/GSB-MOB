package com.example.applimobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardComptableActivity extends AppCompatActivity {

    Button btnConsulterFiches, btnDeconnexion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_comptable);

        btnConsulterFiches = findViewById(R.id.btnConsulterFiches);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);

        btnConsulterFiches.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListesFichesComptableActivity.class);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.applimobile.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
