package com.example.fypwebhost;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class jaro_winkler_algorithm extends AppCompatActivity {

    EditText str1, str2;
    TextView result, result2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaro_winkler_algorithm);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        str1 = findViewById(R.id.str1);
        str2 = findViewById(R.id.str2);
        result = findViewById(R.id.result);
        result2 = findViewById(R.id.result2);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    Toast.makeText(jaro_winkler_algorithm.this, "Permission granted!", Toast.LENGTH_SHORT).show();

                } else {
                    //If user presses deny
                    Toast.makeText(jaro_winkler_algorithm.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check(View view) {

        String S1, S2;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1011);
        } else {
            S1 = getTextFromUrl("https://textplagiarismsql.000webhostapp.com/File1.txt");
            str1.setText(S1);
            S2 = getTextFromUrl("https://textplagiarismsql.000webhostapp.com/File2.txt");
            str2.setText(S2);

            double myres = jaro_distance(S1, S2);
            double percent = myres * 100;
            String s = String.format("%.2f", percent);
            result.setText("The Jaro Distance Will be=" + s + " %");

            double my = jaro_Winkler(S1, S2);
            double per = my * 100;
            String s1 = String.format("%.2f", per);
            result2.setText("The Jaro Winkler Will be=" + s1 + " %");


        }


    }

    // Java implementation of above approach
    // Function to calculate the
    // Jaro Similarity of two strings
    static double jaro_distance(String s1, String s2) {
        // If the strings are equal
        if (s1 == s2)
            return 1.0;

        // Length of two strings
        int len1 = s1.length(),
                len2 = s2.length();

        if (len1 == 0 || len2 == 0)
            return 0.0;

        // Maximum distance upto which matching
        // is allowed
        int max_dist = (int) Math.floor(Math.max(len1, len2) / 2) - 1;

        // Count of matches
        int match = 0;

        // Hash for matches
        int hash_s1[] = new int[s1.length()];
        int hash_s2[] = new int[s2.length()];

        // Traverse through the first string
        for (int i = 0; i < len1; i++) {

            // Check if there is any matches
            for (int j = Math.max(0, i - max_dist);
                 j < Math.min(len2, i + max_dist + 1); j++)

                // If there is a match
                if (s1.charAt(i) == s2.charAt(j) &&
                        hash_s2[j] == 0) {
                    hash_s1[i] = 1;
                    hash_s2[j] = 1;
                    match++;
                    break;
                }
        }

        // If there is no match
        if (match == 0)
            return 0.0;

        // Number of transpositions
        double t = 0;

        int point = 0;

        // Count number of occurances
        // where two characters match but
        // there is a third matched character
        // in between the indices
        for (int i = 0; i < len1; i++)
            if (hash_s1[i] == 1) {

                // Find the next matched character
                // in second string
                while (hash_s2[point] == 0)
                    point++;

                if (s1.charAt(i) != s2.charAt(point++))
                    t++;
            }

        t /= 2;

        // Return the Jaro Similarity
        return (((double) match) / ((double) len1)
                + ((double) match) / ((double) len2)
                + ((double) match - t) / ((double) match))
                / 3.0;
    }

    // Jaro Winkler Similarity
    static double jaro_Winkler(String s1, String s2) {
        double jaro_dist = jaro_distance(s1, s2);

        // If the jaro Similarity is above a threshold
        if (jaro_dist > 0.7) {

            // Find the length of common prefix
            int prefix = 0;

            for (int i = 0;
                 i < Math.min(s1.length(), s2.length()); i++) {

                // If the characters match
                if (s1.charAt(i) == s2.charAt(i))
                    prefix++;

                    // Else break
                else
                    break;
            }

            // Maximum of 4 characters are allowed in prefix
            prefix = Math.min(4, prefix);

            // Calculate jaro winkler Similarity
            jaro_dist += 0.1 * prefix * (1 - jaro_dist);
        }
        return jaro_dist;
    }


// This code is contributed by AnkitRai01


    private String getTextFromUrl(String URL) {
        String link = URL;
        ArrayList<String> al = new ArrayList<>();

        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    al.add(line);
                }
            } finally {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return al.get(0).toString();
    }
}