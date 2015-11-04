package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shark89fish on 11/4/15.
 */
public class SetLock extends AppCompatActivity {
    private Intent intent = null;
    private boolean lockOn;
    private JSONObject nestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);
        intent= getIntent();
        lockOn = intent.getExtras().getBoolean("lock");
        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
        }
        catch (JSONException e)
        {
            Log.e(null, "Json exception caught");
        }

        final Switch lockSwitch = (Switch)  findViewById(R.id.lockSwitch);


        if(lockOn){
            lockSwitch.setChecked(true);
        }
        else{
            lockSwitch.setChecked(false);
        }


        lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("LOCK");

                if (lockSwitch.isChecked()) {
                    new OkidokeysSetLockAsyncTask().execute("lockOn");
                    Toast.makeText(getBaseContext(), "Door Locked", Toast.LENGTH_LONG).show();
                    lockOn = true;
                } else {
                    new OkidokeysSetLockAsyncTask().execute("lockOff");
                    Toast.makeText(getBaseContext(), "Door Unlocked", Toast.LENGTH_LONG).show();
                    lockOn = false;
                }


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
    public void returnToMain(View v){
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("nestData", nestData.toString());
        intent.putExtra("lock", lockOn);
        startActivity(intent);

    }


}
