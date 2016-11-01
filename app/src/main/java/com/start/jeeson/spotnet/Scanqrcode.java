package com.start.jeeson.spotnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class Scanqrcode extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    int match=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the main content layout of the Activity

        setContentView(R.layout.activity_qrscanner);
    }

    //product barcode mode

    public void scanBar(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException anfe) {

            //on catch, show the download dialog
            showDialog(Scanqrcode.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }


    //product qr code mode
    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(Scanqrcode.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }
    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                //toast.show();
                String[] str_array = contents.split(":");
                final String limit = str_array[0];
                String password = str_array[1];
                Toast toast = Toast.makeText(this, "limit" + str_array[0] + "password" + str_array[1], Toast.LENGTH_LONG);
                toast.show();
                try {


                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                    wifiManager.disconnect();
                    String networkSSID = "Spotnet";
                    String networkPass = str_array[1];

                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    wifiConfig.SSID = String.format("\"%s\"", networkSSID);
                    wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

                    //WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
                    wifiManager.addNetwork(wifiConfig);

                    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                    for (final WifiConfiguration i : list) {
                        // Toast.makeText(this, "limit" +networkSSID, Toast.LENGTH_LONG).show();
                        if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                            wifiManager.disconnect();
                            wifiManager.enableNetwork(i.networkId, true);
                            wifiManager.reconnect();
                            Log.v("ssids=", "" + networkSSID);


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    //  buttons[inew][jnew].setBackgroundColor(Color.BLACK);
                                    Intent usage = new Intent(Scanqrcode.this, MainActivity2.class);
                                    usage.putExtra("limit", limit);
                                    startActivity(usage);

                                }
                            }, 10000);
                            // match=1;
                            break;
                        }
                    }


                    //if(match==0) {
                    // int netId = wifiManager.addNetwork(wifiConfig);
                    //wifiManager.enableNetwork(netId, true);
                    //wifiManager.setWifiEnabled(true);
                    // }


                    // int netId = wifiManager.addNetwork(wifiConfig);
                    //wifiManager.disconnect();
                    //wifiManager.enableNetwork(netId, true);
                    //wifiManager.reconnect();
                    // Intent i=new Intent(Scanqrcode.this,serv1.class);
                    //i.putExtra("limit",limit);
                    //Scanqrcode.this.startService(i);

                }catch (Exception e){}
            }
        }
    }
}