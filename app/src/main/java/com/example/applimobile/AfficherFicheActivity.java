package com.example.applimobile;

import static com.example.applimobile.R.id.tableLayoutFiches;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AfficherFicheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_fiches); // nom du layout XML à adapter

        // Récupérer le tableau principal
        TableLayout table = findViewById(tableLayoutFiches); // ajouter un ID au TableLayout dans le XML si besoin

        // Ajouter un clic à chaque ligne de consultation
        for (int i = 1; i < table.getChildCount(); i++) { // commence à 1 pour éviter l'en-tête
            TableRow row = (TableRow) table.getChildAt(i);

            // Dernière colonne = bouton "Consulter >"
            TextView consulterText = (TextView) row.getChildAt(2);
            int finalI = i;

            consulterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mois = ((TextView) row.getChildAt(0)).getText().toString();
                    String montant = ((TextView) row.getChildAt(1)).getText().toString();
                    String etat = consulterText.getText().toString();

                    // Affichage simple pour l'exemple
                    Toast.makeText(
                            AfficherFicheActivity.this,
                            "Fiche " + mois + " sélectionnée\nMontant : " + montant,
                            Toast.LENGTH_SHORT
                    ).show();

                    // Ici tu peux démarrer une activité avec les détails :
                    // Intent intent = new Intent(ConsulterFichesActivity.this, DetailFicheActivity.class);
                    // intent.putExtra("mois", mois);
                    // startActivity(intent);
                }
            });
        }
    }
}
