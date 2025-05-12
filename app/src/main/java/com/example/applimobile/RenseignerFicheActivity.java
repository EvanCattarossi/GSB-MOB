package com.example.applimobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RenseignerFicheActivity extends AppCompatActivity {

    // Champs UI
    private EditText editDateFiche, editKm, editHotel, editRepas;
    private EditText editHorsDate, editHorsLibelle, editHorsMontant;
    private Button btnAjouterHorsForfait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renseigner_fihe); // Corrigé ici

        // Lier les éléments XML
        editDateFiche = findViewById(R.id.editTextDate);
        editKm = findViewById(R.id.editKm);
        editHotel = findViewById(R.id.editHotel);
        editRepas = findViewById(R.id.editRepas);

        editHorsDate = findViewById(R.id.editHorsForfaitDate);
        editHorsLibelle = findViewById(R.id.editHorsForfaitLibelle);
        editHorsMontant = findViewById(R.id.editHorsForfaitMontant);

        btnAjouterHorsForfait = findViewById(R.id.btnAjouterHorsForfait);

        // Bouton action
        btnAjouterHorsForfait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterFraisHorsForfait();
            }
        });
    }

    private void ajouterFraisHorsForfait() {
        String date = editHorsDate.getText().toString();
        String libelle = editHorsLibelle.getText().toString();
        String montantStr = editHorsMontant.getText().toString();

        if (date.isEmpty() || libelle.isEmpty() || montantStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs hors forfait.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double montant = Double.parseDouble(montantStr);

            // Thread pour exécuter l’appel réseau
            new Thread(() -> {
                try {
                    // Remplace cette URL par celle de ton API
                    URL url = new URL("https:///frais");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    // Corps JSON
                    String json = String.format(
                            "{\"date\":\"%s\", \"libelle\":\"%s\", \"montant\":%s}",
                            date, libelle, montantStr
                    );

                    // Envoi du corps JSON
                    OutputStream os = conn.getOutputStream();
                    os.write(json.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    conn.disconnect();

                    runOnUiThread(() -> {
                        if (responseCode == 200 || responseCode == 201) {
                            Toast.makeText(this, "Frais envoyé avec succès !", Toast.LENGTH_LONG).show();
                            editHorsDate.setText("");
                            editHorsLibelle.setText("");
                            editHorsMontant.setText("");
                        } else {
                            Toast.makeText(this, "Erreur serveur : " + responseCode, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Erreur réseau : " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Montant invalide", Toast.LENGTH_SHORT).show();
        }
    }
}
