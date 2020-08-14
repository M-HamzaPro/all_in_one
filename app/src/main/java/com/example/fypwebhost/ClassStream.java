package com.example.fypwebhost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.SpinKitView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClassStream extends Fragment implements View.OnClickListener {
    EditText editTextDesc,editTextTitle;
    Button postbtn,viewbtn;
    ProgressDialog progressBarNewAPP;
    String userType;
    private SpinKitView spinKitViewNewApp;
    final static String postUrl="https://temp321.000webhostapp.com/connect/post.php";

    public ClassStream(String userType)
    {
        this.userType = userType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_stream, container, false);
        editTextDesc=(EditText)view.findViewById(R.id.postdescription);
        editTextTitle=(EditText)view.findViewById(R.id.posttitle);



        postbtn=(Button)view.findViewById(R.id.btn_post);
        viewbtn=(Button)view.findViewById(R.id.btn_View);
        viewbtn.setOnClickListener(this);
        postbtn.setOnClickListener(this);
        spinKitViewNewApp = (SpinKitView) view.findViewById(R.id.spin_kit);
        progressBarNewAPP = new ProgressDialog(ClassStream.this.getActivity());
        if(userType.contains("0"))
        {
            postbtn.setVisibility(View.INVISIBLE);
            editTextDesc.setVisibility(View.INVISIBLE);
            editTextTitle.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                String descriptionvalue=editTextDesc.getText().toString().trim();
                String titlevalue=editTextTitle.getText().toString().trim();
                if (isNetworkConnected()) {

                    if (!descriptionvalue.isEmpty()&& !titlevalue.isEmpty()) {
                        new Post().execute(descriptionvalue, titlevalue);
                    } else {
                        editTextTitle.requestFocus();
                        editTextTitle.setError("title required.");
                        editTextDesc.requestFocus();
                        editTextDesc.setError("Description required.");}
                } else {
                    Toast.makeText(ClassStream.this.getActivity(), "Internet connection Failed", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_View:
                startActivity(new Intent(getActivity(),Add_data_activity.class));
                break;
            default:
                Toast.makeText(getActivity(), "Nothing pressed", Toast.LENGTH_SHORT).show();
        }
    }
    public class Post extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNewAPP.setMessage("Please wait...");
            progressBarNewAPP.setIndeterminateDrawable(spinKitViewNewApp.getIndeterminateDrawable());
            progressBarNewAPP.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            Response response = null;
            String result = null;
            String desValuee = strings[0];
            String titlvaluee = strings[1];

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder().add("postDescription", desValuee).add("posttitle", titlvaluee)
                        .build();
                Request request = new Request.Builder().url(postUrl).post(formBody).build();
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                } else {
                    result="sockettimeout";
                }

            } catch (IOException e) {

                Looper.prepare();
                Toast.makeText(getActivity(), "network issue", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("Post Submitted")) {
                //  progressBarNewAPP.dismiss();

                final AlertDialog.Builder dialog = new AlertDialog.Builder(ClassStream.this.getActivity()).setMessage("Application Submit Successfully");
                dialog.setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                final AlertDialog alert = dialog.create();
                alert.show();

// Hide after some seconds
                String message = "Post Submitted";
                SpannableString ss = new SpannableString(message);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#48a868")), 0, 14, 0);
                alert.setMessage(ss);
                alert.show();
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            editTextDesc.getText().clear();
                            editTextTitle.getText().clear();
                            alert.dismiss();
                        }
                    }
                };

                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 5000);
            } else  {
                progressBarNewAPP.dismiss();
                Toast.makeText(getActivity(), "socket timeout please try again", Toast.LENGTH_SHORT).show();
            }


        }

    }
    //check the internet connection
    private boolean isNetworkConnected() {
        Context ctx = this.getActivity();
        ConnectivityManager cm = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE));
        ;

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }}
