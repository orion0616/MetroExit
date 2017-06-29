package com.konypro.metroexit;

import android.app.DownloadManager;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity {
    
    String givenlocation;
    String geoendpoint = "http://www.geocoding.jp/api/?v=1.1&q=";
    String geourl;

    String dst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.activity_main);

        Button intentButton = (Button)findViewById(R.id.searchButton);
        intentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText) findViewById(R.id.editText);
                givenlocation = e.getText().toString();
                dst = givenlocation;
                try {
                    givenlocation = URLEncoder.encode(givenlocation, "UTF-8");
                } catch (UnsupportedEncodingException error) {
                    //何か起こるのだろうか
                }
                geourl = geoendpoint + givenlocation;

                Intent intent = new Intent(MainActivity.this, activity2nd.class);
                intent.putExtra("url",geourl);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
