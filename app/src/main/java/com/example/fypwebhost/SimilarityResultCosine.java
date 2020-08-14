package com.example.fypwebhost;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class SimilarityResultCosine extends AppCompatActivity {

    String classId, studentID, assignmentID;
    TextView textViewSimilarity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similarity_result_cosine);

        textViewSimilarity = findViewById(R.id.textViewSimilarity);

        classId = getIntent().getStringExtra("classID");
        studentID = getIntent().getStringExtra("studentID");
        assignmentID = getIntent().getStringExtra("assignmentID");

        getResult();
    }

    private void getResult()
    {
        // progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/getResultStudent.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("%"))
                        {
                            //Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                            textViewSimilarity.setText(response);
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

                params.put("classID", classId);
                params.put("studentID", studentID);
                params.put("assignmentID", assignmentID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SimilarityResultCosine.this);
        requestQueue.add(request);
    }
}