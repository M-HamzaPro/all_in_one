package com.example.fypwebhost;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapterPost extends ArrayAdapter<PostDetail> {

    Context context;
    List<PostDetail> postDetailList;


    public MyAdapterPost(@NonNull Context context, List<PostDetail> postDetailList) {
        super(context, R.layout.custom_list_item_post,postDetailList);

        this.context = context;
        this.postDetailList = postDetailList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_post,null,true);

        TextView tvID = view.findViewById(R.id.txt_id);
        TextView tvName = view.findViewById(R.id.txt_name);

        tvID.setText(postDetailList.get(position).getPostId());
        tvName.setText(postDetailList.get(position).getPostTitle());

        return view;
    }
}
