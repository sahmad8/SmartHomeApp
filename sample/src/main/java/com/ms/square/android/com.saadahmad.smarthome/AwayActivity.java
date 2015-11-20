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
import android.widget.Toast;

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
    private boolean lockOn;

    private boolean faceRecon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        lockOn=intent.getExtras().getBoolean("lock");
        faceRecon=intent.getExtras().getBoolean("faceRecon");

        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
            initial_away_status=nestData.getBoolean("away");
        }
        catch (JSONException e)
        {
            Log.e(null,"Json exception caught");
        }
        setContentView(R.layout.activity_away);
        away_switch=(Switch) findViewById(R.id.awaySwitch);
        away_switch.setChecked(initial_away_status);
        final Switch awayModeSwitch = away_switch;
        awayModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("CHANGEDDDD");
                try {
                    nestData.put("away", isChecked);
                } catch (JSONException e) {
                    Log.e(null, "Some Json exception");
                }
                if(awayModeSwitch.isChecked()) {
                    new NestAwayAsyncTask().execute("on");
                    if (lockOn == false) {
                        new OkidokeysSetLockAsyncTask().execute("lockOn");
                        lockOn = true;
                    }
                    Toast.makeText(getBaseContext(), "Away mode is set", Toast.LENGTH_LONG).show();
                }
                else{
                    new NestAwayAsyncTask().execute("off");
                    Toast.makeText(getBaseContext(), "Home mode is set", Toast.LENGTH_LONG).show();
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
        new DeleteIntruderImageAsyncTask().execute("delete before we begin pinging");
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("nestData", nestData.toString());
        intent.putExtra("lock", lockOn);

        intent.putExtra("faceRecon", faceRecon);

        startActivity(intent);
    }
}
