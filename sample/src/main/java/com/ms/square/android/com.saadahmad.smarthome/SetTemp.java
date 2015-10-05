package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Saad Ahmad on 10/2/2015.
 * Class for setting the temperature
 * Works by calling NestAsyncTask and passing an integer to it
 * That will set the target temp on nest thermostat
 */
public class SetTemp extends AppCompatActivity implements View.OnClickListener {
    NumberPicker np;
    Integer setting=75;
    private Button btn;
    private String myresult= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temp);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        btn = (Button) findViewById(R.id.button1);
        np.setMaxValue(78);
        np.setMinValue(75);
        btn.setOnClickListener(this);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
           setting=newVal;
        }
    });
    }


    @Override
    public void onClick(View v) {
        new NestSetAsyncTask().execute(setting.toString());
        Toast.makeText(this, "Set Temperature to "+setting, Toast.LENGTH_LONG).show();
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

}
