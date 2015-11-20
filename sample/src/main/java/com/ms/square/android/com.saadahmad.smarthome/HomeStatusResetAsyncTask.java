package com.ms.square.android.com.saadahmad.smarthome;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.IOException;

/**
 * Created by Saad Ahmad on 11/11/2015.
 */
public class HomeStatusResetAsyncTask extends AsyncTask<String, Integer, Double> {


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
     */
    public String postData(String valueIWantToSend) {
        // Create new HttpClient and HTTPPOST

        HttpClient httpclient = new DefaultHttpClient();
        System.out.println("__________________________");
        System.out.println(valueIWantToSend);

        HttpPost httppost = new HttpPost("http://128.83.52.253:8079/imagetest.py/faceInit");     //this is the url of our post servlet for our web application
        try {
            String paramstring="test hope this works";
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
