package com.ms.square.android.etsyblurdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.renderscript.ScriptIntrinsicBlur;

import com.ms.square.android.R;
import com.ms.square.android.com.saadahmad.smarthome.BlurBuilder;
import com.ms.square.android.com.saadahmad.smarthome.SetTemp;

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
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {


    public String json_String= null;
    public String test=null;
    private JSONObject nestData;
    private Intent intent = null;
    Switch away_switch=null;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent= getIntent();
        json_String=intent.getStringExtra("nestData");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //Toast.makeText(this, json_String, Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        final PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            updateDisplayData(intent);
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute("dont matter");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                        while(test==null){

                        }
                        System.out.println("AJBDA"+test);
                    }
                });
            }

        };


        timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();//debugging purposes
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                final Intent intent=new Intent(getBaseContext(), SetTemp.class);
                intent.putExtra("nestData", json_String);
                startActivity(intent);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
       // actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_dialog) {
            BlurDialogFragment fragment = BlurDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(), "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private ImageView image;
        private TextView text;
        private TextView statusText;


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    /**
     * Asynchornous subclass, MyAsyncTask
     * extends asynchronous task
     * overrides background method and executes http actions
     */
    private void updateDisplayData(Intent intent){
            StringBuilder builder=new StringBuilder();
            HttpResponse response=null;
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
                    json_String=builder.toString();
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
            StringBuilder display = new StringBuilder();

            try {
                nestData = new JSONObject(json_String);
                display.append("Current Temperature: ");
                display.append(nestData.getString("temperature") + "\n");
                display.append("Target Temperature: ");
                display.append(nestData.getString("target") + "\n");
                System.out.println(nestData.getString("target"));

            }catch (JSONException e) {
                e.printStackTrace();
            }
            TextView dataDisplay = (TextView) findViewById(R.id.dataDisplay);

            dataDisplay.setText(display.toString());
            System.out.println("HELLLLLLLOOOOOOO");


    }

    private class PerformBackgroundTask extends AsyncTask<String, Integer, Double> {

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
            //pb.setProgress(progress[0]);
        }

        /**
         * poData-handles http actions
         * called when user clicks submit-then asynchronous task is created and calls this method
         * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
         * @param valueIWantToSend
         */
        public String postData(String valueIWantToSend) {
            // Create new HttpClient and HTTPPOST
/*   This commented code returns an image stored on the server.
            try {
                InputStream in = new URL("http://128.83.52.253:8079/test.py/nestGet").openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(null, "caught exception");
                // log error
            }
            myresult="GOT IT";
            return null;
*/



            InputStream inputStream = null;
            StringBuilder builder=new StringBuilder();
            HttpResponse response=null;
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
                    test=builder.toString();
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
            StringBuilder display = new StringBuilder();

            try {
                nestData = new JSONObject(json_String);
                display.append("Current Temperature: ");
                display.append(nestData.getString("temperature") + "\n");
                display.append("Target Temperature: ");
                display.append(nestData.getString("target") + "\n");

            }catch (JSONException e) {
                e.printStackTrace();
            }
            TextView dataDisplay = (TextView) findViewById(R.id.dataDisplay);

            dataDisplay.setText(display.toString());

            return builder.toString();
        }
    }

}
