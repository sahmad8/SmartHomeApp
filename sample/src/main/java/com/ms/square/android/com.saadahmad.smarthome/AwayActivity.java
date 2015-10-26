package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saad Ahmad on 10/25/2015.
 */
public class AwayActivity extends AppCompatActivity {

    private Switch away_switch=null;
    private boolean initial_away_status;
    private JSONObject nestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
            initial_away_status=nestData.getBoolean("away");
        }
        catch (JSONException e)
        {
            Log.e(null,"Json exception caught");
        }
        setContentView(R.layout.activity_away);
        away_switch=(Switch) findViewById(R.id.awayswitch);
        away_switch.setChecked(initial_away_status);
        away_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              try
              {
                  nestData.put("away",isChecked);
              }
              catch (JSONException e)
              {
                  Log.e(null,"Some Json exception");
              }
                Intent away_intent=new Intent(getBaseContext(), MainActivity.class);
                away_intent.putExtra("nestData", nestData.toString());
                startActivity(away_intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
