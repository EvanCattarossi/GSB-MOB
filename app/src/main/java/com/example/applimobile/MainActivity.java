package com.example.applimobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ton layout de connexion

        // Lier les éléments XML
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        login = findViewById(R.id.btnLogin);

        // Préparer Volley et la boîte de chargement
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connexion...");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir votre e-mail", Toast.LENGTH_SHORT).show();
            } else if (userPass.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir votre mot de passe", Toast.LENGTH_SHORT).show();
            } else {
                loginRequest(userEmail, userPass);
            }
        });
    }

    private void loginRequest(String userEmail, String userPass) {
        String loginUrl = "http://192.168.0.24/gsb-api/login.php";

        progressDialog.show();

        StringRequest stringRequest = new MyStringRequest(loginUrl, userEmail, userPass);

        requestQueue.add(stringRequest);
    }

    private class MyStringRequest extends StringRequest {

        private final String userEmail;
        private final String userPass;

        public MyStringRequest(String loginUrl, String userEmail, String userPass) {
            super(Method.POST, loginUrl, response -> {
                MainActivity.this.progressDialog.dismiss();
                Log.d("REPONSE_API", response);

                try {
                    JSONObject json = new JSONObject(response);

                    boolean success = json.optBoolean("success", false);
                    if (success) {
                        JSONObject user = json.getJSONObject("utilisateur");

                        String id = user.optString("id", "");
                        String role = user.optString("role", "");
                        String email = user.optString("email", "");

                        // Enregistrer la session
                        SharedPreferences prefs = MainActivity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", id);
                        editor.putString("role", role);
                        editor.putString("email", email);
                        editor.apply();

                        // Redirection
                        switch (role.toLowerCase()) {
                            case "visiteur":
                                MainActivity.this.startActivity(new Intent(MainActivity.this, DashboardVisiteurActivity.class));
                                break;
                            case "comptable":
                                MainActivity.this.startActivity(new Intent(MainActivity.this, DashboardComptableActivity.class));
                                break;
                            case "administrateur":
                                MainActivity.this.startActivity(new Intent(MainActivity.this, DashboardAdminActivity.class));
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Rôle non reconnu : " + role, Toast.LENGTH_LONG).show();
                                return;
                        }

                        MainActivity.this.finish(); // Ferme la page Login

                    } else {
                        String message = json.optString("error", "Identifiants incorrects");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("REPONSE_API", "Erreur JSON : " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Erreur de traitement des données", Toast.LENGTH_LONG).show();
                }
            }, error -> {
                MainActivity.this.progressDialog.dismiss();
                Log.e("REPONSE_API", "Erreur réseau : " + error.toString());
                Toast.makeText(MainActivity.this, "Erreur réseau. Vérifiez votre connexion.", Toast.LENGTH_LONG).show();
            });
            this.userEmail = userEmail;
            this.userPass = userPass;
        }

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
            params.put("email", userEmail);
            params.put("password", userPass);
            return params;
        }
    }
}
