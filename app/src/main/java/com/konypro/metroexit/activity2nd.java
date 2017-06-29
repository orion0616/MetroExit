package com.konypro.metroexit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class activity2nd extends ActionBarActivity {



    String consumerKey = "YourOwnConsumerKey";
    String endpoint = "https://api.tokyometroapp.jp/api/v2/";

    String fromlat;
    String fromlon;
    String dstlat;
    String dstlon;

    private String exitName(String s){
        int x=0;
        for(int i=0;i<s.length()-4;i++){
            if(s.charAt(i)=='t' && s.charAt(i+1)=='i' && s.charAt(i+2)=='t'
                    && s.charAt(i+3)=='l' && s.charAt(i+4)=='e'){
                for(int j =4;j<s.length()-i;j++){
                    if(s.charAt(i+4+j)=='"')
                        return s.substring(i+8,i+j+4);
                }
            }
        }
        return "No exit.";
    }

    private String exitLatitude(String s){
        int x=0;
        for(int i=0;i<s.length()-4;i++){
            if(s.charAt(i)=='l' && s.charAt(i+1)=='a' && s.charAt(i+2)=='t'){
                for(int j =3;j<s.length()-i;j++){
                    if(s.charAt(i+2+j)=='"')
                        return s.substring(i+5, i+j+1);
                }
            }
        }
        return "No exit.";
    }

    private String exitLongitude(String s){
        int x=0;
        for(int i=0;i<s.length()-4;i++){
            if(s.charAt(i)=='l' && s.charAt(i+1)=='o' && s.charAt(i+2)=='n'){
                for(int j =3;j<s.length()-i;j++){
                    if(s.charAt(i+2+j)=='"')
                        return s.substring(i+6, i+j+1);
                }
            }
        }
        return "No exit.";
    }

    private void giveExit(String latitude, String longtitude){
        int distance = 2000;
        String metrourl = endpoint + "places?rdf:type=ug:Poi&lon=" + longtitude + "&lat=" + latitude + "&radius=" + String.valueOf(distance) + "&acl:consumerKey=" + consumerKey;
        OkHttpClient client = new OkHttpClient();
//
        Request request = new Request.Builder()
                .url(metrourl)
                .get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView t = (TextView)findViewById(R.id.resultview);
                        t.setText("最寄の出口は" + exitName(responseBody) + "です");
                        fromlat = exitLatitude(responseBody);
                        fromlon = exitLongitude(responseBody);
                    }
                });
            }
        });
    }

    private String giveLat(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='l' && s.charAt(i+1)=='a' && s.charAt(i+2)=='t'){
                int k = i+4;
                for(int j=0;j<s.length()-k;j++){
                    if(s.charAt(k+j)=='<')
                        return s.substring(i+4,k+j-1);
                }
            }
        }
        return "ERROR";
    }

    private String giveLon(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='l' && s.charAt(i+1)=='n' && s.charAt(i+2)=='g'){
                int k = i+4;
                for(int j=0;j<s.length()-k;j++){
                    if(s.charAt(k+j)=='<')
                        return s.substring(i+4,k+j-1);
                }
            }
        }
        return "ERROR";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.activity_activity2nd);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        OkHttpClient client = new OkHttpClient();
//
        Request request = new Request.Builder()
                .url(url)
                .get().build();
//
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dstlat = giveLat(responseBody);
                        dstlon = giveLon(responseBody);
                        giveExit(dstlat, dstlon);
                    }
                });
            }
        });



        Button b = (Button)findViewById(R.id.backButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button mapButton = (Button)findViewById(R.id.rootButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + fromlat + ","
                        + fromlon + "&daddr=" + dstlat + "," + dstlon + "&dirflg=w"));
                startActivity(intent);
            }
        });

    }

}
