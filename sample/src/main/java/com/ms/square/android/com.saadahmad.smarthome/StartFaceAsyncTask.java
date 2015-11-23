package com.ms.square.android.com.saadahmad.smarthome;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Saad Ahmad on 11/15/2015.
 */
public class StartFaceAsyncTask extends AsyncTask <String, Integer, Double>  {

    HttpResponse response;

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
     * poData-handles http actions
     * called when user clicks submit-then asynchronous task is created and calls this method
     * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
     * @param valueIWantToSend
     */
    public String postData(String valueIWantToSend) {
        // Create new HttpClient and HTTPPOST

        HttpClient httpclient = new DefaultHttpClient();
        System.out.println("__________________________");
        System.out.println(valueIWantToSend);

        HttpPost httppost = new HttpPost("http://146.6.65.98:8080/imagetest.py/runFace");     //this is the url of our post servlet for our web application
        try {
            response = httpclient.execute(httppost);           //currently, no response is returned by webiste

        } catch (ClientProtocolException e) {

        }
        catch (IOException e) {
        }
        catch (RuntimeException e) {

        }
        return "no string";
    }
}




