package com.example.fypwebhost;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvid, tvtitle, tvdescription;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Initializing Views
        tvid = findViewById(R.id.txtid);
        tvtitle = findViewById(R.id.txttitle);
        tvdescription = findViewById(R.id.txtdescription);


        Intent intent =getIntent();
        position = intent.getExtras().getInt("position");
        tvid.setText("postId: "+Add_data_activity.postDetailArrayList.get(position).getPostId());
        tvtitle.setText("Title: "+Add_data_activity.postDetailArrayList.get(position).getPostTitle());
        tvdescription.setText(""+Add_data_activity.postDetailArrayList.get(position).getPostDescription());





    }
}