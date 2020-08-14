package com.example.fypwebhost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentAssignment extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    ResultModelClass resultsModelClass;
    ListView listView;
    ResultAdapter adapter;
    public static ArrayList<ResultModelClass> resultArrayList = new ArrayList<>();
    String classID, assignmentID, resultFetcher ="report of student's assignment" ;
    Button buttonGenerateReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);

        listView = findViewById(R.id.lestViewResult);

        classID = getIntent().getStringExtra("classID");
        assignmentID = getIntent().getStringExtra("assignmentID");


        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        buttonGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED)
                    {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, STORAGE_CODE);
                    }
                    else
                    {
                        savePDF();
                    }
                }
                else
                {
                    savePDF();
                }
            }
        });




        Toast.makeText(StudentAssignment.this, "Class Id "+classID+" ass "+assignmentID, Toast.LENGTH_SHORT).show();
        retrieveResult();
    }

    public void retrieveResult()
    {
        resultArrayList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/getResultTeacher.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(success.equals("1")){
//                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(StudentAssignment.this, "1 for success", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    JSONObject object=jsonArray.getJSONObject(i);
                                        String name = object.getString("studentName");
                                        String similarity = object.getString("similarityPercentage");
                                        String email =object.getString("studentEmail");
//                                        String assignmentPostDate =object.getString("assignmentPostDate");
                                    Toast.makeText(StudentAssignment.this, "value "+similarity, Toast.LENGTH_SHORT).show();
                                    resultArrayList.add(
                                            new ResultModelClass(similarity, name, email)
                                    );
                                    adapter=new ResultAdapter(StudentAssignment.this ,resultArrayList);
                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentAssignment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("classID", classID);
                params.put("assignmentID", assignmentID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentAssignment.this);
        requestQueue.add(request);
    }

    private void savePDF()
    {
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis()) + "kok";

        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try
        {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));

            mDoc.open();








            int itemsCount = listView.getChildCount();
            for (int i = 0; i < itemsCount; i++) {
                View view = listView.getChildAt(i);
                String studentName = ((TextView) view.findViewById(R.id.textViewNameStudent)).getText().toString();
                String similarityPer = ((TextView) view.findViewById(R.id.textViewSimilarityPer)).getText().toString();
                String studentEmail = ((TextView) view.findViewById(R.id.textViewEmailStudent)).getText().toString();
                resultFetcher = resultFetcher + "\n" +studentName + "       " + studentEmail + "        " + similarityPer + "%";
            }






            String mText = resultFetcher;

            mDoc.add(new Paragraph(mText));

            mDoc.close();

            Toast.makeText(StudentAssignment.this, mFileName+".pdf\nis saved on\n "+mFilePath, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            Toast.makeText(StudentAssignment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case STORAGE_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    savePDF();
                }
                else
                {
                    Toast.makeText(StudentAssignment.this, "Permission denied...! ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}