package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Saad Ahmad on 10/2/2015.
 * Class for setting the temperature
 * Works by calling NestAsyncTask and passing an integer to it
 * That will set the target temp on nest thermostat
 */
public class SetTemp extends AppCompatActivity  {

    NumberPicker np;
    private int setting = 75;
    private Drawable image;
    private Button btn;
    private int maxTemp = 90;
    private int minTemp = 50;
    private String myresult = null;
    private JSONObject nestData;
    private int targetTemperature = 75;
    HttpResponse response;
    private boolean lock;
    private boolean faceRecon;
    private boolean nestGetFlag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        nestGetFlag = true;
        new MyAsyncTask().execute("nah");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temp);

//        np = (NumberPicker) findViewById(R.id.numberPicker);
        btn = (Button) findViewById(R.id.button1);
//        np.setMaxValue(maxTemp);
//        np.setMinValue(minTemp);
        //btn.setOnClickListener(this);
//        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//           setting=newVal;
//        }
//    });
        //Bundle b =  getIntent().getExtras();

        Intent intent= getIntent();
        //nestData = resetNestData(intent);






        StringBuilder display = new StringBuilder();

        // display nestDat on screen
        while(nestGetFlag){

        }
        System.out.println("999999999999999");

        final Switch fanModeSwitch = (Switch)  findViewById(R.id.fanMode);
        final Switch modeSwitch = (Switch)  findViewById(R.id.systemMode);

        try {

            nestData = new JSONObject(myresult);
            //nestData = new JSONObject(intent.getStringExtra("nestData"));
            display.append("Current Temperature: ");
            double temp = Double.parseDouble(nestData.getString("temperature"));
            String result = String.format("%.2f", temp);
            display.append(result + "\n");
            display.append("Target Temperature: ");
            temp = Double.parseDouble(nestData.getString("target"));
            targetTemperature = (int)temp;
            result = String.format("%.2f", temp);
            display.append(result + "\n");
            display.append("Away status: "+nestData.getString("away"));

//            np.setValue(targetTemperature);



            ImageView background = (ImageView) findViewById(R.id.background);
            TextView tempNum = (TextView) findViewById(R.id.tempDisplay);
            Button setTemp = (Button) findViewById(R.id.set);
            ImageButton up = (ImageButton) findViewById(R.id.buttonUp);
            ImageButton down = (ImageButton) findViewById(R.id.buttonDown);

            if(nestData.getString("away").equals("true")){
                image = getResources().getDrawable(R.drawable.nest_away);
                background.setImageDrawable(image);
                modeSwitch.setEnabled(false);
                fanModeSwitch.setEnabled(false);
                setTemp.setEnabled(false);
                down.setEnabled(false);
                up.setEnabled(false);
                tempNum.setVisibility(View.INVISIBLE);
            }
            else {
                modeSwitch.setEnabled(true);
                fanModeSwitch.setEnabled(true);
                setTemp.setEnabled(true);
                down.setEnabled(true);
                up.setEnabled(true);
                tempNum.setVisibility(View.VISIBLE);


                if (nestData.getString("mode").equals("cool")) {
                    modeSwitch.setChecked(false);
                    image = getResources().getDrawable(R.drawable.nest_cool);
                    background.setImageDrawable(image);
                } else {
                    modeSwitch.setChecked(true);
                    image = getResources().getDrawable(R.drawable.nest_heat);
                    background.setImageDrawable(image);
                }
                if(nestData.getString("fan").equals("false")){
                    fanModeSwitch.setChecked(false);
                }
                else{
                    fanModeSwitch.setChecked(true);
                }
            }
            System.out.println(nestData);

            TextView tempDisplay = (TextView) findViewById(R.id.tempDisplay);
            tempDisplay.setText(String.valueOf(targetTemperature));
            setting = targetTemperature;

        }catch (JSONException e) {
            e.printStackTrace();
        }
        TextView dataDisplay = (TextView) findViewById(R.id.dataDisplay);

        dataDisplay.setText(display.toString());



        fanModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("FANNNNNNN");

                if (fanModeSwitch.isChecked()) {
                    new NestSetFanAsyncTask().execute("on");
                    Toast.makeText(getBaseContext(), "Fan set to on", Toast.LENGTH_LONG).show();
                } else {
                    new NestSetFanAsyncTask().execute("auto");
                    Toast.makeText(getBaseContext(), "Fan set to auto", Toast.LENGTH_LONG).show();
                }


            }

        });
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("SYSTEM");
                ImageView background = (ImageView) findViewById(R.id.background);
                if(modeSwitch.isChecked()) {
                    new NestSetModeAsyncTask().execute("heat");
                    Toast.makeText(getBaseContext(), "System is set to heat", Toast.LENGTH_LONG).show();
                    image = getResources().getDrawable(R.drawable.nest_heat);
                    background.setImageDrawable(image);
                }
                else{
                    new NestSetModeAsyncTask().execute("cool");
                    Toast.makeText(getBaseContext(), "System is set to cool", Toast.LENGTH_LONG).show();
                    image = getResources().getDrawable(R.drawable.nest_cool);
                    background.setImageDrawable(image);
                }


            }

        });


    }



    public void increaseTemp(View v){
        if(targetTemperature < 90) {
            targetTemperature++;
//            np.setValue(targetTemperature);

            setting = targetTemperature;
            TextView tempDisplay = (TextView) findViewById(R.id.tempDisplay);
            tempDisplay.setText(String.valueOf(setting));

        }
    }
    public void decreaseTemp(View v){
        if(targetTemperature > 50) {
            targetTemperature--;
//            np.setValue(targetTemperature);
            setting = targetTemperature;
            TextView tempDisplay = (TextView) findViewById(R.id.tempDisplay);
            tempDisplay.setText(String.valueOf(setting));
        }

    }

    public void setTemp(View v) {

        new NestSetTemperatureAsyncTask().execute(Integer.toString(setting));
        Toast.makeText(this, "Set Temperature to "+ setting /*np.getValue()*/, Toast.LENGTH_LONG).show();

//        StringBuilder builder=new StringBuilder();
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://128.83.52.253:8079/test.py/nestSet?temperature=" + setting.toString());     //this is the url of our post servlet for our web application
//        try {
//            String paramstring="test hope this works";
//            response = httpclient.execute(httppost);           //currently, no response is returned by webiste
//            HttpEntity entity = response.getEntity();
//            if(entity != null) {
//                HttpEntity entity2 = response.getEntity();
//                InputStream content = entity2.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                String line;
//                while((line = reader.readLine()) != null){
//                    builder.append(line);
//                }
//                myresult=builder.toString();
//            }
//        } catch (ClientProtocolException e) {
//            Log.e(null, "caught exception:CLIENT PROTO..");  //log for debugging in Android studio console.
//        }
//        catch (IOException e) {
//            Log.e(null, "caught exception:IO EXCEP..");
//        }
//        catch (RuntimeException e) {
//            Log.e(null, "caught exception:RUNTIME EXCEP...");
//        }


    }
    public void setFan(){
        new NestSetFanAsyncTask().execute(Integer.toString(np.getValue()));
        Toast.makeText(this, "Set Fan to "+ setting /*np.getValue()*/, Toast.LENGTH_LONG).show();

    }
    public void returnToMain(View v){
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("nestData", nestData.toString());
        intent.putExtra("faceRecon", false);
        startActivity(intent);
        myresult = null;
    }
    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            final String returned = postData(params[0]);
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

        /**
         * poData-handles http actions
         * called when user clicks submit-then asynchronous task is created and calls this method
         * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
         * @param valueIWantToSend
         */
        public String postData(String valueIWantToSend) {
            InputStream inputStream = null;
            StringBuilder builder=new StringBuilder();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://128.83.52.253:8079/test.py/nestGet");     //this is the url of our post servlet for our web application
            try {

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
            System.out.println(myresult);
        try {
            nestData = new JSONObject(myresult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            System.out.println(nestData.toString());
            nestGetFlag = false;
            return builder.toString();

        }


    }

}
