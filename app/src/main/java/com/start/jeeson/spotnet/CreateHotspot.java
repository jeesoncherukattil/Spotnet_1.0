package com.start.jeeson.spotnet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class CreateHotspot extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createhotspot);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Configure"));
        tabLayout.addTab(tabLayout.newTab().setText("Connected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void generateqrcode(View view) {
        EditText qrInput = (EditText) findViewById(R.id.password);
        EditText dlimtit = (EditText) findViewById(R.id.etUserName);
         String qrInputText = dlimtit.getText().toString()+":"+qrInput.getText().toString();
      //  Log.v(LOG_TAG, qrInputText);

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }


    public void onoffhotspot(View view) {

        EditText password = (EditText) findViewById(R.id.password);
        //EditText ssid = (EditText) findViewById(R.id.hssid);
        String hpass      = password.getText().toString();
       // String hssid      = ssid.getText().toString();
        if(hpass.equals(""))
        {
            Toast.makeText(CreateHotspot.this, "Enter the details", Toast.LENGTH_LONG).show();
        }
        else {
            boolean checked = ((ToggleButton) view).isChecked();
            if (checked) {
                Toast.makeText(CreateHotspot.this, "hotspot starting", Toast.LENGTH_LONG).show();
                WifiManager wifimanager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

                if (wifimanager.isWifiEnabled()) {
                    wifimanager.setWifiEnabled(false);
                }

                Method[] wmMethods = wifimanager.getClass().getDeclaredMethods();
                boolean methodFound = false;
                for (Method method : wmMethods) {
                    if (method.getName().equals("setWifiApEnabled")) {
                        methodFound = true;
                        WifiConfiguration netConfig = new WifiConfiguration();
                        netConfig.SSID = "Spotnet";
                        netConfig.preSharedKey = hpass;
                        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

                        try {
                            boolean apstatus = (boolean) method.invoke(wifimanager, netConfig, true);
                            for (Method isWifiApEnabledmethod : wmMethods) {
                                if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                                    while (!(Boolean) isWifiApEnabledmethod.invoke(wifimanager)) {
                                    }
                                    ;
                                    for (Method method1 : wmMethods) {
                                        if (method1.getName().equals("getWifiApState")) {
                                            int apstate;
                                            apstate = (Integer) method1.invoke(wifimanager);
                                            Log.i(this.getClass().toString(), "Apstate ::: " + apstate);
                                        }
                                    }
                                }
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }


                    }
                }

                if (!methodFound) {
                    Log.d("Splash Activity",
                            "cannot configure an access point");
                }
            }

        else {
                Toast.makeText(CreateHotspot.this, "Hotspot Stopping", Toast.LENGTH_SHORT).show();
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

                Method[] methods = wifiManager.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals("setWifiApEnabled")) {
                        try {
                            method.invoke(wifiManager, null, false);
                        } catch (Exception ex) {
                        }
                        break;
                    }
                }
            }
        }

    }

}