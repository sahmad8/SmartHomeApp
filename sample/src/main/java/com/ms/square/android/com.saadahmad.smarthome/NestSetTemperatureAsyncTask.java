package com.ms.square.android.com.saadahmad.smarthome;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.square.android.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Saad Ahmad on 9/30/2015.
 * Class for setting Nest temperature. Just pass the integer value and call
 * execute on this class (look at SetTemp class)
 */
public class NestSetTemperatureAsyncTask extends AsyncTask<String, Integer, Double> {

    boolean success=false;
    private ProgressBar pb;
    HttpResponse response;
    public String myresult=null;
    public Integer temp_requested=null;
    private ImageView imageView;


    @Override
    protected Double doInBackground(String... params) {
        // TODO Auto-generated method stub
        temp_requested=Integer.parseInt(params[0]);
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

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://128.83.52.253:8079/test.py/nestSet?temperature="+valueIWantToSend);     //this is the url of our post servlet for our web application
            try {
                String paramstring="test hope this works";
                response = httpclient.execute(httppost);           //currently, no response is returned by webiste
                HttpEntity entity = response.getEntity();
                if(entity != null) {
                    myresult = EntityUtils.toString(entity);
                    return EntityUtils.toString(entity);
                }
            } catch (ClientProtocolException e) {

            }
            catch (IOException e) {
            }
            catch (RuntimeException e) {

            }
        return "no string";
    }
}

