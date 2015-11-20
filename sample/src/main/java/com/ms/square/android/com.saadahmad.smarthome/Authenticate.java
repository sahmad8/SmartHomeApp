package com.ms.square.android.com.saadahmad.smarthome;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import android.media.MediaPlayer;

/**
 * Created by Saad Ahmad on 8/28/2015.
 * Authenticate class
 * After valid password entry, performs ASNYC TASK of getting thermostat info.
 * Then starts MainActivity after recieving info.
 */
public class Authenticate extends ActionBarActivity implements View.OnClickListener {

    private final String password = "xilinx";
    private EditText value;
    private Button btn;
    private ProgressBar pb;
    HttpResponse response;
    public String myresult=null;
    private ImageView imageView;
    private Bitmap bmp=null;
    private boolean lockOn;


    /**onCreate Method
     * Initializes the Activity values and creates the text field and button.
     * Recieves intent from game test activity (thescore)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        lockOn = false;
        // playerscore = intent.getIntExtra("thescore", 0);
        setContentView(R.layout.activity_authenticate);
        value = (EditText) findViewById(R.id.editText1);
        btn = (Button) findViewById(R.id.button1);
        imageView = (ImageView) findViewById(R.id.i1);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        btn.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        new HomeStatusResetAsyncTask().execute("Reset home b4 we start demoing");
        new DeleteIntruderImageAsyncTask().execute("Delete image before we start showing the app");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a message when button is clicked. If user inputs(atleast 1 character, otherwise toast is displayed) and clicks  the input to the database.
     * Executes asynchronous task, sends name string
     *
     * @param view
     */
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (!value.getText().toString().equals(password)) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
        }
        else
        {
            pb.setVisibility(View.VISIBLE);
            new MyAsyncTask().execute(value.getText().toString());                 //commented out since we dont have a getTemp method setup on server
            while (myresult==null){

            }
            //myresult now has json string. BTW, i just passed the json string instead of objject to the next activity
            //just use the same method as below to extract info from the string using json object class.
            JSONObject json= null;
            try {
                json = new JSONObject(myresult);
                Toast.makeText(this, json.getString("temperature"), Toast.LENGTH_LONG).show();  //for debugging, js toast the temp.
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
            final Intent intent=new Intent(getBaseContext(), MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("nestData", myresult);
            extras.putBoolean("lock", lockOn);
            extras.putBoolean("faceRecon", false); ///default at start of app to false.
            intent.putExtras(extras);
            startActivity(intent);
            //imageView.setImageBitmap(bmp);
        }
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
            //Toast.makeText(null, response.toString(), Toast.LENGTH_LONG).show();
        }

        /**
         * onProgressUpdate-
         * updates progress bar
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
        }

        /**
         * poData-handles http actions
         * called when user clicks submit-then asynchronous task is created and calls this method
         * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
         * @param valueIWantToSend
         */
        public String postData(String valueIWantToSend) {
            // Create new HttpClient and HTTPPOST
            InputStream inputStream = null;
            StringBuilder builder=new StringBuilder();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://128.83.52.253:8079/test.py/nestGet");     //this is the url of our post servlet for our web application
            try {
                String paramstring="test hope this works";
                response = httpclient.execute(httppost);           //currently, no response is returned by webiste
                HttpEntity entity = response.getEntity();
                if(entity != null) {
                    HttpEntity entity2 = response.getEntity();
                    InputStream content = entity2.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                    myresult=builder.toString();
                }
            } catch (ClientProtocolException e) {
                Log.e(null, "caught exception:CLIENT PROTO..");  //log for debugging in Android studio console.
            }
            catch (IOException e) {
                Log.e(null, "caught exception:IO EXCEP..");
            }
            catch (RuntimeException e) {
                Log.e(null, "caught exception:RUNTIME EXCEP...");
            }
            System.out.println("++++++++++++");
            //System.out.println(myresult);
            return builder.toString();
        }
    }

}