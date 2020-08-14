package com.example.fypwebhost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    Button buttonSetChanges, buttonChangePassword;
    EditText editTextName, editTextPassword, editTextOldPassword, editTextConfirmPassword;
    TextView textViewChangePassword;
    TextInputLayout grpName, grpPassword, grpConfirmPassword, grpOldPassword;
    SharedPreferences prefs;
    String loginEmail, userId, userName, userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        buttonChangePassword = findViewById(R.id.buttonSetPassword);
        textViewChangePassword = findViewById(R.id.textViewChangePassword);
        textViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                grpPassword.setVisibility(View.VISIBLE);
                grpConfirmPassword.setVisibility(View.VISIBLE);
                grpOldPassword.setVisibility(View.VISIBLE);
                buttonChangePassword.setVisibility(View.VISIBLE);

                grpName.setVisibility(View.INVISIBLE);
                textViewChangePassword.setVisibility(View.INVISIBLE);
                buttonSetChanges.setVisibility(View.INVISIBLE);
            }
        });

        grpConfirmPassword = findViewById(R.id.grpConfirmPassword);
        grpName = findViewById(R.id.grpName);
        grpPassword = findViewById(R.id.grpPassword);
        grpOldPassword = findViewById(R.id.grpOldPassword);

        grpPassword.setVisibility(View.INVISIBLE);
        grpConfirmPassword.setVisibility(View.INVISIBLE);
        grpOldPassword.setVisibility(View.INVISIBLE);
        buttonChangePassword.setVisibility(View.INVISIBLE);

        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextName = findViewById(R.id.editTextNewName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");
        userId = prefs.getString("userId", "");
        userName = prefs.getString("name", "");
        userPassword = prefs.getString("password", "");

//        name = getIntent().getStringExtra("name");
//        password = getIntent().getStringExtra("password");
//        userId = getIntent().getStringExtra("id");

        editTextName.setText(userName);


        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "hgghg", Toast.LENGTH_SHORT).show();
                passwordChange();
            }
        });

        buttonSetChanges = findViewById(R.id.buttonSetChanges);
        buttonSetChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameChange();
            }
        });
    }


    
    private void nameChange() {

        final String newName = editTextName.getText().toString().trim();
        final String newPassword = editTextPassword.getText().toString().trim();

        if(newName.isEmpty())
        {
            editTextName.setError("This field is mandatory");
            editTextPassword.setError("This field is mandatory");
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            // progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/changeProfile.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("Name updated"))
                            {
                                Toast.makeText(getApplicationContext(), "Name updated", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Login Again to see updated content", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent1 = new Intent(getApplicationContext(), login.class);
                                startActivity(intent1);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("id", userId);
                    params.put("name", newName);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.add(request);
        }
    }

    private void passwordChange() {


        final String newPassword = editTextPassword.getText().toString().trim();
        final String newPasswordConfirm = editTextConfirmPassword.getText().toString().trim();
        final String oldPassword = editTextOldPassword.getText().toString().trim();

        if(newPassword.isEmpty() || newPasswordConfirm.isEmpty() || oldPassword.isEmpty())
        {
            editTextOldPassword.setError("This field is mandatory");
            editTextConfirmPassword.setError("This field is mandatory");
            editTextPassword.setError("This field is mandatory");
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }

        else
        {
            // progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/changePassword.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("Password updated"))
                            {
                                Toast.makeText(getApplicationContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Login Again to see updated content", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent1 = new Intent(getApplicationContext(), login.class);
                                startActivity(intent1);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("id", userId);
                    params.put("newPassword", newPassword);
                    params.put("oldPassword", oldPassword);
                    params.put("confirmPassword", newPasswordConfirm);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.add(request);
        }
    }
}