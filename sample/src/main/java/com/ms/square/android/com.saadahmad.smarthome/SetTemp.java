package com.ms.square.android.com.saadahmad.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.etsyblurdemo.MainActivity;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saad Ahmad on 10/2/2015.
 * Class for setting the temperature
 * Works by calling NestAsyncTask and passing an integer to it
 * That will set the target temp on nest thermostat
 */
public class SetTemp extends AppCompatActivity  {
    NumberPicker np;
    Integer setting=75;
    private Button btn;
    private int maxTemp = 90;
    private int minTemp = 50;
    private String myresult= null;
    private JSONObject nestData;
    private int targetTemperature = 75;
    HttpResponse response;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temp);

        np = (NumberPicker) findViewById(R.id.numberPicker);
        btn = (Button) findViewById(R.id.button1);
        np.setMaxValue(maxTemp);
        np.setMinValue(minTemp);
        //btn.setOnClickListener(this);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
           setting=newVal;
        }
    });
        //Bundle b =  getIntent().getExtras();
        Intent intent= getIntent();
        StringBuilder display = new StringBuilder();

        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
            Toast.makeText(this, "The target is "+ nestData.getString("target") , Toast.LENGTH_LONG).show();
            display.append("Current Temperature: ");
            display.append(nestData.getString("temperature") + "\n");
            display.append("Target Temperature: ");

            double targetTemp = Double.parseDouble(nestData.getString("target"));
            targetTemperature  = (int)targetTemp;
            display.append(targetTemperature + "\n");
            np.setValue(targetTemperature);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        TextView dataDisplay = (TextView) findViewById(R.id.dataDisplay);

        dataDisplay.setText(display.toString());

        
        final Switch fanModeSwitch = (Switch)  findViewById(R.id.fanMode);
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
        final Switch modeSwitch = (Switch)  findViewById(R.id.systemMode);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("SYSTEM");

                if(modeSwitch.isChecked()) {
                    new NestSetModeAsyncTask().execute("heat");
                    Toast.makeText(getBaseContext(), "System is set to heat", Toast.LENGTH_LONG).show();
                }
                else{
                    new NestSetModeAsyncTask().execute("cool");
                    Toast.makeText(getBaseContext(), "System is set to cool", Toast.LENGTH_LONG).show();
                }


            }

        });


    }



    public void increaseTemp(View v){
        if(targetTemperature < 90) {
            targetTemperature++;
            np.setValue(targetTemperature);
        }
    }
    public void decreaseTemp(View v){
        if(targetTemperature > 50) {
            targetTemperature--;
            np.setValue(targetTemperature);
        }
    }

    public void setTemp(View v) {

        new NestSetTemperatureAsyncTask().execute(Integer.toString(np.getValue()));
        Toast.makeText(this, "Set Temperature to "+ np.getValue(), Toast.LENGTH_LONG).show();

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
        Toast.makeText(this, "Set Fan to "+ np.getValue(), Toast.LENGTH_LONG).show();

    }
    public void returnToMain(View v){
        final Intent intent=new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("nestData", nestData.toString());
        startActivity(intent);
    }

}
