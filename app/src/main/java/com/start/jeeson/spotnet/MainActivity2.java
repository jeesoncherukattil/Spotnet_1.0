 package com.start.jeeson.spotnet;

 import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

 public class MainActivity2 extends Activity {

    private Handler mHandler = new Handler();

    private long mStartRX = 0;

    private long mStartTX = 0;


    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.usage);
        Log.d("aessss", "Service nnnnnnnnnn");


new Connection().execute();



       // Intent i=new Intent(MainActivity2.this,serv1.class);
       // i.putExtra("limit","1");
        //MainActivity2.this.startService(i);


            if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Uh Oh!");

                alert.setMessage("Your device does not support traffic stat monitoring.");

                alert.show();

            } else {




            }

    }

    private final Runnable mRunnable = new Runnable() {

        public void run() {

            TextView RX = (TextView) findViewById(R.id.RX);

            TextView TX = (TextView) findViewById(R.id.TX);

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

            RX.setText(Long.toString(rxBytes));

            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            TX.setText(Long.toString(txBytes));

            mHandler.postDelayed(mRunnable, 1000);

        }

    };

     private class Connection extends AsyncTask {

         @Override
         protected Object doInBackground(Object... arg0) {
          //   Log.i("aess", "Service started");

             if(hasActiveInternetConnection(MainActivity2.this))
             {
                 //START SERVICE AND ALL HERE
                 Log.d("aess", "Service started");
                 Intent i=new Intent(MainActivity2.this,serv1.class);
                 i.putExtra("limit","1");
                 startService(i);
mStartTX=0;
                 mStartRX=0;
                 mStartRX = TrafficStats.getTotalRxBytes();

                 mStartTX = TrafficStats.getTotalTxBytes();

                 mHandler.postDelayed(mRunnable, 1000);
             }
             else
             {
                 //PRINT NO INTERNET CONNECTION
//                 Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
             }
             return null;
         }

     }
     public static boolean isNetworkAvailable(Activity activity) {
         ConnectivityManager connectivity = (ConnectivityManager) activity
                 .getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivity == null) {
             return false;
         } else {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null) {
                 for (int i = 0; i < info.length; i++) {
                     if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                         return true;  //<--  --  -- Connected
                     }
                 }
             }
         }
         return false;  //<--  --  -- NOT Connected
     }



     public static boolean hasActiveInternetConnection(Activity context) {
         if (isNetworkAvailable(context)) {
             try {
                 HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                 urlc.setRequestProperty("User-Agent", "Test");
                 urlc.setRequestProperty("Connection", "close");
                 urlc.setConnectTimeout(1500);
                 urlc.connect();
                 return (urlc.getResponseCode() == 200);
             } catch (IOException e) {
                 Log.d("jisjoe", "Error checking internet connection", e);
             }
         } else {
             Log.d("jisjoe", "No network available!");
         }
         return false;
     }
}