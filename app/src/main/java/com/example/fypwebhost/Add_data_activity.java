package com.example.fypwebhost;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_data_activity extends AppCompatActivity {
    ListView listView;
    MyAdapterPost adapter;
    public static ArrayList<PostDetail> postDetailArrayList = new ArrayList<>();
    String url = "https://temp321.000webhostapp.com/connect/retrieve.php";
    PostDetail employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_activity);
        listView = findViewById(R.id.myListView);
        getSupportActionBar().hide();
        adapter = new MyAdapterPost(this, postDetailArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View Post","Delete post"};
                builder.setTitle(postDetailArrayList.get(position).getPostTitle());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){

                            case 0:
                                startActivity(new Intent(getApplicationContext(),DetailActivity.class)
                                        .putExtra("position",position));
                                break;

                            case 1:
                                deleteData(postDetailArrayList.get(position).getPostId());

                                break;


                        }
                    }
                });
                builder.create().show();
            }
        });
        retrieveData();

    }

    private void deleteData(final String id) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/deletepost.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(Add_data_activity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Add_data_activity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Add_data_activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void retrieveData() {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                postDetailArrayList.clear();
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(sucess.equals("1")){


                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("postId");
                            String title = object.getString("postTitle");
                            String description = object.getString("postDescription");

                            employee = new PostDetail(id,title,description);
                            postDetailArrayList.add(employee);
                            adapter.notifyDataSetChanged();



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
                Toast.makeText(Add_data_activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}