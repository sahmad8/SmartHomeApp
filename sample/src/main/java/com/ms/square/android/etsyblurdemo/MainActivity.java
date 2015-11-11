package com.ms.square.android.etsyblurdemo;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.R;
import com.ms.square.android.com.saadahmad.smarthome.AwayActivity;
import com.ms.square.android.com.saadahmad.smarthome.FacialRecon;
import com.ms.square.android.com.saadahmad.smarthome.Intruder;
import com.ms.square.android.com.saadahmad.smarthome.OkidokeysSetLockAsyncTask;
import com.ms.square.android.com.saadahmad.smarthome.RSBlurFragment;
import com.ms.square.android.com.saadahmad.smarthome.SetLock;
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
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {


    Timer timer=null;
    private boolean away_mode=false;

    private int targetTemperature = 75;
    public String test=null;
    private JSONObject nestData;
    private Intent intent = null;
    private Bundle extras = null;
    private boolean lockOn;
    private boolean faceRecon;
    Switch away_switch=null;
    public MediaPlayer mp3_notify_known;
    public MediaPlayer mp3_notify_uknown;
    private String personatmydoor="null";
    boolean changed=false;



    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent= getIntent();
        extras = intent.getExtras();
        lockOn = extras.getBoolean("lock");
        faceRecon = extras.getBoolean("faceRecon");
        System.out.println("YYYYYYYYY");
        System.out.println(lockOn);
        System.out.println(faceRecon);
        RSBlurFragment ft = new RSBlurFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mp3_notify_known=MediaPlayer.create(this.getBaseContext(), R.raw.notifyknown);
        this.myActivity=this;
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        StringBuilder display = new StringBuilder();
        try {
            nestData = new JSONObject(intent.getStringExtra("nestData"));
            Toast.makeText(this, "The target is "+ nestData.getString("target") , Toast.LENGTH_LONG).show();
            display.append("Current Temperature: ");
            display.append(nestData.getString("temperature") + "\n");
            display.append("Target Temperature: ");
            display.append(nestData.getString("target") + "\n\n");
            display.append("Away status: "+nestData.getString("away"));
            away_mode=nestData.getBoolean("away");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        TextView dataDisplay = (TextView) findViewById(R.id.dataDisplay);
        dataDisplay.setText(display.toString());
        if (away_mode || faceRecon) {
            if (away_mode)
            {
                faceRecon=false;
            }
            if (faceRecon)
            {
                away_mode=false;
            }
            final Handler handler = new Handler();
            timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            try {
                                //changed=false;
                                PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask(myActivity);
                                // PerformBackgroundTask this class is the class that extends AsynchTask
                                performBackgroundTask.execute("dont matter");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                            if (changed) {
                                    mp3_notify_known.start();
                                    Toast.makeText(getBaseContext(), personatmydoor + " is at ur door!!", Toast.LENGTH_LONG).show();
                                    new OkidokeysSetLockAsyncTask().execute("lockOn");
                            }
                            handler.removeCallbacks(this);
                        }
                    }, 6000);
                    System.out.println("GETHEREEVERYtIME:::!");
                }

            };
            System.out.println("About to schedule\n");
            timer.schedule(doAsynchronousTask, 0, 8000); //execute in every 50000 ms
        }
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
                mTitle = "Home Page";
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                Intent intent=new Intent(this, SetTemp.class);
                if (timer!=null) {
                    timer.cancel();
                    timer = null;
                }
                intent.putExtra("nestData", nestData.toString());
                intent.putExtra("lock", lockOn);
                intent.putExtra("faceRecon", faceRecon);
                startActivity(intent);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                Intent lock_intent=new Intent(this, SetLock.class);
                if (timer!=null) {
                    timer.cancel();
                    timer = null;
                }
                lock_intent.putExtra("nestData", nestData.toString());
                lock_intent.putExtra("lock", lockOn);
                lock_intent.putExtra("faceRecon",faceRecon);
                startActivity(lock_intent);
                break;
            case 4:
                mTitle="Away/Home";
                Intent away_intent=new Intent(this, AwayActivity.class);
                if (timer!=null) {
                    timer.cancel();
                    timer = null;
                }
                away_intent.putExtra("nestData", nestData.toString());
                away_intent.putExtra("lock", lockOn);
                away_intent.putExtra("faceRecon", faceRecon);
                startActivity(away_intent);
                break;
            case 5:
                mTitle = "FacialRecon";
                Intent face=new Intent(this, FacialRecon.class);
                if (timer!=null) {
                    timer.cancel();
                    timer = null;
                }
                face.putExtra("nestData", nestData.toString());
                face.putExtra("lock", lockOn);
                face.putExtra("faceRecon",faceRecon);
                startActivity(face);
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



    private class PerformBackgroundTask extends AsyncTask<String, Integer, Double> {

        private Activity activity=null;

        public PerformBackgroundTask (Activity activity)
        {
            this.activity=activity;
        }

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            final String returned=postData(params[0]);
            return null;
        }

        /**
         * onPosttExecute-displays toast upon http execute completion
         * we can exit application here if something goes wrong.
         * @param result
         */
        protected void onPostExecute(Void result){
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
         * Uses http address with IP address of apache running on the board, along with pron #
         * @param valueIWantToSend
         */
        public String postData(String valueIWantToSend) {
//  This code returns an image stored on the server.
           InputStream in=null;
            if (away_mode) {
                try {
                    in = new URL("http://128.83.52.253:8079/imagetest.py/showImage").openStream();
                    //bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e(null, "caught exception");
                    test = "Exception";
                    return null;
                    // log error
                }
                test = "GOT IT";
                timer.cancel();
                Intent intrude=new Intent(activity.getBaseContext(), Intruder.class);
                intrude.putExtra("Target", "motion");
                startActivity(intrude);
                return null;
            }
            else if (faceRecon)
            {
                InputStream inputStream = null;
                StringBuilder builder=new StringBuilder();
                HttpResponse response=null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://128.83.52.253:8079/imagetest.py/faceRecon");     //this is the url of our post servlet for our web application
                try {
                    String paramstring="test hope this works";
                    response = httpclient.execute(httppost);           //currently, no response is returned by webiste
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        HttpEntity entity2 = response.getEntity();
                        InputStream content = entity2.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
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
                changed=false;
               // personatmydoor=builder.toString();
                if (personatmydoor.toString().equals(builder.toString()))
                {
                    return null;
                }
                personatmydoor=builder.toString();
                if (personatmydoor.toString().equals("Unknown"))
                {
                    timer.cancel();
                    Intent myintent=new Intent(activity, Intruder.class);
                    myintent.putExtra("Target", "face");
                    startActivity(myintent);
                }
                changed=true;
            return builder.toString();
            }
            return null;
        }
    }



}
