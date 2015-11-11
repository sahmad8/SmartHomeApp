package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Saad Ahmad on 11/10/2015.
 */
public class Unrecognized extends AppCompatActivity {


    private boolean lockOn;
    private boolean faceRecon;
    private JSONObject nestData;
    private Bitmap bmp;
    private boolean recieved=false;
    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unrecognized);
        imageView=(ImageView) findViewById(R.id.imageUnrecognized);
        Vibrator v = (Vibrator) this.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(2000);
        Toast.makeText(getBaseContext(), "Unrecognized person at your door", Toast.LENGTH_LONG);
        Intent intent = getIntent();
        lockOn=intent.getExtras().getBoolean("lockOn");
        faceRecon=intent.getExtras().getBoolean("faceRecon");
        try {
            nestData=new JSONObject(intent.getExtras().getString("nestData"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        new UnknownImageAsyncTask().execute("get image");
        while (!recieved) {
            //spin lock if recieved is false;
        }
        imageView.setImageBitmap(bmp);

    }

    public void allowUnkown(View v){
        if (lockOn)    //if door is locked, then only we need to toggle the lock.
        {
            new OkidokeysSetLockAsyncTask().execute("unlock");
            Toast.makeText(getBaseContext(), "Door Unlocked.", Toast.LENGTH_LONG);
            lockOn=false;
            return;
        }
        Toast.makeText(getBaseContext(),"Door is already Unlocked!!", Toast.LENGTH_LONG);
    }

    public void returnToMainFromUnrecognized(View v){
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("nestData", nestData.toString());
        intent.putExtra("lock", lockOn);
        intent.putExtra("faceRecon", faceRecon);
        startActivity(intent);
    }

    /**
     * Asynchornous subclass
     * extends asynchronouse task
     * overrides background method and executes http actions (for getting uknown image)
     */
    private class UnknownImageAsyncTask extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            final String returned=postData(params[0]);
            return null;
        }

        /**
         * onPosttExecute-displays toast upon http execute completion
         * exits application
         * @param result
         */
        protected void onPostExecute(Void result){
            //Toast.makeText(null, response.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Unknown person at your door", Toast.LENGTH_LONG).show();
            this.postData(null);
        }

        /**
         * onProgressUpdate-
         * updates progress bar
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {

        }

        /**
         * poData-handles http actions
         * called when user clicks submit-then asynchronous task is created and calls this method
         * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
         * @param valueIWantToSend
         */
        public String postData(String valueIWantToSend) {
            // Create new HttpClient and HTTPPOST
            recieved=false;
            try {
                    InputStream in = new URL("http://128.83.52.253:8079/imagetest.py/faceUknown").openStream();
                    bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(null, "caught exception something went wrong, this should not happen since we were guaranteed the image was there");
                // log error
            }
            recieved=true;
            return null;
        }
    }

}
