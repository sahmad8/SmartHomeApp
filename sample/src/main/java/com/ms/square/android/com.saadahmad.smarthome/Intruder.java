package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import android.os.Vibrator;

/**
 * Created by Saad Ahmad on 10/19/2015.
 */
public class Intruder extends AppCompatActivity implements View.OnClickListener {

    private EditText value;
    private Button save_button;
    public String myresult=null;
    private ImageView imageView;
    private Bitmap bmp=null;
    private boolean lockOn;
    private boolean faceRecon;
    private JSONObject nestData;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Vibrator v = (Vibrator) this.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(2000);

        Intent intent = getIntent();
        setContentView(R.layout.activity_intruder);
        lockOn=intent.getExtras().getBoolean("lock");
        faceRecon=intent.getExtras().getBoolean("faceRecon");
        try{
            nestData=new JSONObject(intent.getExtras().getString("nestData"));
        }
        catch(JSONException e)
        {

        }
        value = (EditText) findViewById(R.id.editText1);
        //btn = (Button) findViewById(R.id.button2);
        imageView = (ImageView) findViewById(R.id.imageSwitcher);
        save_button= (Button) findViewById(R.id.button_save);
        save_button.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        new MyAsyncTask().execute("get the image");
        while (myresult==null)
        {

        }
        imageView.setImageBitmap(bmp);
        new DeleteIntruderImageAsyncTask().execute("delete image");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a message when button is clicked. If user inputs(atleast 1 character, otherwise toast is displayed) and clicks  the input to the database.
     * Executes asynchronous task, sends name string
     *
     * @param view
     */
    public void onClick(View view) {

            MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Intruder", "Image taken when home status was sset to away");
            Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show();
        }

    /**
     * Asynchornous subclass, MyAsyncTask
     * extends asynchronouse task
     * overrides background method and execuutes http actions
     */
    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

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
            try {
                    InputStream in = new URL("http://128.83.52.253:8079/imagetest.py/showImage").openStream();
                    bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(null, "caught exception something went wrong, this should not happen since we were guaranteed the image was there");
                // log error
            }
            myresult="something";
            return null;
        }
    }

}
