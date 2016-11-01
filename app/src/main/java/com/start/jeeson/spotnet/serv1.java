package com.start.jeeson.spotnet;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class serv1 extends Service {
    float Dlimit,flag=0;
    // final float slimit;
    private Handler mHandler = new Handler();

    long mStartRX = 0;

    long mStartTX = 0;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(serv1.this,"Service starts",Toast.LENGTH_SHORT).show();

        Log.i("aaa", "Service started");
        String limit = intent.getStringExtra("limit");
      final float Dlimit = Float.parseFloat(limit);
        Log.i("flsg value",""+Dlimit);
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        Toast.makeText(serv1.this, "service running", Toast.LENGTH_LONG).show();
        mHandler.postDelayed(mRunnable, 1000);


       // againStartGPSAndSendFile();
        return START_STICKY;



    }


   // public void ActionStartsHere() {
     //   mStartRX = TrafficStats.getTotalRxBytes();

       // mStartTX = TrafficStats.getTotalTxBytes();
       // mHandler.postDelayed(mRunnable, 1000);
       // againStartGPSAndSendFile();

    //}




  //  public void againStartGPSAndSendFile() {
   //     new CountDownTimer(11000, 10000) {
   //         @Override
    //        public void onTick(long millisUntilFinished) {


               // long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

               // float txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

                //float rxBytesmb= rxBytes/100000;

               // String pdata = intent.getStringExtra("limit");
                //Log.v("received",""+rxBytes);
                //Log.v("sent",""+txBytes+);

                //Log.i("passed limit",""+slimit);
              /*  if (rxBytesmb >= slimit) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    int networkId = wifiManager.getConnectionInfo().getNetworkId();
                    wifiManager.removeNetwork(networkId);
                    wifiManager.saveConfiguration();
                    Toast.makeText(serv1.this, "limit exceed", Toast.LENGTH_LONG).show();
                    //Log.i("sent","exeeded");
                }*/
     //       }
      //      @Override
        //    public void onFinish() {
          //      ActionStartsHere();

  //          }


  //      }.start();
 //   }

/*
        String limit = intent.getStringExtra("limit");

        mStartRX = TrafficStats.getTotalRxBytes();

        mStartTX = TrafficStats.getTotalTxBytes();

        long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

        float txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            float rxBytesmb= rxBytes/100000;

        String pdata = intent.getStringExtra("limit");
        Log.i("received",""+rxBytesmb);
        //Log.v("sent",""+txBytes+);
        float slimit =Float.parseFloat(pdata);
        Log.i("passed limit",""+slimit);
        if (rxBytesmb >= slimit) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.getConnectionInfo().getNetworkId();
            wifiManager.removeNetwork(networkId);
            wifiManager.saveConfiguration();
            Toast.makeText(serv1.this, "limit exceed", Toast.LENGTH_LONG).show();
            //Log.i("sent","exeeded");
        }*/


    private final Runnable mRunnable = new Runnable() {

        public void run() {




        long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

        long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;


        float rxBytesmb = rxBytes /10000;

        Log.i("slimit"+Dlimit, "received" + rxBytesmb);
        if (rxBytesmb >= 1) {
            try {

               // flag = 1;
                mHandler.removeCallbacks(mRunnable);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int networkId = wifiManager.getConnectionInfo().getNetworkId();
                wifiManager.removeNetwork(networkId);
                wifiManager.saveConfiguration();
                Toast.makeText(serv1.this, "limit exceeded", Toast.LENGTH_LONG).show();
                Intent i=new Intent(serv1.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            catch (Exception e){}

        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }

    }


    };



}
