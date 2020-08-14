package com.example.fypwebhost;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
public class ClassWork extends Fragment {


    String classCode, userType, classID, studentID;
    AssignmentModelClass assignmentModelClass;
    ListView listView;
    AssignmentAdapter adapter;

    //    ProgressBar progressBar;
    public static ArrayList<AssignmentModelClass> assignmentArrayList = new ArrayList<>();
    //    public static String URL="https://temp321.000webhostapp.com/connect/getAssignmentsInfo.php";
    public static String URL="https://temp321.000webhostapp.com/connect/getAssignmentInfoNew.php";

    String encodedfile, fileName, TAG = "INFO", path;
    int PICKFILE_REQUEST_CODE = 100;
    public ClassWork(String classCode, String userType, String classID, String studentID)
    {
        this.classCode = classCode;
        this.userType = userType;
        this.classID = classID;
        this.studentID = studentID;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_work, container, false);
        listView = view.findViewById(R.id.mylistview);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                assignmentModelClass = assignmentArrayList.get(position);
                Intent intent = new Intent(getContext(), SimilarityResultCosine.class);
                intent.putExtra("classID", classID);
                intent.putExtra("studentID", studentID);
                intent.putExtra("assignmentID", assignmentModelClass.getAssignmentID());
                startActivity(intent);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                assignmentModelClass = assignmentArrayList.get(position);


                Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/txt");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);

                        try {
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select a File to Upload"),
                                    PICKFILE_REQUEST_CODE);
                        } catch (android.content.ActivityNotFoundException ex) {
                            // Potentially direct the user to the Market with a Dialog
                            Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), "Please install a File Manager.", Snackbar.LENGTH_LONG);
                            snackbar.setTextColor(Color.parseColor("#ff0000"));
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            }
        });


        retrieveAssignmentInfo();

        return view;
    }
    public void retrieveAssignmentInfo()
    {
        assignmentArrayList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(success.equals("1")){
//                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(success.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        String assignmentID = object.getString("assignmentID");
                                        String assignmentTitle = object.getString("assignmentTitle");
                                        String assignmentDueDate =object.getString("assignmentDueDate");
                                        String assignmentPostDate =object.getString("assignmentPostDate");

                                        assignmentArrayList.add(
                                                new AssignmentModelClass(assignmentTitle,assignmentDueDate ,assignmentPostDate, assignmentID)
                                        );
                                    }
                                    adapter=new AssignmentAdapter(getActivity() ,assignmentArrayList);
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("classCode", String.valueOf(classCode));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && data != null && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            path = new PathFromUri().getPathFromUri(getContext(), uri);

            fileName = new PathFromUri().getFileName(getContext(),uri);
            uploadFile();
            Log.i(TAG, "data -- " + data.toString());
            Log.i(TAG, "uri -- " + ((Uri) uri).toString());
            Log.i(TAG, "file/path -- " + path);
            Log.i(TAG, "file name -- " + fileName);

//            Intent intent = new Intent(getContext(), SimilarityResultCosine.class);
//        intent.putExtra("classID", classID);
//        intent.putExtra("studentID", studentID);
//        intent.putExtra("assignmentID", assignmentModelClass.getAssignmentID());
//        startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile() {
        try {
            new MultipartUploadRequest(getContext(), UUID.randomUUID().toString(), "https://temp321.000webhostapp.com/connect/newComparing.php")
                    .addFileToUpload(path, "file")
                    .addParameter("fileName", fileName)
                    .addParameter("classID", classID)
                    .addParameter("assignmentID", assignmentModelClass.getAssignmentID())
                    .addParameter("studentID", studentID)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}