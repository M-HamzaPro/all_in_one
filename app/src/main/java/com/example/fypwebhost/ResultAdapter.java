package com.example.fypwebhost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<ResultModelClass> {

    Context context;
    String[] arr;
    int i = 0;
    List<ResultModelClass> arrayListResults;

    public ResultAdapter(@NonNull Context context, List<ResultModelClass> arrayListResults) {
        super(context, R.layout.custom_list_rresults, arrayListResults);

        this.context = context;
        this.arrayListResults = arrayListResults;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_rresults, null,true);


        TextView textViewSimilarityPer = view.findViewById(R.id.textViewSimilarityPer);
        TextView textViewNameStudent = view.findViewById(R.id.textViewNameStudent);
        TextView textViewEmailStudent = view.findViewById(R.id.textViewEmailStudent);


        textViewNameStudent.setText(arrayListResults.get(position).getStudentName());
        textViewSimilarityPer.setText(arrayListResults.get(position).getSimilarity());
        textViewEmailStudent.setText(arrayListResults.get(position).getStudentEmail());

        return view;
    }
}
