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
import android.widget.Switch;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saad Ahmad on 11/4/2015.
 */
public class FacialRecon extends AppCompatActivity {

     private Intent intent;
     private Button face_button;
     private Bundle extras;
     private boolean face_recon;
     private boolean lockOn;
     private JSONObject nestData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recon);
        intent=getIntent();
        extras=intent.getExtras();
        face_recon=extras.getBoolean("faceRecon");
        lockOn = intent.getExtras().getBoolean("lock");
        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
        }
        catch (JSONException e)
        {
            Log.e(null, "Json exception caught");
        }
    final Switch faceSwitch = (Switch)  findViewById(R.id.faceSwitch);
    if(face_recon){
        faceSwitch.setChecked(true);
    }
    else{
        faceSwitch.setChecked(false);
    }
    faceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            System.out.println("FACE");
            if (faceSwitch.isChecked()) {
                Toast.makeText(getBaseContext(), "Facial Recon mode On", Toast.LENGTH_LONG).show();
                face_recon = true;
            } else {
                Toast.makeText(getBaseContext(), "Facial Recon mode Off", Toast.LENGTH_LONG).show();
                face_recon = false;;
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
        intent.putExtra("faceRecon", face_recon);
        intent.putExtra("nestData", nestData.toString());
        intent.putExtra("lock", lockOn);
        startActivity(intent);
    }

}
